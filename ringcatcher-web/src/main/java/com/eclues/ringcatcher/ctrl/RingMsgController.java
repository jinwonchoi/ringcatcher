package com.eclues.ringcatcher.ctrl;

import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eclues.ringcatcher.MsgInfo;
import com.eclues.ringcatcher.ReqMessageInfo;
import com.eclues.ringcatcher.ReqRegisterMessage;
import com.eclues.ringcatcher.ReqUploadImage;
import com.eclues.ringcatcher.UserInfo;
import com.eclues.ringcatcher.dao.MsgHistoryDAO;
import com.eclues.ringcatcher.dao.MsgInfoDAO;
import com.eclues.ringcatcher.dao.UserInfoDAO;
import com.eclues.ringcatcher.etc.Constant;
import com.eclues.ringcatcher.etc.ReturnCode;
import com.eclues.ringcatcher.obj.MessageResult;
import com.eclues.ringcatcher.obj.RegisterResult;
import com.eclues.ringcatcher.obj.UploadImageResult;
import com.eclues.ringcatcher.service.FileService;
import com.eclues.ringcatcher.service.GcmSender;

@Controller
@RequestMapping("json")
public class RingMsgController {
	private static final Logger logger = LoggerFactory.getLogger(RingMsgController.class);

	@Autowired
	private MsgInfoDAO msgInfoDAO;
	
	@Autowired
	private UserInfoDAO userInfoDAO;
	
	//@Autowired
	//private MsgHistoryDAO msgHistoryDAO;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
		
	@Autowired
	private FileService fileService;

	@Autowired
	private GcmSender gcmSender;
	
