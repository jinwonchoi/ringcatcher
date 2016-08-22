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

import com.eclues.ringcatcher.dao.CustomerDAO;
import com.eclues.ringcatcher.dao.EnvironmentBean;
import com.eclues.ringcatcher.dao.MsgInfoDAO;
import com.eclues.ringcatcher.dao.RingHistoryDAO;
import com.eclues.ringcatcher.dao.RingInfoDAO;
import com.eclues.ringcatcher.dao.UserInfoDAO;
import com.eclues.ringcatcher.etc.ReturnCode;
import com.eclues.ringcatcher.io.ContactListResult;
import com.eclues.ringcatcher.io.InviteDetail;
import com.eclues.ringcatcher.io.InviteResult;
import com.eclues.ringcatcher.io.RegisterResult;
import com.eclues.ringcatcher.io.ReqContactList;
import com.eclues.ringcatcher.io.ReqRingInfo;
import com.eclues.ringcatcher.io.ReqRingUpdate;
import com.eclues.ringcatcher.io.ReqUserInfo;
import com.eclues.ringcatcher.io.RingUpdateResult;
import com.eclues.ringcatcher.obj.Customer;
import com.eclues.ringcatcher.obj.RingHistory;
import com.eclues.ringcatcher.obj.RingInfo;
import com.eclues.ringcatcher.obj.UserInfo;
import com.eclues.ringcatcher.service.FileService;
import com.eclues.ringcatcher.service.GcmSender;

@Controller
@RequestMapping("json")
public class JSONController {
	private static final Logger logger = LoggerFactory.getLogger(JSONController.class);

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private RingInfoDAO ringInfoDAO;
	
	@Autowired
	private RingHistoryDAO ringHistoryDAO;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
		
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public @ResponseBody Customer getCustomerInJSON(@PathVariable String id) {
		Customer customer = new Customer();
		customer = customerDAO.get(id);
		logger.info("getCustomerInJSON");
		return customer;
	}
	
	@Autowired
	private FileService fileService;

	@Autowired
	private GcmSender gcmSender;

