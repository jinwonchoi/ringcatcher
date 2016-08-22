function sendFile(fileData) {
	var formData = new FormData(fileData);

	var tokenId = "test-token-id";
	var jsonStr = "	{\"userNum\":\"01055557777,01064668888,55557777\""
		+",\"callingId\":\""+tokenId+"\""
		+",\"callingNum\":\"0244445555\""
		+",\"callingName\":\"Mememe\""
		+",\"locale\":\"ko_KR\""//en_US, ko_KR
		+",\"expiredDate\":\"99991231\""
		+",\"durationType\":\"\""
		+",\"ringFileName\":\"00044.jpg\"}";
	//formData.append('file', $('input[type="file"]')[0]);
	//formData.append('json', jsonStr);
	console.log("sendFile  fileData:"+fileData);
	console.log ("sendFile  jsonStr:"+jsonStr);

	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/ringcatcher/json/invite',
		data: formData,
		contentType: false,
		processData: false,
		success: function (data) {
			if (data.success) {
				alert('Your file was successfully uploaded!');
			} else {
				console.log("data.error"+data);
				alert('There was an error uploading your file!');
			}
		},
		error: function (data) {
			console.log("request error"+data);
			alert('There was an error uploading your file!');
		}
	});
}