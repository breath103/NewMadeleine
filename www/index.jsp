<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Madeleine</title>
		<link rel="SHORTCUT ICON" href="http://www.davesite.com/webstation/html/favicon.ico">
		<style>
			* {
				font-size: 1em;
				letter-spacing: -1px;
				font-family: NanumGothic,"Apple SD Gothic Neo","Malgun Gothic",AppleGothic,Dotum,sans-serif;
			}
		
			body{
				margin:0px 0px 0px 0px;
				background-color:#FDF1DC;
			}
			body .page-wrapper{
				margin-left:auto;
				margin-right:auto;
				
				width:100%;
				max-width  : 800px;
				
				height : 100%;
			}
			
			.page-wrapper .header{
				background-width : 100%;
				background-height: 100%;
				background-image : url("http://th02.deviantart.net/fs71/PRE/f/2010/004/d/9/Metal_Texture_by_Helpax.jpg");
				width:100%;
				height:50px;
				box-shadow: 0px 0px 10px white;
				text-align:center;		
			}
			.page-wrapper .header span{
				text-shadow: 0px 0px 5px #FFFFFF;
				width: 70%; 
				margin: 0 auto; 
				text-align: center; 
				color: white; 
				line-height: 50px; 
				font-weight: bold;
				font-size: 1.2em;
				text-overflow: ellipsis; 
				overflow: hidden; 
				white-space: nowrap;
			}
			
			.page-wrapper .article{
				position:relative;
				width:100%;
				height:700px;
				overflow:scroll;
				background-color:#FFB667;
			}
			.page-wrapper .article .section
			{
				width:100%;
				height:100%;
			}
			.page-wrapper .footer{
				background-color:white;	
				width:100%;
				height:50px;
			}
			
			
			.image-container{
				overflow:hidden;	
				width:100%;
				height:100%;
			}
			.image-container img{
			
			}
			
			.AlbumContainer{
				width:100%;
				height:80%;
				overflow:scroll;
			}
			.BowlContainer{
				width:100%;
				height:20%;
				background-color:red;	
			}
			
			.Photo{
				position:relative;
				display:inline-block;
				float:left;
				width:100px;
				height:100px;
				border-radius:10px;
				background-color:white;
				overflow:hidden;
				margin:2px 2px 2px 2px;
				padding : 5px 5px 5px 5px;
			}
			.Photo .image-container{
				height:100%;
			}
			.Album {	
				border : red solid 0px;
				width:100%;
				height:120px;
				display:inline-block;
				position:relative;
				overflow-x:scroll;
			}
		</style>
		<script type="text/javascript" src="./js/jquery-1.7.1.js"></script>
		<script type="text/javascript" src="./js/jquery-css-transform.js"></script>
		<script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
		<script type="text/javascript" src="./js/sprintf.js"></script>
		<script type="text/javascript" src="./js/facebookAuth.js"></script>
		<script type="text/javascript" src="./js/imageCrop.js"></script>
		<script type="text/javascript" language="javascript">
			var fs = sprintf;
		
			var photoControllerContainer = {};
			function Photo(albumController,fbPhoto)
			{
				this.albumController = albumController;
				this.fbPhoto = fbPhoto;
				
				photoControllerContainer[this.fbPhoto.id];
				
				FBUtil.fetchComment(fbPhoto.id,function(response){
					//console.log(response);
				});
				this.$div = null;
				this.imageLoadCallback = null;
			}
			Photo.prototype = {
				toTransportableJSON : function(){
					return {
						src 		: this.fbPhoto.source,
						description : this.fbPhoto.name,
						sourceType  : "FACEBOOK",
						sourceID 	: this.fbPhoto.id
					};
				},
				generateDivString : function(){
					return fs("<div class='Photo' albumid = '%s' photoid='%s'>"+
							  	"<div class='image-container'>"+
									"<img src='%s'/>"+
							  	"</div>"+
							  "</div>",
								this.albumController.fbAlbum.id,
								this.fbPhoto.id,
								this.fbPhoto.images[3].source);
				},
				getDiv : function(){
					return (this.$div) ? 
							this.$div :(this.$div = $(fs(".Photo[photoid='%s']",this.fbPhoto.id)));
				},
				isImageLoaded : function(){
					return this.get$image()[0].complete;
				},
				get$image : function(){
					return this.getDiv().find("img");
				},
				onImageLoaded : function(callback){
					this.imageLoadCallback = callback;
					
					if(this.isImageLoaded()){
						callback.call(this.get$image());
					}	
					else{
						this.get$image().load(callback);
					}
				}
			};
			
			var g_albums = {};
			function getAlbumController(id){
				return g_albums[id];	
			}
			function Album(fbAlbum)
			{
				this.fbAlbum = fbAlbum;
				this.photoControllers = {};
				this.$div = null;
				
				g_albums[this.fbAlbum.id] = this;
			}
			Album.prototype = {
				generateDivString : function(){
					return sprintf("<div class='Album' albumId='%s'></div>",this.fbAlbum.id);
				},
				getDiv : function(){
					if(this.$div) return this.$div;
					else return (this.$div = $(fs(".Album[albumId='%s']",this.fbAlbum.id)));
				},
				getPhotos : function(callback){
					var thisAlbum = this;
					FB.api("/" + this.fbAlbum.id + "/photos", function(response){
						callback.call(thisAlbum,response);
					});
				},
				getPhotoController : function(id){
					return this.photoControllers[id];
				},
				getCoverPhoto : function(callback){
					var thisAlbum = this;
					if(this.fbAlbum.cover_photo)
					{
						FB.api("/" + this.fbAlbum.cover_photo, function (response){
							callback.call(thisAlbum,response);
						});
					}
				},
				getFBData : function(){
					return this.fbAlbum;
				},
				createPhotoController : function(fbPhoto){
					var photo = new Photo(this,fbPhoto);
					this.photoControllers[fbPhoto.id] = photo;
					return photo;
				},
			};
			
			var selectedPhotos = [];
			function createMadeleine(){
				$.post("./madeleine/create.m",$.param({
					name   : "Test Madeleine",
					photos : JSON.stringify(selectedPhotos)
				}),function(response){
					console.log(response);
				},"json");
			}
			$(document).ready(function(){
				FBAuthInit("191785150891389",function(authResponse){
				//	$(".article").append("<div class='BowlContainer'></div>");
					FB.api("me",function(me){
						$.post("./joinWithFacebookInfo.m",{
							facebook_id  : authResponse.userID,
							access_token : authResponse.accessToken,
							name 		 : me.name,
							email 		 : me.email
						},function(response){
							console.log(response);
						},"json");
					});
					FB.api("me/albums",function(response){
						var albumArray = response.data;
						for(var index in albumArray)
						{
							var albumController = new Album(albumArray[index]);
							
							$(".AlbumContainer").append(albumController.generateDivString());
							
							albumController.getCoverPhoto(function(response){
							//	this.getDiv().css({"background-image" : "url("+response.source+")"});
							});
							albumController.getPhotos(function(response){
								var photoArray = response.data;
								for(var index in photoArray){
									var photo = this.createPhotoController(photoArray[index]);
									this.getDiv().append(photo.generateDivString());
									photo.onImageLoaded(function(){
							
									});
									photo.getDiv().click(function(){
										var albumController = getAlbumController($(this).attr("albumid"));
										var photoController = albumController.getPhotoController($(this).attr("photoid"));
										selectedPhotos.push(photoController.toTransportableJSON());
									});
									photo.getDiv().find(".image-container").cropImageCenter()
												  .resize(function(){
													  $(this).find(".image-container").cropImageCenter();
												   });
								}
							});
						}
					});
				});
			});
		</script>
	</head>
	<body>
		<div class="page-wrapper">
			<div class="header">
				<span>New Madeleine</span>
			</div>
			<div class="article">
				<div class='AlbumContainer'></div>
				<div class='BowlContainer'></div>
			</div>
			<div class="footer">
			</div>
		</div>
	</body>
</html>