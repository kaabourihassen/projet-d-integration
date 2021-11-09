$(document).ready(function () {
	
	$('#saveAndContinueBtn').on('click', function () {
		//console.log("action before : " + $("[name='editProfileForm']").attr('action'));
		$("[name='editProfileForm']").attr('action' , "/profile/saveAndChangePassword?userId="+ $('#userId').val());
		//console.log("action after : " + $("[name='editProfileForm']").attr('action'));
		$("[name='editProfileForm']").submit();
	});
	
});