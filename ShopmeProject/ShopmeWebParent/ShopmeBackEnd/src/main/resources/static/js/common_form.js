
$(document).ready(function(){
	$("#cancelButton").on("click", function(){
		window.location = homePageUrl
	})
	
	$("#fileImage").change(function(){
		let fileSize = this.files[0].size
		if(fileSize > 1048576){
			this.setCustomValidity("You must choose an image 1 MB or smaller.");
			this.reportValidity();
		}else{
			this.setCustomValidity("");
			showImageThumbnail(this);
		}
		
	})
	
})

function showImageThumbnail(fileInput){
	let file = fileInput.files[0]
	let fileReader = new FileReader()
	fileReader.onload = function(e){
		$("#thumbnail").attr("src", e.target.result)
	}
	fileReader.readAsDataURL(file);
}
