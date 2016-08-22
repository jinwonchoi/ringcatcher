package com.eclues.ringcatcher.ctrl;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eclues.ringcatcher.dao.AdminInfoDAO;
import com.eclues.ringcatcher.dao.CustomerDAO;
import com.eclues.ringcatcher.dao.EnvironmentBean;
import com.eclues.ringcatcher.dao.MsgInfoDAO;
import com.eclues.ringcatcher.dao.RingHistoryDAO;
import com.eclues.ringcatcher.dao.RingInfoDAO;
import com.eclues.ringcatcher.dao.UserInfoDAO;
import com.eclues.ringcatcher.etc.ReturnCode;
import com.eclues.ringcatcher.io.ItemResult;
import com.eclues.ringcatcher.io.ContactListResult;
import com.eclues.ringcatcher.io.InviteDetail;
import com.eclues.ringcatcher.io.InviteResult;
import com.eclues.ringcatcher.io.ItemResult;
import com.eclues.ringcatcher.io.ListResult;
import com.eclues.ringcatcher.io.RegisterResult;
import com.eclues.ringcatcher.io.ReqContactList;
import com.eclues.ringcatcher.io.ReqList;
import com.eclues.ringcatcher.io.ReqRingInfo;
import com.eclues.ringcatcher.io.ReqRingUpdate;
import com.eclues.ringcatcher.io.ReqUserInfo;
import com.eclues.ringcatcher.io.RequestMap;
import com.eclues.ringcatcher.io.RingUpdateResult;
import com.eclues.ringcatcher.io.UserListResult;
import com.eclues.ringcatcher.obj.AdminInfo;
import com.eclues.ringcatcher.obj.Customer;
import com.eclues.ringcatcher.obj.MsgInfo;
import com.eclues.ringcatcher.obj.RingHistory;
import com.eclues.ringcatcher.obj.RingInfo;
import com.eclues.ringcatcher.obj.UserInfo;
import com.eclues.ringcatcher.service.FileService;
import com.eclues.ringcatcher.service.GcmSender;

