$(function () {
	// define the application
	var MyProjects = {};
	var pgtransition = 'slide';
	var seedword='myownproject';
	var urlStr = "http://192.168.229.129:8080/ringcatcher";
	console.log("slide");
	(function (appAdmin) {
		console.log("appAdmin main");
		
		var UserLi = '<li ><a href="#pgEditUser?userNum=Z2&callingNum=Z3"><h2>Z1</h2><p>DESCRIPTION</p></a></li>';
		var UserHdr = '<li data-role="list-divider">Your Users</li>';
		var noUser = '<li id="noUser">You have no users</li>';
		var pgUserListScroller = new IScroll('#pgUserList', {mouseWheel:true, scrollbars:true, bounce:true, zoom:false});
		
		var RingMessageLi = '<li ><a href="#pgEditRingMessage?RingMessageId=Z2"><h2>Z1</h2><p>DESCRIPTION</p></a></li>';
		var RingMessageHdr = '<li data-role="list-divider">Your RingMessages</li>';
		var noRingMessage = '<li id="noRingMessage">You have no projects</li>';
		var pgRingMessageListScroller = new IScroll('#pgRingMessageList', {mouseWheel:true, scrollbars:true, bounce:true, zoom:false});

		
		appAdmin.init = function () {
			FastClick.attach(document.body);
			console.log("appAdmin.init");
			appAdmin.SignInBindings();
			appAdmin.UserBindings();
			appAdmin.RingMessageBindings();
			
			$('#msgboxyes').on('click',function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				var yesmethod = $('#msgboxyes').data('method');
				var yesid = $('#msgboxyes').data('id');
				appAdmin[yesmethod](yesid);
			});
			$('#msgboxno').on('click',function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				var nomethod = $('#msgboxno').data('method');
				var noid = $('#msgboxno').data('id');
				var toPage = $('#msgboxno').data('topage');
				$.mobile.changePage('#'+toPage,{transition:pgtransition});
				//console.log("msgboxno.noid:"+noid);
				appAdmin[nomethod](noid);
			});
			$('#alertboxok').on('click', function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				var toPage = $('#alertboxok').data('topage');
				// show the page to display after ok is clicked
				$.mobile.changePage('#'+toPage, {transition:pgtransition});
			});
			
		
		};
		
		appAdmin.UserBindings = function () {
			console.log("UserBindings");
			// code to run before showing the page that lists the records.
			//run after the page has been displayed
			$(document).on('pagecontainershow', function (e, ui) {
				var pageId = $(':mobile-pagecontainer').pagecontainer('getActivePage').attr('id');
				console.log("pagecontainershow pageId="+pageId);
				switch (pageId) {
				}
			});
			//before records listing is shown, check for storage
			$(document).on('pagebeforechange', function (e, data) {
				//get page to go to
				var toPage = data.toPage[0].id;
				console.log("pagebeforechange toPage="+toPage);
				switch (toPage) {
					case 'pgUser':
					$('#pgRptUserBack').data('from', 'pgUser');
					// restart the storage check
					appAdmin.checkForUserStorage();
					break;
					case 'pgReports':
					$('#pgRptUserBack').data('from', 'pgReports');
					break;
					case 'pgRptUser':
					break;
					case 'pgEditUser':
					$('#pgRptUserBack').data('from', 'pgEditUser');
					break;
					case 'pgAddUser':
					$('#pgRptUserBack').data('from', 'pgAddUser');
					break;
					default:
				}
			});
			
			/************************************************
			 ************Listing Page ***********************
			 ************************************************/ 
			$(document).on('click', '#pgUserList a', function(e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				console.log("#pgUserList a.click");
				var href = $(this)[0].href.match(/\?.*$/)[0];
				console.log("href="+href);
				var UserNum=href.replace(/^\?UserNum=/,'');
				console.log("UserNum="+UserNum);
				//change page to edit page
				$.mobile.changePage('#pgEditUser',{transition: pgtransition});
				//read record from JSON and update screen.
				appAdmin.editUser(UserNum);
			
			});
			
			$('#pgUserBack').on('click', function(e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				$.mobile.changePage('#pgMenu', {transition:pgtransition});
				
			});
			
			$('#pgUserNew').on('click',function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				$('#pgAddUser').data('from','pgUser');
				//show the active and user type elements
				$('#pgAddUserheader h1').text('MyProjects > Add User');
				$('#pgAddUserMenu').show();
				$.mobile.changePage('#pgAddUser', {transition:pgtransition});
			});
			
			$('#pgAddUserBack').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				//which page are we coming from, if from sign in go back to it
				var pgFrom = $('#pgAddUser').data('from');
				switch (pgFrom) {
					case "pgSignIn":
					$.mobile.changePage('#pgSignIn', {transition: pgtransition});
					break;
					default:
					// go back to the records listing screen
					$.mobile.changePage('#pgUser', {transition: pgtransition});
				}
			});

			$('#pgAddUserSave').on('click', function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				
				var UserRec = pgAddUserGetRec();
				appAdmin.addUser(UserRec);
			});
			
			$('#pgEditUserBack').on('click', function(e){
				console.log('pgEditUserBack.click');
				e.preventDefault();
				e.stopImmediatePropagation();
				$.mobile.changePage('#pgUser', {transition:pgtransition});
			});
			
			$('#pgEditUserUpdate').on('click', function(e) {
				console.log('pgEditUserUpdate.click');
				e.preventDefault();
				e.stopImmediatePropagation();
				var UserRecNew;
				UserRecNew = pgEditUserGetRec();
				appAdmin.updateUser(UserRecNew);
			});
			
			$('#pgEditUserDelete').on('click', function(e){
				console.log('pgEditUserDelete.click');
				e.preventDefault();
				e.stopImmediatePropagation();
				var FullName = $('#pgEditUserUserName').val()+'('+ $('#pgEditUserUserNum').val()+')';
				var userNum = $('#pgEditUserUserNum').val();
				$('#msgboxheader h1').text('Confirm Delete');
				$('#msgboxtitle').text(FullName);
				$('#msgboxprompt').text('Are you sure to delete this user? This action cannot be undone.');
				$('#msgboxyes').data('method','deleteUser');
				$('#msgboxno').data('method','editUser');
				$('#msgboxyes').data('id',userNum);
				$('#msgboxno').data('id',userNum);
				$('#msgboxyes').data('topage','pgEditUser');
				$('#msgboxno').data('topage','pgEditUser');
				$.mobile.changePage('#msgbox',{transition:'pop'});
			});
			
		};
		
		appAdmin.checkForUserStorage = function () {
			console.log("appAdmin.checkForUserStorage");
			var UserObj = appAdmin.getRealUser();
			
			if (!$.isEmptyObject(UserObj)) {
				appAdmin.displayUser(UserObj);
			} else {
				$("#pgUserList").html(UserHdr + noUser).listview("refresh");
			}
		};
		
		appAdmin.getRealUser = function(search) {
			if (typeof search == 'undefined') {
				search = "";
			}
			console.log("appAdmin.getRealUser:"+search);
			
			//var reqSearch = {"userNum":"","userName":""};
			var reqSearch = {};
			var userList = {};
			reqSearch.userNum = search;
			reqSearch.userName = "";
			var recordJSON = JSON.stringify(reqSearch);
			console.log("recordJSON:"+recordJSON);
			var req = Ajax("/ringcatcher/admin/getuserlist","POST", recordJSON);
			if (req.status == 200) {
				var resultStr = req.responseText;
				console.log("resultStr:"+resultStr);
				var  resultObj = JSON.parse(req.responseText);
				var resultCode = resultObj.resultCode;
				var resultList = resultObj.list;
				console.log("resultCode="+resultObj.resultCode+" resultMsg="+resultObj.resultMsg);
				for (key in resultList) {
					console.log("item.userNum="+resultList[key].userNum);
					console.log("item.userId="+resultList[key].userId);
					console.log("item.userName="+resultList[key].userName);
					console.log("item.updateDate="+resultList[key].updateDate);
					console.log("item.createDate="+resultList[key].createDate);
					userList[resultList[key].userNum] = resultList[key];
				}
				var keys = Object.keys(userList);
				keys.sort();
				var sortedObj = Object();
				var j;
				for (j in keys) {
					key = keys[j];
					sortedObj[key] = userList[key];
				} 
				userList=sortedObj;
			} else {
				toastr.error('Failed in listing users. Please try again.', 'MyProjects');
			}
			return userList;
			//console.log();
		};
		
		appAdmin.displayUser = function (UserObj) {
			console.log("appAdmin.displayUser");

			var html = "";
			var userItem;//userNum
			var adminInfo = sessionStorage.getItem('adminInfo');
			console.log('sessionStorage.getItem:'+adminInfo);

			for (userItem in UserObj) {
				var UserRec = UserObj[userItem];
				var nItem = UserLi;
				
				nItem = nItem.replace(/Z2/g, UserRec.userNum);
				
				var nTitle = '';
				if (UserRec.userName == null) {
					console.log("UserRec.userName is Null");
					nTitle += "N/A";
				} else {
					console.log("UserRec.userName is Not Null");
					nTitle += UserRec.userName;
				}
				
				nItem = nItem.replace(/Z1/g, nTitle);
				var nDescription = "";
				nDescription = UserRec.userNum+"("+UserRec.userId+")";
				
				nItem = nItem.replace(/DESCRIPTION/g, nDescription);
				html +=  nItem;				
			}
			console.log(UserHdr + html);
			$('#pgUserList').html(UserHdr + html).listview("refresh");
		};
		
		function pgEditUserClear() {
			$('#pgEditUserUserNum').val('');
			$('#pgEditUserUserId').val('');
			$('#pgEditUserUserName').val('');
			$('#pgEditUserEmail').val('');
			$('#pgEditUserRecomId').val('');
			$('#pgEditUserUpdateDate').val('');
			$('#pgEditUserCreateDate').val('');
		}
		
		function pgEditUserGetRec() {
			var UserRec;
			UserRec = {};
			UserRec.userNum =  $('#pgEditUserUserNum').val().trim();
			UserRec.userId = $('#pgEditUserUserId').val().trim();
			UserRec.userName = $('#pgEditUserUserName').val().trim();
			UserRec.userEmail = $('#pgEditUserEmail').val().trim();
			UserRec.recomId = $('#pgEditUserRecomId').val().trim();
			UserRec.updateDate = $('#pgEditUserUpdateDate').val().trim();
			UserRec.createDate = $('#pgEditUserCreateDate').val().trim();
			return UserRec;
		}
		
		function pgAddUserClear() {
			$('#pgAddUserUserNum').val('');
			$('#pgAddUserUserId').val('');
			$('#pgAddUserUserName').val('');
			$('#pgAddUserEmail').val('');
			$('#pgAddUserRecomId').val('');
			//$('#pgAddUserUpdateDate').val('');
			//$('#pgAddUserCreateDate').val('');
		}
		
		function pgAddUserGetRec() {
			var UserRec;
			UserRec = {};
			UserRec.userNum =  $('#pgAddUserUserNum').val().trim();
			UserRec.userId = $('#pgAddUserUserId').val().trim();
			UserRec.userName = $('#pgAddUserUserName').val().trim();
			UserRec.userEmail = $('#pgAddUserEmail').val().trim();
			UserRec.recomId = $('#pgAddUserRecomId').val().trim();
			UserRec.updateDate = '';//$('#pgAddUserUpdateDate').val().trim();
			UserRec.createDate = '';//$('#pgAddUserCreateDate').val().trim();
			return UserRec;
		}
		
		function pgEditUserInitRec() {
			var UserRec;
			UserRec = {};
			UserRec.userNum =  "";
			UserRec.userId = "";
			UserRec.userName = "";
			UserRec.userEmail = "";
			UserRec.recomId = "";
			UserRec.updateDate = "";
			UserRec.createDate = "";
			return UserRec;
		}
		
		
		appAdmin.editUser = function(UserNum) {
			console.log("appAdmin.editUser:"+UserNum);
			var UserObj = appAdmin.getRealUser(UserNum);
			var UserRec = UserObj[UserNum];
			//console.log(UserObj[UserNum].userName);
			console.log(UserObj[UserNum].userId);
			console.log(UserObj[UserNum].userNum);

			console.log('appAdmin.editUser:'+UserRec.userNum);
			//set data-url of the page to the read record key.
			$('#pgEditUser').data('id', UserNum);
			//update each control in the Edit page
			$('#pgEditUserUserId').val(UserRec.userNum);
			$('#pgEditUserUserNum').val(UserRec.userNum);
			$('#pgEditUserUserId').val(UserRec.userId);
			$('#pgEditUserUserName').val(UserRec.userName);
			$('#pgEditUserEmail').val(UserRec.userEmail);
			$('#pgEditUserRecomId').val(UserRec.recomId);
			$('#pgEditUserUpdateDate').val(UserRec.updateDate);
			$('#pgEditUserCreateDate').val(UserRec.createDate);
			
		}; 
		
		appAdmin.deleteUser = function(UserNum) {
			console.log("appAdmin.deleteUser:"+UserNum);
			var UserRec = pgEditUserInitRec();
			UserRec.userNum = UserNum;
			var recordJSON = JSON.stringify(UserRec);
			var req = Ajax("/ringcatcher/admin/unregisteruser","POST", recordJSON);
			if (req.status == 200) {
				console.log("unregisteruser result:"+req.statusText);
				toastr.success('User record deleted.['+UserNum+']','MyProjects');
			} else {
				toastr.error('Failed in deleting user['+UserNum+']. Please try again.', 'MyProjects');
			}
			$.mobile.changePage('#pgUser',{transition:pgtransition});
		}
		
		appAdmin.updateUser = function(UserRec) {
			console.log("appAdmin.updateUser:"+UserRec.userNum);
			var UserNum = UserRec.userNum;
			var recordJSON = JSON.stringify(UserRec);
			var req = Ajax("/ringcatcher/admin/registeruser","POST", recordJSON);
			if (req.status == 200) {
				console.log("registeruser result:"+req.statusText);
				toastr.success('User record updated.['+UserNum+']','MyProjects');
			} else {
				toastr.error('Failed in updating user['+UserNum+']. Please try again.', 'MyProjects');
			}

			pgEditUserClear();
			$.mobile.changePage('#pgUser',{transition:pgtransition});
		}
		
		appAdmin.addUser = function(UserRec) {
			console.log("appAdmin.addUser:"+UserRec.userNum);
			var UserNum = UserRec.userNum;
			var recordJSON = JSON.stringify(UserRec);
			var req = Ajax("/ringcatcher/admin/registeruser","POST", recordJSON);
			if (req.status == 200) {
				console.log("registeruser result:"+req.statusText);
				toastr.success('User record added.['+UserNum+']','MyProjects');
				var pgFrm = $('#pgAddUser').data('from');
				switch(pgFrm) {
				case "pgSignIn":
					$.mobile.changePage('#pgSignIn',{transition:pgtransition});
					break;
				default:
					pgAddUserClear();
				}
			} else {
				toastr.error('Failed in adding user['+UserNum+']. Please try again.', 'MyProjects');
			}
		}
		
		appAdmin.SignInBindings = function () {
			console.log("SignInBindings");
			$("#pgSignIn").find('#pgSignInPhoneMode').hide();

			$('#rgSiginInEmail, #rgSiginInPhoneNum').on('change', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				// verify the user details
//				if ($("#rgSiginInEmail").is(":checked")) {
//					console.log("rgSiginInEmail");
//					$("#pgSignIn").find('#pgSignInPhoneMode').hide();
//					$("#pgSignIn").find('#pgSignInEmailMode').show();
//					$('#pgSignInPhoneNum').removeAttr('required');
//					$('#pgSignInEmail').attr('required', 'true');
//				} else if ($("#rgSiginInPhoneNum").is(":checked")) {
//					console.log("rgSiginInPhoneNum");
//					$("#pgSignIn").find('#pgSignInEmailMode').hide();
//					$("#pgSignIn").find('#pgSignInPhoneMode').show();
//					$('#pgSignInEmail').removeAttr('required');
//					$('#pgSignInPhoneNum').attr('required', 'true');
//				}
				
			});
			
			$("#pgSignInIn").on("click", function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				console.log("pgSignInIn.click");
				var userId = $('#pgSignInUserId').val();
				var passwd = $('#pgSignInPasswd').val();
//				if ($("#rgSiginInEmail").is(":checked")) {
//					authId = $('#pgSignInEmail').val().replace(/ /g,'-');
//					authIdType = 'E';
//					console.log("pgSiginInEmail==> authId:"+authId);
//				} else if ($("#rgSiginInPhoneNum").is(":checked")) {
//					authId = $('#pgSignInPhoneNum').val();
//					authIdType = 'P';
//					console.log("pgSignInPhoneNum==> authId:"+authId);
//				}
				console.log("userId="+userId+": passwd="+passwd);
				appAdmin.SignInUser(userId,passwd);
				//$.mobile.changePage('#pgMenu', {transition: pgtransition});
			}); 
			
			$('#pgChangePasswdUp').on('click', function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				console.log("pgChangePasswdUp.click");
				$('#pgChangePasswd').data('from','pgSignIn');
				$('#pgChangePasswdheader h1').text('MyProjects > Change Password');
				//$('#pgChangePasswdBack').data('from','pgSignIn');
				$('#pgChangePasswdLogin').removeAttr('readonly');
				$('#pgChangePasswdLogin').attr({title:'Enter curent login id.', placeholder:'Enter curent login id.'});
				console.log("title :"+$('#pgChangePasswdLogin').attr('title'));
				$.mobile.changePage('#pgChangePasswd','{transition:pgtransition}');
			});
			
			$('#pgChangePasswdIn').on('click',function(e){
				e.preventDefault();
				e.stopImmediatePropagation();
				console.log("pgChangePasswdIn.click");
				var userId = $('#pgChangePasswdLogin').val();
				var passwdPrev = $('#pgChangePasswdPrevPwd').val();
				var passwdNew = $('#pgChangePasswdNewPwd').val();
				var passwdConfirmed = $('#pgChangePasswdConfirmPwd').val();
				if (passwdNew != passwdConfirmed) {
					$('#alertboxheader h1').text('Password Error');
					$('#alertboxtitle').text("-");
					$('#alertboxprompt').text('The new password confirmation is incorrect!');
					$('#alertboxok').data('topage', 'pgChangePasswd');
					//$('#alertboxok').data('id', uname);
					$.mobile.changePage('#alertbox', {transition: 'pop'});
				}
				appAdmin.ChangePasswd(userId,passwdPrev, passwdNew);
			});

		};

		//user_id,auth_id, a.auth_id_type,a.user_name,a.user_passwd,a.description,a.update_date,a.create_date
		function pgSignInAdminInitRec() {
			var AdminRec;
			AdminRec = {};
			AdminRec.userId =  "";
			AdminRec.authId = "";
			AdminRec.authIdType = "";
			AdminRec.userName = "";
			AdminRec.userPasswd = "";
			AdminRec.description= "";
			AdminRec.updateDate = "";
			AdminRec.createDate = "";
			return AdminRec;
		}
		
		function pgSignInClear() {
			$('#pgSignInUserId').val('');
//			$("#rgSiginInEmail").attr('checked','true');
//			$('#pgSignInEmail').val('');
//			$('#pgSignInPhoneNum').val('');
			$('#pgSingInPasswd').val('');
		}
		
		function pgChangePasswdClear() {
			$('#pgChangePasswdLogin').val('');
			$('#pgChangePasswdPrevPwd').val('');
			$('#pgChangePasswdNewPwd').val('');
			$('#pgChangePasswdConfirmPwd').val('');
		}
		
		appAdmin.SignInUser = function(userId, passwd) {
			//$('pgSignIn').data('success','true');
			var AdminRec = pgSignInAdminInitRec();
			AdminRec.userId = userId;
			AdminRec.userPasswd = passwd;
			var recordJSON = JSON.stringify(AdminRec);
			console.log("recordJSON="+recordJSON);
			var req = Ajax("/ringcatcher/admin/authenticateadmin","POST", recordJSON);
			if (req.status == 200) {
				console.log("authenticateadmin result:"+req.statusText);
				console.log("authenticateadmin result:"+req.responseText);
				var  resultObj = JSON.parse(req.responseText);
				var resultCode = resultObj.resultCode;
				var resultItem = resultObj.item;
				if (('sessionStorage' in window) && window['sessionStorage'] != null) {
					console.log('sessionStorage.setItem:'+JSON.stringify(resultItem));
					sessionStorage.setItem('adminInfo', JSON.stringify(resultItem));
				}
				if (resultCode == "0001") {
					if (passwd == userId) 
						alertforChangePasswd(userId, "Your password is not secure.\nYou need to change it.");
					else 
						$.mobile.changePage('#pgMenu',{transition:pgtransition});
				} else if (resultCode == "4003") { //wrong passwd
					pgSignInClear();
					alertforSignIn('Admin id not found['+userId+']. Please try again.');
				} else if (resultCode == "4005") { //invalid userid
					pgSignInClear();
					alertforSignIn('Password mismatched. Please try again.');
				} else {
					pgSignInClear();
					alertforSignIn('Failed in login. Please try again.');
				}
			} else {
				pgSignInClear();
				alertforSignIn('Failed in login . Please try again.');
			}
		};
		
		function alertforSignIn(msg) {
			$('#alertboxheader h1').text('SignIn Error');
			$('#alertboxtitle').text("MyProjects");
			$('#alertboxprompt').text(msg);
			$('#alertboxok').data('topage', 'pgSignIn');
			//$('#alertboxok').data('id', uname);
			$.mobile.changePage('#alertbox', {transition: 'pop'});
		}

		function alertforChangePasswd(userid, msg) {
			$('#pgChangePasswdLogin').val(userid);
			$('#alertboxheader h1').text('Password Change');
			$('#alertboxtitle').text("MyProjects");
			$('#alertboxprompt').text(msg);
			$('#alertboxok').data('topage', 'pgChangePasswd');
			//$('#alertboxok').data('id', uname);
			$.mobile.changePage('#alertbox', {transition: 'pop'});
		}

		function errorAlertforChangePasswd(msg) {
			$('#pgChangePasswdLogin').val(userid);
			$('#alertboxheader h1').text('Password Change Error');
			$('#alertboxtitle').text("MyProjects");
			$('#alertboxprompt').text(msg);
			$('#alertboxok').data('topage', 'pgChangePasswd');
			//$('#alertboxok').data('id', uname);
			$.mobile.changePage('#alertbox', {transition: 'pop'});
		}

		appAdmin.ChangePasswd = function(userId, oldpasswd, newpasswd) {
			console.log("userId="+userId+": oldpasswd="+oldpasswd+": newpasswd="+newpasswd);
			var AdminRec = pgSignInAdminInitRec();
			AdminRec.userId = userId;
			AdminRec.userPasswd = oldpasswd;
			var recJsonAuth = JSON.stringify(AdminRec);
			var resultAdminInfo;
			var reqAuth = Ajax("/ringcatcher/admin/authenticateadmin","POST", recJsonAuth);
			if (reqAuth.status == 200) {
				console.log("authenticateadmin result:"+reqAuth.statusText);
				console.log("authenticateadmin result:"+reqAuth.statusText);
				var  resultObj = JSON.parse(reqAuth.responseText);
				var resultCode = resultObj.resultCode;
				resultAdminInfo = resultObj.item;

				if (resultCode == "0001") {
					//
				} else if (resultCode == "4003") { //wrong passwd
					pgChangePasswdClear();
					errorAlertforChangePasswd(userId, 'Password mismatched. Please try again.');
				} else if (resultCode == "4005") { //invalid userid
					pgChangePasswdClear();
					errorAlertforChangePasswd(userId, 'Admin id not found['+userId+']. Please try again.');
				} else {
					pgChangePasswdClear();
					errorAlertforChangePasswd(userId, 'Failed in Password Change. Please try again.');
				}
			} else {
				pgChangePasswdClear();
				errorAlertforChangePasswd(userId, 'Failed in Password Change . Please try again.');
			}
			console.log("JSON resultAdminInfo:"+JSON.stringify(resultAdminInfo));
			resultAdminInfo.userPasswd = newpasswd;
			var recJsonReg = JSON.stringify(resultAdminInfo);
			console.log("recJsonReg:"+recJsonReg);
			var reqReg = Ajax("/ringcatcher/admin/registeradmin","POST", recJsonReg);
			if (reqReg.status == 200) {
				console.log("registeradmin result:"+reqReg.statusText);
				var  resultObj = JSON.parse(reqReg.responseText);
				var resultCode = resultObj.resultCode;

				if (resultCode == "0001"||resultCode == "0002") {
					$.mobile.changePage('#pgSignIn', {transition: 'page'});
				} else {
					pgChangePasswdClear();
					errorAlertforChangePasswd(userId, 'Failed in Password Change. Please try again.');
				}
			} else {
				pgChangePasswdClear();
				errorAlertforChangePasswd(userId, 'Failed in Password Change . Please try again.');
			}
		};
		
		function decodeJSON(encodedJson){
			// Create Base64 Object
			var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}

			// Decode the String
			var decodedString = Base64.decode(encodedJson);
			console.log(decodedString);
			return decodedString;
		};
		
		appAdmin.RingMessageBindings = function() {
			// code to run before showing the page that lists the records.
			//run after the page has been displayed
			$(document).on('pagecontainershow', function (e, ui) {
			});
			//before records listing is shown, check for storage
			$(document).on('pagebeforechange', function (e, data) {
				//get page to go to
				var toPage = data.toPage[0].id;
				switch (toPage) {
					case 'pgRingMessage':
					// restart the storage check
					appAdmin.checkForRingMessageStorage();
					break;
					case 'pgEditRingMessage':
					// clear the add page form fields
					pgEditRingMessageClear();
					break;
					case 'pgAddRingMessage':
					// clear the add page form fields
					pgAddRingMessageClear();
					break;
					default:
				}
			});
			//***** Add Page *****
			// code to run when back button is clicked on the add record page.
			// Back click event from Add Page
			$('#pgAddRingMessageBack').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				//which page are we coming from, if from sign in go back to it
				var pgFrom = $('#pgAddRingMessage').data('from');
				switch (pgFrom) {
					case "pgSignIn":
					$.mobile.changePage('#pgSignIn', {transition: pgtransition});
					break;
					default:
					// go back to the records listing screen
					$.mobile.changePage('#pgRingMessage', {transition: pgtransition});
				}
			});
			// code to run when the Save button is clicked on Add page.
			// Save click event on Add page
			$('#pgAddRingMessageSave').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				// save the RingMessage
				var RingMessageRec;
				//get form contents into an object
				RingMessageRec = pgAddRingMessageGetRec();
				//save object to JSON
				appAdmin.addRingMessage(RingMessageRec);
			});
			// code to run when a get location button is clicked on the Add page.
			//***** Add Page - End *****
			//***** Listing Page *****
			// code to run when a listview item is clicked.
			//listview item click eventt.
			$(document).on('click', '#pgRingMessageList a', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				//get href of selected listview item and cleanse it
				var href = $(this)[0].href.match(/\?.*$/)[0];
				var tempUrl = href.replace(/^\?userNum=/,'').replace(/^\&callingNum=/,',');
				//change page to edit page.
				$.mobile.changePage('#pgEditRingMessage', {transition: pgtransition});
				//read record from JSON and update screen.
				appAdmin.editRingMessage(tempUrl); //[userNum],[callingNum]
			});
			// code to run when back button of record listing is clicked.
			// bind the back button of the records listing
			$('#pgRingMessageBack').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				// move to the defined previous page with the defined transition
				$.mobile.changePage('#pgMenu', {transition: pgtransition});
			});
			// code to run when New button on records listing is clicked.
			// New button click on records listing page
			$('#pgRingMessageNew').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				//we are accessing a new record from records listing
				$('#pgAddRingMessage').data('from', 'pgRingMessage');
				// show the active and user type elements
				$('#pgAddRingMessageheader h1').text('MyRingMessages > Add RingMessage');
				$('#pgAddRingMessageMenu').show();
				// move to the add page screen
				$.mobile.changePage('#pgAddRingMessage', {transition: pgtransition});
			});
			//***** Listing Page - End *****
			//***** Edit Page *****
			// code to run when the back button of the Edit Page is clicked.
			// Back click event on Edit page
			$('#pgEditRingMessageBack').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				// go back to the listing screen
				$.mobile.changePage('#pgRingMessage', {transition: pgtransition});
			});
			// code to run when the Update button is clicked in the Edit Page.
			// Update click event on Edit Page
			$('#pgEditRingMessageUpdate').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				// save the RingMessage
				var RingMessageRecNew;
				//get contents of Edit page controls
				RingMessageRecNew = pgEditRingMessageGetRec();
				//save updated records to JSON
				appAdmin.updateRingMessage(RingMessageRecNew);
			});
			// code to run when the Delete button is clicked in the Edit Page.
			// delete button on Edit Page
			$('#pgEditRingMessageDelete').on('click', function (e) {
				e.preventDefault();
				e.stopImmediatePropagation();
				//read the record key from form control
				var userNum = $('#pgEditRingMessageUserNum').val().trim();
				var callingNum  = $('#pgEditRingMessageCallingNum').val().trim();
				var callingName = $('#pgEditRingMessageCallingName').val().trim();
				var titleName = "message from "+callingNum+"("+callingName+") to "+userNum;
				var argValue = userNum+","+callingNum;
				//show a confirm message box
				$('#msgboxheader h1').text('Confirm Delete');
				$('#msgboxtitle').text(titleName);
				$('#msgboxprompt').text('Are you sure that you want to delete this message? This action cannot be undone.');
				$('#msgboxyes').data('method', 'deleteRingMessage');
				$('#msgboxno').data('method', 'editRingMessage');
				RingMessageName = RingMessageName.replace(/ /g, '-');
				$('#msgboxyes').data('id', argValue);
				$('#msgboxno').data('id', argValue);
				$('#msgboxyes').data('topage', 'pgEditRingMessage');
				$('#msgboxno').data('topage', 'pgEditRingMessage');
				$.mobile.changePage('#msgbox', {transition: 'pop'});
			});
			//***** Edit Page - End *****
			//Our events are now fully defined.
		};
		// this defines methods/procedures accessed by our events.
		// get existing records from JSON
		//get all existing records from JSON
		appAdmin.getRingMessage = function (argValue) {
			// get RingMessage records
			var ringMessageList = {};
			var userNum = "";
			var callingNum = "";
			
			if (typeof argValue != 'undefined') {
				var arArg=argValue.split(",");
				userNum = arArg[0];
				callingNum = arArg[1];
			}
			console.log("appAdmin.getRingMessage:"+argValue);
			
			//var reqSearch = {"userNum":"","userName":""};
			var reqSearch = {};
			reqSearch.userNum = userNum;
			reqSearch.callingNum = callingNum;
			var recordJSON = JSON.stringify(reqSearch);
			console.log("recordJSON:"+recordJSON);
			var req = Ajax("/ringcatcher/admin/getmsglist","POST", recordJSON);
			if (req.status == 200) {
				var resultStr = req.responseText;
				console.log("resultStr:"+resultStr);
				var  resultObj = JSON.parse(req.responseText);
				var resultCode = resultObj.resultCode;
				var resultList = resultObj.list;
				console.log("resultCode="+resultObj.resultCode+" resultMsg="+resultObj.resultMsg);
				for (key in resultList) {
					console.log("item.userNum="+resultList[key].userNum);
					console.log("item.callingNum="+resultList[key].callingNum);
					console.log("item.callingName="+resultList[key].callingName);
					console.log("item.updateDate="+resultList[key].updateDate);
					console.log("item.createDate="+resultList[key].createDate);
					ringMessageList[resultList[key].userNum+","+resultList[key].callingNum] = resultList[key];
				}
				var keys = Object.keys(ringMessageList);
				keys.sort();
				var sortedObj = Object();
				var j;
				for (j in keys) {
					key = keys[j];
					sortedObj[key] = ringMessageList[key];
				} 
				ringMessageList=sortedObj;
			} else {
				toastr.error('Failed in listing users. Please try again.', 'MyProjects');
			}
			return ringMessageList;
			//console.log();
		};

		// save the defined Add page object to JSON
		// add a new record to server storage.
		appAdmin.addRingMessage = function (RingMessageRec) {
			//convert record to json to write to server
			var recordJSON = JSON.stringify(RingMessageRec);
			// save the data to a server file, use the post method as it has 8MB minimum data limitation
			var req = Ajax("/ringcatcher/admin/registermsg","POST", recordJSON);
			if (req.status == 200) {
				console.log("registeruser result:"+req.statusText);
				//show a toast message that the record has been saved
				toastr.success('RingMessage record saved.', 'MyRingMessages');
				//find which page are we coming from, if from sign in go back to it
				var pgFrom = $('#pgAddRingMessage').data('from');
				switch (pgFrom) {
					case "pgSignIn":
					$.mobile.changePage('#pgSignIn', {transition: pgtransition});
					break;
					default:
					// clear the edit page form fields
					pgAddRingMessageClear();
					//stay in the same page to add more records
				}
				} else {
				//show a toast message that the record has not been saved
				toastr.error('RingMessage record not saved. Please try again.', 'MyRingMessages');
			}
		};
		// save the defined Edit page object to JSON
		// update an existing record and save to server.
		appAdmin.updateRingMessage = function (RingMessageRec) {
			//convert record to json to write to server
			var recordJSON = JSON.stringify(RingMessageRec);
			var req = Ajax("/ringcatcher/admin/registermsg","POST", recordJSON);
			if (req.status == 200) {
				//show a toast message that the record has been saved
				toastr.success('RingMessage record updated.', 'MyRingMessages');
				// clear the edit page form fields
				pgEditRingMessageClear();
				// show the records listing page.
				$.mobile.changePage('#pgRingMessage', {transition: pgtransition});
				} else {
				//show a toast message that the record has not been saved
				toastr.error('RingMessage record not updated. Please try again.', 'MyRingMessages');
			}
		};
		// delete record from JSON
		//delete a record from JSON using record key
		appAdmin.deleteRingMessage = function (argValue) {
			// get RingMessage records
			//var ringMessageObj = {};
			var userNum = "";
			var callingNum = "";
			
			if (typeof argValue != 'undefined') {
				var arArg=argValue.split(",");
				userNum = arArg[0];
				callingNum = arArg[1];
			}
			console.log("appAdmin.deleteRingMessage:"+argValue);
			var RingMessageRec = pgInitRingMessagerec();
			RingMessageRec.userNum = userNum;
			RingMessageRec.callingNum = callingNum;
			var recordJSON = JSON.stringify(RingMessageRec);
			var req = Ajax("/ringcatcher/admin/unregistermsg","POST", recordJSON);
			if (req.status == 200) {
				toastr.success('RingMessage record deleted.', 'MyRingMessages');
				} else {
				toastr.error('RingMessage record not deleted.', 'MyRingMessages');
			}
			// show the page to display after a record is deleted, this case listing page
			$.mobile.changePage('#pgRingMessage', {transition: pgtransition});
		};
		// display existing records in listview of Records listing.
		//***** List Page *****
		//display records in listview during runtime.
		appAdmin.displayRingMessage = function (RingMessageObj) {
			// create an empty string to contain html
			var html = '';
			// make sure your iterators are properly scoped
			var n;
			// loop over records and create a new list item for each
			//append the html to store the listitems.
			for (n in RingMessageObj) {
				//get the record details
				var RingMessageRec = RingMessageObj[n];
				var titleName = "message from "+RingMessageRec.callingNum+"("+RingMessageRec.callingName+") to "+RingMessageRec.userNum;
				//define a new line from what we have defined
				var nItem = RingMessageLi;
				//update the href to the key
				nItem = nItem.replace(/Z2/g,RingMessageRec.userNum);
				nItem = nItem.replace(/Z3/g,RingMessageRec.callingNum);
				//update the title to display, this might be multi fields
				var nTitle = '';
				//assign cleaned title
				nTitle = titleName;
				//replace the title;
				nItem = nItem.replace(/Z1/g,nTitle);
				//there is a description, update the list item
				var nDescription = '';
				nDescription += RingMessageRec.registerDate;
				nDescription += '~';
				nDescription += RingMessageRec.expiredDate;
				//replace the description;
				nItem = nItem.replace(/DESCRIPTION/g,nDescription);
				html += nItem;
			}
			//update the listview with the newly defined html structure.
			$('#pgRingMessageList').html(RingMessageHdr + html).listview('refresh');
		};
		// check JSON for Records. This initializes JSON if there are no records
		//display records if they exist or tell user no records exist.
		appAdmin.checkForRingMessageStorage = function () {
			//get records from JSON.
			var RingMessageObj = appAdmin.getRingMessage();
			// are there existing RingMessage records?
			if (!$.isEmptyObject(RingMessageObj)) {
				// yes there are. pass them off to be displayed
				appAdmin.displayRingMessage(RingMessageObj);
				} else {
				// nope, just show the placeholder
				$('#pgRingMessageList').html(RingMessageHdr + noRingMessage).listview('refresh');
			}
		};
		// ***** Edit Page *****
		// clear the contents of the Edit Page controls
		//clear the form controls for data entry
		function pgEditRingMessageClear() {
			$('#pgEditRingMessageUserNum').val('');
			$('#pgEditRingMessageCallingNum').val('');
			$('#pgEditRingMessageCallingName').val('');
			$('#pgEditRingMessageRegisterDate').val('');
			$('#pgEditRingMessageExpiredDate').val('');
			$('#pgEditRingMessageJsonMsg').val('');
			$('#pgEditRingMessageDurationType').val('');
			$('#pgEditRingMessageDurationType').selectmenu('refresh');
			//user_num,calling_num, calling_name, register_date, expired_date, json_msg, duration_type, download_cnt
		}
		// get the contents of the edit screen controls and store them in an object.
		//get the record to be saved and put it in a record array
		//read contents of each form input
		function pgEditRingMessageGetRec() {
			//define the new record
			var RingMessageRec
			RingMessageRec = {};
			RingMessageRec.userNum = $('#pgEditRingMessageUserNum').val().trim();
			RingMessageRec.callingNum = $('#pgEditRingMessageCallingNum').val().trim();
			RingMessageRec.callingName = $('#pgEditRingMessageCallingName').val().trim();
			RingMessageRec.registerDate = $('#pgEditRingMessageRegisterDate').val().trim();
			RingMessageRec.expiredDate = $('#pgEditRingMessageExpiredDate').val().trim();
			RingMessageRec.jsonMsg = $('#pgEditRingMessageJsonMsg').val().trim();
			RingMessageRec.durationType = $('#pgEditRingMessageDurationType').val().trim();
			return RingMessageRec;
		}
		// display content of selected record on Edit Page
		//read record from JSON and display it on edit page.
		appAdmin.editRingMessage = function (argValue) {
			// get RingMessage records.
			var RingMessageObj = appAdmin.getRingMessage(argValue);
			var RingMessageRec = RingMessageObj[argValue];
			//set data-url of the page to the read record key.
			$('#pgEditRingMessage').data('id', argValue);
			//update each control in the Edit page
			//clean the primary key
			var arArg=argValue.split(",");
			userNum = arArg[0];
			callingNum = arArg[1];

			$('#pgEditRingMessageUserNum').val(RingMessageRec.userNum);
			$('#pgEditRingMessageCallingNum').val(RingMessageRec.callingNum);
			$('#pgEditRingMessageCallingName').val(RingMessageRec.callingName);
			$('#pgEditRingMessageRegisterDate').val(RingMessageRec.registerDate);
			$('#pgEditRingMessageExpiredDate').val(RingMessageRec.expiredDate);
			$('#pgEditRingMessageJsonMsg').val(RingMessageRec.jsonMsg);
			$('#pgEditRingMessageDurationType').val(RingMessageRec.durationType);
			$('#pgEditRingMessageDurationType').selectmenu('refresh');
		};
		// ***** Add Page *****
		// get the contents of the add screen controls and store them in an object.
		//get the record to be saved and put it in a record array
		//read contents of each form input
		function pgAddRingMessageGetRec() {
			//define the new record
			var RingMessageRec
			RingMessageRec = {};
			RingMessageRec.userNum = $('#pgEditRingMessageUserNum').val().trim();
			RingMessageRec.callingNum = $('#pgEditRingMessageCallingNum').val().trim();
			RingMessageRec.callingName = $('#pgEditRingMessageCallingName').val().trim();
			RingMessageRec.registerDate = $('#pgEditRingMessageRegisterDate').val().trim();
			RingMessageRec.expiredDate = $('#pgEditRingMessageExpiredDate').val().trim();
			RingMessageRec.jsonMsg = $('#pgEditRingMessageJsonMsg').val().trim();
			RingMessageRec.durationType = $('#pgEditRingMessageDurationType').val().trim();
			return RingMessageRec;
		}
		// clear the contents of the Add page controls
		//clear the form controls for data entry
		function pgAddRingMessageClear() {
			$('#pgEditRingMessageUserNum').val('');
			$('#pgEditRingMessageCallingNum').val('');
			$('#pgEditRingMessageCallingName').val('');
			$('#pgEditRingMessageRegisterDate').val('');
			$('#pgEditRingMessageExpiredDate').val('');
			$('#pgEditRingMessageJsonMsg').val('');
			$('#pgEditRingMessageDurationType').val('');
			$('#pgEditRingMessageDurationType').selectmenu('refresh');
		}

		function pgInitRingMessagerec() {
			var RingMessageRec
			RingMessageRec = {};
			RingMessageRec.userNum = '';
			RingMessageRec.callingNum = '';
			RingMessageRec.callingName = '';
			RingMessageRec.registerDate = '';
			RingMessageRec.expiredDate = '';
			RingMessageRec.jsonMsg = '';
			RingMessageRec.durationType = '';
			return RingMessageRec;
		}

		//appAdmin
		
		
		//function pg
		appAdmin.init();

	})(MyProjects);
	
});
