
function clearFilter(){
	window.location = moduleUrl
}

function showDeleteConfirmationModal(link, entityName){
	let entityId = link.attr("entityId")
	$("#confirmBody").text("Are you sure you want to delete "+entityName+" of ID " + entityId)
	$("#confirmYes").attr("href", link.attr("href"))
	$("#confirmDialog").modal()
}