	/**
	 * 미사용
	 * @param userInfo
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping(value="/ringfileup", method=RequestMethod.POST, consumes={"multipart/form-data"})
	public @ResponseBody RegisterResult getRingFileUp(@RequestPart("json") ReqUserInfo userInfo,@RequestPart("upfile") MultipartFile multipartFile) {
		//ReqUserInfo userInfo = new ReqUserInfo();
		logger.info("ringfileup:json:"+userInfo);
		long fileSize = multipartFile.getSize();
		String fileName = multipartFile.getOriginalFilename();
		logger.debug("ringfileup:fileName="+fileName);
		logger.debug("ringfileup:fileSize="+fileSize);

		RegisterResult result =  new RegisterResult();
		if (fileService.saveFile(multipartFile)) {
			result.setResultCode("0001");
			result.setResultMsg(String.format("file[%s] upload OK!!", fileName));
		} else {
			result.setResultCode("2001");
			result.setResultMsg(String.format("file[%s] upload Error!!", fileName));
		}
		return result;
		
	}
	
	
	/**
	 * 1 앱으로 가입
	등록정보 폰번호
	path: /json/register
	{"user_num":"01044445555" 
	,"user_id":"3453245234" <== 내부 생성
	,"user_email":"jinnonspot@gmail.com" 
	,"recom_id":"" 
	,"overwrite":"true|false" (default : false) }
	로직: 
	등록된 것이 없으면 일반 insert후 정상등록으로 응답
	if 등록된것이 있을때 
	  if overwrite=true이면
	   이전 등록정보/이력 모두 삭제(delete)
	  else
	   user_id만 등록/이력 모두 업데이트
	
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

	** 체크사항>
	- recom_id(추천자)값이 DB상에 없으면 오류발생없이 등록처리
	- 이미 등록된 것이 있을때 원래정보 유지하겠다고 하면 email인증할지 고려.	    
	 *       
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RegisterResult registerUser(@RequestBody ReqUserInfo reqUserInfo) {
		
		logger.info("registerUser:"+reqUserInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		RegisterResult result =  new RegisterResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserNum(reqUserInfo.getUserNum());
			userInfo.setUserId(reqUserInfo.getUserId());
			userInfo.setUserEmail(reqUserInfo.getUserEmail());
			userInfo.setRecomId(reqUserInfo.getRecomId());
			
			UserInfo resultInfo = userInfoDAO.get(reqUserInfo.getUserNum());
			logger.debug("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				if (!reqUserInfo.isOverwrite()) {
					result.setResultCode(ReturnCode.USER_EXISTS.get());
					result.setResultMsg(ReturnCode.STR_USER_EXISTS.get());
				} else { 
					userInfoDAO.update(userInfo);
					result.setResultCode(ReturnCode.UPDATE_OK.get());
					result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
				}
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
		logger.info("registerUser:"+reqUserInfo+":"+result);
		return result;
	}
	
		
	/**
	 * 2. 폰북에서 친구초대
	  친구를 선택하고 각각 벨소리를 지정한다.
	  path : invite
	         inviete.jsp
	등록 정보 
	upfile: filename
	json:
	{"userNum":"010444448888"
	,"callingId":"2134129p3481"
	,"callingNum":"01066663333"
	,"ringFileName":"234.mp3"
	} 
	
	String userNum;
	String callingId;
	String callingNum;
	String ringFileName;
	
	return OK
	  {"resultCode":"0001"
	  ,"resultMsg":"SUCCESS"}
	return already registered
	  {"resultCode":"4002"
	  ,"resultMsg":"Error in File upload"}
	return error
	  {"resultCode":"4001"
	  ,"regId":""
	  ,"resultMsg":"Unknown Error"}


	 한 건씩 등록하고 친구에게 어플 초대문자를 보낸다.
	  체크사항> 
	   - 친구가 가입자인지 확인
	    미가입이면 초대문자전송
		가입이면 다음단계	
	파일이 내부이면 file명만 등록
	내부 파일명: internal_00001.mp3
	파일명 {callernum}_{rcv_num}.*
	파일경로 /home/file_repo/YYYYMMDD/...
	  --> insert/update ring_info
	  업로드한 파일은 파일명을 다음과 같이 변경
	 *
	 */
	@RequestMapping(value="/invite", method=RequestMethod.POST, consumes={"multipart/form-data"})
	public @ResponseBody InviteResult inviteUser(@RequestPart("json") ReqRingInfo reqRingInfo,@RequestPart("upfile") MultipartFile multipartFile) {
		
		logger.info("inviteUser:"+reqRingInfo);
		InviteResult result =  new InviteResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;

		try {
			UserInfo userInfo = userInfoDAO.get(reqRingInfo.getCallingNum());
			if (userInfo == null || !userInfo.getUserId().equals(reqRingInfo.getCallingId())) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.error("inviteUser:"+reqRingInfo+":"+result);
				return result;
			}
			String[] userList = reqRingInfo.getUserNum().split(",");
			// save file
			RingInfo ringInfo = new RingInfo();
			//ringInfo.setUserNum(reqRingInfo.getUserNum());
			ringInfo.setCallingNum(reqRingInfo.getCallingNum());
			ringInfo.setCallingName(reqRingInfo.getCallingName());
			String yyyymmdd = fileService.getYYYYMMDDPath();
			String uploadFileName = fileService.getSaveFileName(multipartFile, reqRingInfo.getCallingNum(), userList[0]);
			String savedFilePath = fileService.getSaveFilePath(yyyymmdd,uploadFileName);
			String savedFileUrl = fileService.getSaveFileUrl(yyyymmdd,uploadFileName);
			ringInfo.setRingFileName(savedFileUrl);
			ringInfo.setExpiredDate(reqRingInfo.getExpiredDate());
			ringInfo.setDurationType(reqRingInfo.getDurationType());
			ringInfo.setRegisterDate(yyyymmdd);
			
			if (!fileService.saveFile(multipartFile,yyyymmdd,savedFilePath)) {
				result.setResultCode(ReturnCode.ERROR_FILEUP.get());
				result.setResultMsg(ReturnCode.STR_ERROR_FILEUP.get());
				logger.error("inviteUser:"+reqRingInfo+":"+result);
				return result;
			}			

			//ReqUserInfo userInfo = new ReqUserInfo();
			long fileSize = multipartFile.getSize();
			String fileName = multipartFile.getOriginalFilename();
			System.out.println("ringfileup:fileName="+fileName);
			System.out.println("ringfileup:fileSize="+fileSize);
			
			txStatus =transactionManager.getTransaction(txDef);
			//수신자가 다수인지 확인
			userList = reqRingInfo.getUserNum().split(",");
			String resultUnRegUsers = null;
			InviteResult tempResult =  null;
			if (userList.length == 1) {
				result = doInviteUser(reqRingInfo.getUserNum(), ringInfo, reqRingInfo.getLocale());
			} else {
				result = null;
				for (String userItem : userList) {
					tempResult = doInviteUser(userItem, ringInfo, reqRingInfo.getLocale());
					if (ReturnCode.USER_NOT_FOUND.get().equals(tempResult.getResultCode())) {
						if (resultUnRegUsers == null)
							resultUnRegUsers = userItem;
						else
							resultUnRegUsers += ","+userItem;
					} else if (ReturnCode.SUCCESS.get().equals(tempResult.getResultCode())) {
						if (result==null) {
							result = tempResult;
						}
					} else if (ReturnCode.UPDATE_OK.get().equals(tempResult.getResultCode())) {
						result = tempResult;
					}
				} 
				if (result == null) {
					transactionManager.rollback(txStatus);
					
					result=new InviteResult();
					result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
					result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
					logger.error("inviteUser:"+reqRingInfo+":"+result);
					return result;
				} else {
					if (resultUnRegUsers != null){
						result=new InviteResult();
						result.setResultCode(ReturnCode.USER_NOT_ALL_FOUND.get());
						result.setResultMsg(resultUnRegUsers);
					}
				}
				
			}
			transactionManager.commit(txStatus);

		} catch (Exception e) {
			logger.error("Saving file failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("inviteUser:"+reqRingInfo+":"+result);
		return result;
	}

	InviteResult doInviteUser(String userNum, RingInfo ringInfo, String locale) throws Exception {
		InviteResult result = new InviteResult();
		UserInfo reqUserInfo = userInfoDAO.get(userNum);
		if (reqUserInfo == null) {
			result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
			result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			logger.error("doInviteUser:"+ringInfo+":"+result);
			return result;
		}
		ringInfo.setUserNum(userNum);
		RingInfo checkRingInfo = ringInfoDAO.get(ringInfo.getUserNum(), ringInfo.getCallingNum());
		if (checkRingInfo != null) {
			ringInfoDAO.update(ringInfo);
			result.setResultCode(ReturnCode.UPDATE_OK.get());
			result.setResultMsg(ReturnCode.STR_UPDATE_OK.get()+":"+ringInfo.getRingFileName());
		} else {
			ringInfoDAO.create(ringInfo); 
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get()+":"+ringInfo.getRingFileName());
		}
        
		JSONObject jData = new JSONObject();
        jData.put("actType", "ring");
        jData.put("userNum", ringInfo.getUserNum());
        jData.put("ringSrcUrl", ringInfo.getRingFileName());
        jData.put("callingNum", ringInfo.getCallingNum());
        jData.put("callingName", ringInfo.getCallingName());
        jData.put("locale", locale);
        jData.put("expiredDate", ringInfo.getExpiredDate());
        jData.put("durationType", ringInfo.getDurationType());
		gcmSender.call(jData, reqUserInfo.getUserId(), ringInfo.getCallingNum(),ringInfo.getCallingName(), locale);

		logger.info("doInviteUser:"+ringInfo+":"+result);
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
	@RequestMapping(value="/ringupdate", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RingUpdateResult ringUpdate(@RequestBody ReqRingUpdate ringUpdate) {
		logger.info("locale="+Locale.KOREA.getDisplayName());
		logger.info("locale2="+Locale.KOREA.toString());
		logger.info("ringupdate:"+ringUpdate);
		RingUpdateResult result =  new RingUpdateResult();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null; 
		
		try {
			
			//find ring info by user num.
			//set returnCode.
			UserInfo userInfo = userInfoDAO.get(ringUpdate.getUserNum());
			if (userInfo == null || !userInfo.getUserId().equals(ringUpdate.getUserId())) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.error("ringupdate:"+ringUpdate+":"+result);
				return result;
			}
			
			List<RingInfo> listRingInfo;
			listRingInfo = ringInfoDAO.getByUserNum(ringUpdate.getUserNum(), false);

			
			if (listRingInfo != null && listRingInfo.size() > 0) {
				txStatus = transactionManager.getTransaction(txDef);
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
				
				//delete ring info.
				//insert ring info to history.
				for(RingInfo item : listRingInfo) {
					result.setUpdateItem(item.getCallingNum(), item.getRingFileName(), item.getExpiredDate(), item.getDurationType());
					item.setDownload_cnt(item.getDownload_cnt()+1);
					ringInfoDAO.update(item);
					RingHistory ringHistory = new RingHistory(ringUpdate.getUserNum(),item.getCallingNum()
							,item.getCallingName(),item.getRegisterDate(), item.getExpiredDate(), ringUpdate.getUserId(), item.getRingFileName(), item.getDurationType());
					ringHistoryDAO.create(ringHistory);
				}
				transactionManager.commit(txStatus);
			} else {
				result.setResultCode(ReturnCode.RING_NO_UPDATE.get());
				result.setResultMsg(ReturnCode.STR_RING_NO_UPDATE.get());
			}

		} catch (Exception e) {
			logger.error("Ring info update failed.",e);
			if (txStatus != null) transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("ringupdate:"+ringUpdate+":"+result);
		return result;
	}
	
	/**
	 *5. 기존정보 재다운로드
	*/
	@RequestMapping(value="/ringcheckout", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RingUpdateResult ringCheckout(@RequestBody ReqRingUpdate ringUpdate) {
		
		logger.info("ringcheckout:"+ringUpdate);
		RingUpdateResult result =  new RingUpdateResult();
		try {
			//find ring info by user num.
			//set returnCode.
			UserInfo userInfo = userInfoDAO.get(ringUpdate.getUserNum());
			if (userInfo == null || !userInfo.getUserId().equals(ringUpdate.getUserId())) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.error("ringcheckout:"+ringUpdate+":"+result);
				return result;
			}
			
			List<RingInfo> listRingInfo;
			listRingInfo= ringInfoDAO.getByUserNum(ringUpdate.getUserNum(), true);

			if (listRingInfo != null && listRingInfo.size() > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());

				for(RingInfo item : listRingInfo) {
					result.setUpdateItem(item.getCallingNum(), item.getRingFileName(), item.getExpiredDate(), item.getDurationType());
				}
			}
//			Map<String, String> ringMap = new HashMap<String, String>();
//			ringMap.put("0255558888", "http://localhost:8080/ringcatcher/ring/aa.mp3");
//			result.setResultCode(ReturnCode.SUCESS.get());
//			result.setResultMsg(ReturnCode.STR_SUCESS.get());
//			result.setUpdateMap(ringMap);
		} catch (Exception e) {
			logger.error("Ring History checkout failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("ringcheckout:"+ringUpdate+":"+result);
		return result;	
	}
	

}
