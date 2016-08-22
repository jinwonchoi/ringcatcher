function readFile(file) {
	var reader = new FileReader();

	console.log("readfile");
	reader.onload = function(e) {
		$("#uploadimage").attr("src",e.target.result);
		$("#uploadimage").attr("width",$(window).width());

	}
	reader.onloadend = function () {
		//processFile(reader.result, file.type);
		console.log("reader.result:"+reader.result);
		sendFile(reader.result);
	}

	reader.onerror = function () {
		alert('There was an error reading the file!');
	}

	reader.readAsDataURL(file);
}