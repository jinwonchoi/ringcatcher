# ringcatcher



# ringcatcherTest
API를 기술함 


1 앱으로 가입
 등록정보 폰번호 
 path: /json/register
 { "user_id":"3453245234"    <== timestamp로 아이디를 생성
		  ,"user_num":"01044445555"
		  ,"user_email":"jinnonspot@gmail.com"
		  ,"recom_id":""
	  ,"overwrite":"true|false" (default : false)
	  }   
	  return OK
	  {"resultCode":"0001"
	  ,"regId":"23413241234123"
	  ,"resultMsg":"SUCCESS"}
	  return already registered
	  {"resultCode":"1002"
	  ,"regId":"23413241234123"
	  ,"resultMsg":"Already registered"}
	  return error
	  {"resultCode":"4001"
	  ,"regId":""
	  ,"resultMsg":"Unknown Error"}
	  
	    ** 체크사항>
	  - 추천자 있는지 확인--> 없으면 에러. 추천자 없이 등록하도록 유도
	  - 기등록여부 --> 기존 정보 유지할건지 물음. 기존 정보유지는 email인증 후.
	  - 등록정보 다운로드
	     -기존 정보 재 다운로드
		 -대기 정보 다운로드
	  
2. 폰북에서 친구초대
	  친구를 선택하고 각각 벨소리를 지정한다.
	  path : json/invite
	  
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
	  
3. 대기중인 링정보 체크 및 다운로드
	 path : json/ringupdate
	{ "user_id":"01042047792"
	, "user_num":"01042047792"}
	return OK
	{"updateMap":{"01066663333":"\/home\/jinnon\/file_repo\/\/20160402\/01066663333_010444448888.jpg"},"resultCode":"0001","resultMsg":"Sucess"}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}

5. 기존정보 재다운로드
	path : json/ringcheckout
			
	{"act_type":"ring_chekout"
	, "user_id":"231434123412341"
	, "user_num":"01042047792"}

	{"act_res":"OK"
	,["buddy_num":"01055556666","file_url":"http://afdsasd...."]}  
	  