@Controller
@RequestMapping("admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminInfoDAO adminInfoDAO;

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private MsgInfoDAO msgInfoDAO;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	
	/**
	응답메시지
	* 정상등록: RES-A01
	{"resultCode":"0001" 
	,"resultMsg":"SUCCESS"} 

	* 이미 등록: RES-A02 
	{"resultCode":"1002" 
	,"resultMsg":"Already registered"}
	 
	* 등록정보 업데이트: RES-A03 
	{"resultCode":"1003" 
	,"resultMsg":"User info updated"}

	 * 오류 발생: RES-A11  --> 재시도
	{"resultCode":"4001" 
	,"resultMsg":"Unknown Error"}

	 */
	@RequestMapping(value="/registeradmin", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RegisterResult registerAdmin(@RequestBody AdminInfo adminInfo) {
		
		logger.info("registerAdmin:"+adminInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		RegisterResult result =  new RegisterResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			
			AdminInfo resultInfo = adminInfoDAO.get(adminInfo.getUserId());
			logger.debug("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				adminInfoDAO.update(adminInfo);
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
			} else {
				adminInfoDAO.create(adminInfo);
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
			logger.debug("result="+result);;

			transactionManager.commit(txStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Admin Register fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerAdmin:"+adminInfo+"=>"+result);
		return result;
	}
	
	@RequestMapping(value="/unregisteradmin", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RegisterResult unregisterAdmin(@RequestBody AdminInfo adminInfo) {
		
		logger.info("unregisterAdmin:"+adminInfo);

		RegisterResult result =  new RegisterResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			
			AdminInfo resultInfo = adminInfoDAO.get(adminInfo.getUserId());
			logger.info("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				adminInfoDAO.delete(adminInfo.getUserId());
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
				transactionManager.commit(txStatus);
			} else {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			}
			logger.debug("result="+result);;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Admin Unregister fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("unregisterAdmin:"+adminInfo+"=>"+result);
		return result;
	}

	/**
	4. 대기중인 링정보 체크 및 다운로드
	 path : ringupdate
			ringupdate.jsp
	{ "user_id":"01042047792"
	, "user_num":"01042047792"}
	return OK
	{"updateMap":{"01066663333":"\/home\/jinnon\/file_repo\/\/20160402\/01066663333_010444448888.jpg"},"resultCode":"0001","resultMsg":"Sucess"}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}

	*/
	@RequestMapping(value="/authenticateadmin", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ItemResult authenticateAdmin(@RequestBody AdminInfo reqAdminInfo) {
		logger.info("authenticateadmin:"+reqAdminInfo);
		ItemResult result =  new ItemResult();
		try {
			AdminInfo resultInfo = adminInfoDAO.get(reqAdminInfo.getUserId());
			
			if (resultInfo != null) {
				if (reqAdminInfo.getUserPasswd().equals(resultInfo.getUserPasswd())) {
					result.setResultCode(ReturnCode.SUCCESS.get());
					result.setResultMsg(ReturnCode.STR_SUCCESS.get());
					result.setItem(resultInfo);
				} else {
					result.setResultCode(ReturnCode.ERROR_WRONG_PASSWD.get());
					result.setResultMsg(ReturnCode.STR_ERROR_WRONG_PASSWD.get());
					result.setItem(null);
				}
			} else {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
			}

		} catch (Exception e) {
			logger.error("authenticateadmin failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("authenticateadmin:"+reqAdminInfo+"=>"+result);
		return result;
	}
	
	@RequestMapping(value="/registeruser", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ItemResult registerUser(@RequestBody UserInfo userInfo) {
		
		logger.info("registerUser:"+userInfo);
		ItemResult result =  new ItemResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			
			UserInfo resultInfo = userInfoDAO.get(userInfo.getUserNum());
			logger.debug("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				userInfoDAO.update(userInfo);
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
			} else {
				userInfoDAO.create(userInfo);
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
			logger.debug("result="+result);;

			transactionManager.commit(txStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Register fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerUser:"+userInfo+"=>"+result);
		return result;
	}
	
	@RequestMapping(value="/unregisteruser", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ItemResult unregisterUser(@RequestBody UserInfo userInfo) {
		
		logger.info("unregisterUser:"+userInfo);

		ItemResult result =  new ItemResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			
			UserInfo resultInfo = userInfoDAO.get(userInfo.getUserNum());
			logger.info("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				userInfoDAO.delete(userInfo.getUserNum());
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
				transactionManager.commit(txStatus);
			} else {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			}
			logger.debug("result="+result);;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("User Unregister fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("unregisterUser:"+userInfo+"=>"+result);
		return result;
	}
	
	/**
	 *5. 기존정보 재다운로드
	*/
	@RequestMapping(value="/getuserlist", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ListResult getUserList(@RequestBody RequestMap requestMap) {
		
		logger.info("getuserlist:"+requestMap);
		ListResult<UserInfo> result =  new ListResult<UserInfo>();
		try {
			
			List<UserInfo> userInfoList = userInfoDAO.getListBy((HashMap)requestMap);
			if (userInfoList == null || userInfoList.size()==0) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.error("getuserlist:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setList((ArrayList)userInfoList);
			
		} catch (Exception e) {
			logger.error("getuserlist failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getuserlist:"+requestMap+"=>"+result);
		return result;	
	}
	
	@RequestMapping(value="/registermsg", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ItemResult registerUser(@RequestBody MsgInfo msgInfo) {
		
		logger.info("registermsg:"+msgInfo);
		ItemResult result =  new ItemResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			
			MsgInfo resultInfo = msgInfoDAO.get(msgInfo.getUserNum(), msgInfo.getCallingNum());
			logger.debug("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				msgInfoDAO.update(msgInfo);
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
			} else {
				msgInfoDAO.create(msgInfo);
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
			logger.debug("result="+result);;

			transactionManager.commit(txStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Msg Register fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registermsg:"+msgInfo+"=>"+result);
		return result;
	}
	
	@RequestMapping(value="/unregistermsg", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ItemResult unregisterUser(@RequestBody MsgInfo msgInfo) {
		
		logger.info("unregistermsg:"+msgInfo);

		ItemResult result =  new ItemResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {			
			MsgInfo resultInfo = msgInfoDAO.get(msgInfo.getUserNum(),msgInfo.getCallingNum());
			logger.info("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				msgInfoDAO.delete(msgInfo.getUserNum(),msgInfo.getCallingNum());
				result.setResultCode(ReturnCode.UPDATE_OK.get());
				result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
				transactionManager.commit(txStatus);
			} else {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			}
			logger.debug("result="+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("msg Unregister fail", e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("unregistermsg:"+msgInfo+"=>"+result);
		return result;
	}
	
	
	@RequestMapping(value="/getmsglist", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ListResult getMsgList(@RequestBody RequestMap requestMap) {
		
		logger.info("getmsglist:"+requestMap);
		ListResult<MsgInfo> result =  new ListResult<MsgInfo>();
		try {
			
			List<MsgInfo> msgInfoList = msgInfoDAO.getListByAdmin((HashMap)requestMap);
			if (msgInfoList == null || msgInfoList.size()==0) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.error("getuserlist:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setList((ArrayList)msgInfoList);
			
		} catch (Exception e) {
			logger.error("getmsglist failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getmsglist:"+requestMap+"=>"+result);
		return result;	
	}

}
