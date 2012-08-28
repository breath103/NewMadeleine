

function FBAuthInit(appID,callback)
{
	FB.init({ appId: appID, status: false, cookie: true, xfbml: true ,oauth:true});
	FB.getLoginStatus(function(response) {
		if (response.status == 'connected') {	
			callback(Object.freeze(response.authResponse));
		}
	    else {
	    	alert("Not Conneted");
	    	FB.login(function(response) {
	    	//	alert("LOIGN");
	    		console.log(response);
	    		$.get("/NewMadeleine/joinWithFacebookInfo.m",
	    				{facebook_id : response.authResponse.userID , access_token : response.authResponse.accessToken },
	    				function(response){
	    					console.log(response);
	    				},"json");
	    	}, {scope: 'email,read_stream,user_about_me,user_photos,publish_stream,read_friendlists,offline_access'});
	    	/*
	    	if (window.location.hash.length == 0) 
			{
		    	var path = 'https://www.facebook.com/dialog/oauth?';
			 	var queryParams = ['client_id=' + appID,
			 	                   'redirect_uri=' + window.location,
			 	                   'response_type=token',
			 	                   'scope=email,read_stream,user_about_me,user_photos,publish_stream,read_friendlists,offline_access'];
    		   	var query = queryParams.join('&');
    		   	var url = path + query;
    		   	location.href = url;
    		}
    		else{
    			var accessToken = window.location.hash.substring(1);
    		}
    		*/
	   	}
	});
}


var FBUtil = {
	fetchComment : function(photoId,callback){
		FB.api("/" + photoId + "/comments", function (response) {
			callback(response);
		});
	},
	fetchObject : function(fbId,callback){
		FB.api("/" + fbId, function (response) {
			callback(response);
		});
	}
};







