$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        if($('#sidebar').hasClass("active")){
        	//console.log("show");
        	$('#sidebarCollapse span').html("Show Sidebar");
        }else{
        	//console.log("Hide");
        	$('#sidebarCollapse span').html("Hide Sidebar");
        }
    });
    /*$('#general').on('click', function(){
        console.log('after');
        $('#tables').load('');
        
    });*/
    $('#services').on('click', function(){
        console.log('after');
        $('#tables').load('/Admin/adminServices');
    });
    $('#accounts').on('click', function(){
        console.log('after');
        $('#tables').load('/Admin/adminAccounts');
        
    });
    $('#requests').on('click', function(){
        console.log('after');
        $('#tables').load('/Admin/adminRequests');
        
    });
    $('#tests').on('click', function(){
        console.log('after');
        $('#tables').load('/Admin/adminTests');
        
    });
    /*$('#reports').on('click', function(){
        console.log('after');
        $('#tables').load('report-table');
        
    });*/
    
});



