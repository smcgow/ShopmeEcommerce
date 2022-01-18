

$(document).ready(function(){
	
	$("#logoutLink").on("click", function(e){
		e.preventDefault();
		$("#logoutForm").submit()
	})
	
	customizeDropDown()
	
	function customizeDropDown(){
		
		//Make the logout div slide up and down on hover.
		$(".navbar .dropdown").hover(
			function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown()
			},
			function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideUp()
			}
		)
		
		
		//Make the name link go to the account
		$("#dropDownLink").on("click", function(e){
			window.location = this.href;
		})
	}
	
});