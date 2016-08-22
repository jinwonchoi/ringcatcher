if (window.File && window.FileReader && window.FormData) {
	var $inputField = $('#file');
	
	$(document).on('change', '#file', function(e){
		var file = e.target.files[0];
		console.log(e.target.files[0]);
		if (file) {
			console.log("HandlingFileInoput2");
			if (/^image\//i.test(file.type)) {
				readFile(file);
			} else {
				alert('Not a valid image!');
			}
		}
	});
} else {
	alert("File upload is not supported!");
}