	/**
	 * 1 작성한 메시지의 이미지파일을 하나씩 등록      
	 한건씩만 등록.
	path : json/imgUpload
	
	등록 정보 
	upfile: filename
	json:
	{"userNum":"010444448888"
	,"callingId":"asdfasdfo09asdfw23rfawefwaef"
	,"callingNum":"01066663333"
	,"callingName":"asdfasd"
	,"locale":"ko_KR"
	,"imgFileName":"234.mp3"
	} 
		String userNum;
	String callingId;
	String callingNum;
	String callingName;
	String locale;
	String imageFileName;
	내부적으로 파일명은 {callernum}_{rcv_num}_{random 자리}{.원래 확장자명}
	이미 등록된 내용/파일이 있으면 overwrite됨
	
	응답메시지
	* 정상등록: RES-A01
	  {"resultCode":"0001"
	  ,"resultMsg":"SUCCESS"
	      ,"fileUrl": "....."}
	
	* 전화상대방 등록정보없음: RES-A02 
	  {"resultCode":"1001"
	  ,"resultMsg":"Recepient not found"}
	
	* 유저 등록정보없음: RES-A03
	  {"resultCode":"4003"
	  ,"resultMsg":"Invalid User Error"}
	* 오류 발생: RES-A11   --> 재시도
	  {"resultCode":"4001"
	  ,"resultMsg":"Unknown Error"}
	 *       
	 */
	@RequestMapping(value="/uploadImage", method=RequestMethod.POST, consumes={"multipart/form-data"})
	public @ResponseBody UploadImageResult uploadImage(@RequestPart("json") ReqUploadImage reqUploadImage,@RequestPart("upfile") MultipartFile multipartFile) {
		
		logger.info("uploadImage:"+reqUploadImage);
		UploadImageResult result =  new UploadImageResult();
		
		try {
			UserInfo userInfo = userInfoDAO.get(reqUploadImage.getCallingNum());
			if (userInfo == null || !userInfo.getUserId().equals(reqUploadImage.getCallingId())) {
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				result.setFileUrl("");
				logger.info("uploadImage:"+reqUploadImage+":"+result);
				return result;
			}

			// save file
			String yyyymmdd = fileService.getYYYYMMDDPath();
			String uploadFileName = fileService.getSaveFileName(multipartFile, reqUploadImage.getCallingNum(), reqUploadImage.getUserNum());
			String savedFilePath = fileService.getSaveFilePath(yyyymmdd,uploadFileName);
			String savedFileUrl = fileService.getSaveFileUrl(yyyymmdd,uploadFileName);

			
			//ReqUserInfo userInfo = new ReqUserInfo();
			long fileSize = multipartFile.getSize();
			String fileName = multipartFile.getOriginalFilename();
			System.out.println("imagefileup:fileName="+fileName);
			System.out.println("imagefileup:fileSize="+fileSize);
			
			if (!fileService.saveFile(multipartFile,yyyymmdd,savedFilePath)) {
				result.setResultCode(ReturnCode.ERROR_FILEUP.get());
				result.setResultMsg(ReturnCode.STR_ERROR_FILEUP.get());
				result.setFileUrl("");
				logger.info("uploadImage:"+reqUploadImage+":"+result);
				return result;
			}			
			//find user by user num
			// set returnCode
			String[] userList = reqUploadImage.getUserNum().split(",");
			UserInfo reqUserInfo = userInfoDAO.get(userList[0]);
			if (reqUserInfo == null) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				result.setFileUrl("");
			} else {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
				result.setFileUrl(savedFileUrl);
			}

		} catch (Exception e) {
			logger.error("Saving file failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
			result.setFileUrl("");
		}
		logger.info("uploadImage:"+reqUploadImage+":"+result);
		return result;
	}

		
	/**
	 * 2. 메시지에 첨부한 이미지 파일을 올리고 올린 파일 경로명을 받는다.
	  path : /registerMessage
	  
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

	if userNum = 'default user'
		디폴트 메시지 처리
	else if userNum == 다수
		하나씩 등록여부 확인하고 메시지처리
		모두 미등록사용자인 경우 user not found처리
		일부 미등록사용자인 경우 user not all found처리 + 미등록번호 리턴
		모두 등록사용자인 경우   success or updated

	 *
	 */
	@RequestMapping(value="/registerMessage", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody RegisterResult registerMessage(@RequestBody ReqRegisterMessage reqRegisterMessage) {
		
		logger.info("registerMessage:"+reqRegisterMessage);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		RegisterResult result =  null;
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;

		
		try {

			txStatus =transactionManager.getTransaction(txDef);
			UserInfo userInfo = userInfoDAO.get(reqRegisterMessage.getCallingNum());
			if (userInfo == null || !userInfo.getUserId().equals(reqRegisterMessage.getCallingId())) {
				result=new RegisterResult();
				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
				logger.info("registerMessage:"+reqRegisterMessage+":"+result);
				return result;
			}

			String[] userList = reqRegisterMessage.getUserNum().split(",");
			String resultUnRegUsers = null;
			RegisterResult tempResult =  null;
			if (userList.length == 1) {
				result = doRegisterOneMessage(reqRegisterMessage.getUserNum(), reqRegisterMessage);
			} else {
				for (String userItem : userList) {
					tempResult = doRegisterOneMessage(userItem, reqRegisterMessage);
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
					
					result=new RegisterResult();
					result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
					result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
					logger.info("registerMessage:"+reqRegisterMessage+":"+result);
					return result;
				} else {
					if (resultUnRegUsers != null){
						result=new RegisterResult();
						result.setResultCode(ReturnCode.USER_NOT_ALL_FOUND.get());
						result.setResultMsg(resultUnRegUsers);
					}
				}
				
			}

			transactionManager.commit(txStatus);

		} catch (Exception e) {
			logger.error("Saving file failed.",e);
			transactionManager.rollback(txStatus);
			result=new RegisterResult();
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerMessage:"+reqRegisterMessage+":"+result);
		return result;
	}
	
	private RegisterResult doRegisterOneMessage(String userNum, ReqRegisterMessage reqRegisterMessage) throws Exception {
		RegisterResult result  =  new RegisterResult();
		
		//find user by user num
		// set returnCode
		UserInfo reqUserInfo = userInfoDAO.get(userNum);
		if (reqUserInfo == null) {
			result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
			result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			logger.info("registerMessage:"+reqRegisterMessage+":"+result);
			return result;
		}

		// save info
		MsgInfo msgInfo = new MsgInfo();
		msgInfo.setUserNum(userNum);
		msgInfo.setCallingNum(reqRegisterMessage.getCallingNum());
		msgInfo.setCallingName(reqRegisterMessage.getCallingName());
		String yyyymmdd = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
		msgInfo.setJsonMsg(reqRegisterMessage.getJsonMessage());
		msgInfo.setRegisterDate(yyyymmdd);
		msgInfo.setExpiredDate(reqRegisterMessage.getExpiredDate());
		MsgInfo checkMsgInfo = msgInfoDAO.get(userNum, reqRegisterMessage.getCallingNum());
		
		if (checkMsgInfo != null) {
			msgInfoDAO.update(msgInfo);
			result.setResultCode(ReturnCode.UPDATE_OK.get());
			result.setResultMsg(ReturnCode.STR_UPDATE_OK.get());
		} else {
			msgInfoDAO.create(msgInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		}
		if (Constant.DEFAULT_USER_NUM.get().equals(reqUserInfo.getUserNum())) {
			//don't send gcm for default message
		} else {
			//gcm 처리방식이 추후 결정되면 다시 적용하기로 
//            JSONObject jData = new JSONObject();
//            jData.put("actType", "message");
//            jData.put("userNum", reqRegisterMessage.getUserNum());
//            jData.put("jsonMessage", reqRegisterMessage.getJsonMessage());
//            jData.put("callingNum", reqRegisterMessage.getCallingNum());
//            jData.put("callingName", reqRegisterMessage.getCallingName());
//            jData.put("locale", reqRegisterMessage.getLocale());
//			gcmSender.callMessage(jData, reqUserInfo.getUserId(), reqRegisterMessage.getCallingNum()
//					,reqRegisterMessage.getCallingName(), reqRegisterMessage.getLocale());
		}
		
		return result;
		
	}
	
	@RequestMapping(value="/getmessage", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody MessageResult getMessage(@RequestBody ReqMessageInfo reqMessageInfo) {
		
		logger.info("getMessage:"+reqMessageInfo);
		MessageResult result =  new MessageResult();
		
		try {
//			UserInfo userInfo = userInfoDAO.get(reqMessageInfo.getCallingNum());
//			if (userInfo == null || !userInfo.getUserId().equals(reqMessageInfo.getCallingId())) {
//				result=new RegisterResult();
//				result.setResultCode(ReturnCode.ERROR_INVALID_USER.get());
//				result.setResultMsg(ReturnCode.STR_ERROR_INVALID_USER.get());
//				logger.info("getMessage:"+reqMessageInfo+":"+result);
//				return result;
//			}

			MsgInfo defaultMsgInfo = msgInfoDAO.get(Constant.DEFAULT_USER_NUM.get(), reqMessageInfo.getCallingNum());
			if (defaultMsgInfo!= null) {
				if (defaultMsgInfo.getDurationType()!=null)
					result.setDefaultDurationType(defaultMsgInfo.getDurationType());
				else
					result.setDefaultDurationType("");
				result.setDefaultJsonMessage(defaultMsgInfo.getJsonMsg());
				result.setDefaultExpiredDate(defaultMsgInfo.getExpiredDate());
			} else { 
				result.setDefaultDurationType("");
				result.setDefaultJsonMessage("");
				result.setDefaultExpiredDate("");
			}
				
			MsgInfo msgInfo = msgInfoDAO.get(reqMessageInfo.getUserNum(), reqMessageInfo.getCallingNum());
			if (msgInfo!= null) {
				if (msgInfo.getDurationType()!=null)
					result.setDurationType(msgInfo.getDurationType());
				else
					result.setDurationType("");
				result.setExpiredDate(msgInfo.getExpiredDate());
				result.setJsonMessage(msgInfo.getJsonMsg());
			} else {
				result.setDurationType("");
				result.setExpiredDate("");
				result.setJsonMessage("");
			}

			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//logger.info("getMessage:"+reqMessageInfo+":"+result);

		} catch (Exception e) {
			logger.error("query message failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getMessage:"+reqMessageInfo+":"+result);
		return result;
	}

}
