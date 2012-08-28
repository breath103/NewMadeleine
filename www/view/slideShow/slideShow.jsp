<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:useBean id="jsonObject" class="net.sf.json.JSONObject"/>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${madeleine.name}</title>
		<style>
			body{
				background-image : url("http://ericnovak.com/wp-content/uploads/2010/09/Damask_Texture_by_mangion.jpg");
				background-size : 100% auto;
				width:100%;
				height:100%;
			}
			.article
			{
				width:100%;
				height:100%;
			}
			.slide-container{
				height:800px;
				width:1000px;
				margin-left	 : auto;
				margin-right : auto;
				overflow:hidden;
			}
			.CoverSlide{
				
			}
			.Photo{
				width:70%;
				height:50%;
			}
			.Photo .image-container{
				width:100%;
				height:70%;
				overflow:hidden;
			}
		</style>
		<script type="text/javascript" src="./js/jquery-1.7.1.js"></script>
		<script type="text/javascript" src="./js/jquery-css-transform.js"></script>
		<script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
		<script type="text/javascript" src="./js/sprintf.js"></script>
		<script type="text/javascript" src="./js/facebookAuth.js"></script>
		<script type="text/javascript" src="./js/imageCrop.js"></script>
		<script type="text/javascript" language="javascript">
			function LoadingManager(){
				this.allProcessCount = 0;
				this.completeProcessCount = 0;
			}	
			LoadingManager.prototype = {
				addProcess : function(){
					this.allProcessCount++;
				},
				onProcessComplete : function(){
					this.completeProcessCount++;
					if(this.allProcessCount == this.completeProcessCount)
						console.log(this.allProcessCount);
				}
			};
			var loadingManager = new LoadingManager();
		
		
		
			function R2Br(str){
				return str.split("\n").join("</br>");
			}
			
		
			function Photo(json){
				//this.jsonInfo = json;
				this.idx		 = json.idx;
				this.src 		 = json.src;
				this.description = json.description;
				this.sourceType  = json.sourceType;
				this.sourceID    = json.sourceID;
				
				this.additionalSourceInfo  = null;
				
				loadingManager.addProcess();
				this.startLoadingAdditionalSourceInfo();
			}
			Photo.prototype = {
				startLoadingAdditionalSourceInfo : function(){
					//여기에 다른 소스타입들의 경우. 미투데이나 등등등에 대한 처리를 넣는다.
					if(this.sourceType == "FACEBOOK"){
						(function(photo){
							FBUtil.fetchObject(photo.sourceID,function(response){
								photo.onAdditionalSourceInfoLoaded(response);
							});
						})(this);
					}
				},
				onAdditionalSourceInfoLoaded : function(sourceInfo){
					this.additionalSourceInfo = sourceInfo;
					$(".slide-container").append(this.generateDiv());
					
					this.getDiv().find(".image-container").cropImageCenter();
					
					loadingManager.onProcessComplete();
				},
				generateDiv : function(){
					var commentStr = "";
				
					//여기에 다른 소스타입들의 경우. 미투데이나 등등등에 대한 처리를 넣는다.
					if(this.sourceType == "FACEBOOK"){
						if(this.additionalSourceInfo.comments){
							for(var key in this.additionalSourceInfo.comments.data){
								var comment = this.additionalSourceInfo.comments.data[key];
								commentStr += fs("%s : %s</br>",comment.from.name,comment.message);
							}
						}
					}	
					
					
					return fs("<div class='Photo' photoid = '%s'>" + 
							  	"<div class='image-container'>"+
									"<img src = '%s'/>" + 
								"</div>" + 
								"%s</br>%s" + 
							 "</div>",this.idx,this.src,R2Br(this.description),R2Br(commentStr));
				},
				getDiv : function(){
					return $(fs("*[photoid=%s]",this.idx));
				}
			}
			function SlideShowController(){	
				//일단 서버에서 가져오는 부분을 이렇게 짠다. 가능하면 이 코드를 서버로 옮기고, 정리해야할것같다.
				/*
				this.madeleine = {
					createdTime  : new Date("${madeleine.createdTime}"),
					reservedTime : new Date("${madeleine.reservedTime}"),
					name 		 : "${madeleine.name}",
					sendState    : "${madeleine.sendState}",
					isPublic     : ${madeleine.isPublic}
				};
				*/
				this.madeleine = ${jsonMadeleine};
				this.photos = [];
				//this.photos = [];
				/*
				<c:forEach var="photo" items="${madeleine.photos}">
				this.photos.push({
					src 		: "${photo.src}",
					description : "${photo.description}",
					sourceType  : "${photo.sourceType}",
					sourceID	: "${photo.sourceID}"
				});
				</c:forEach>
				*/
				for(var index in this.madeleine.photos){
					var photo = this.madeleine.photos[index];
					this.photos.push(new Photo(photo));
				}
			}
			SlideShowController.prototype = {
				start : function(){
					
				},
				stop : function(){
					
				},
				next : function(){
					
				},
				previous : function(){
					
				}
			};
			var slideShowController;
			
			$(document).ready(function(){
				FBAuthInit("191785150891389",function(authResponse){
					slideShowController = new SlideShowController();
				});
			});
		</script>
	</head>
	<body>
		<audio controls="controls">
 			<source src="./resource/slideshow/sound/bg1.mp3" type="audio/mpeg" />
	 	 	Your browser does not support the audio element.
		</audio>
		
		<div class="article">
			<div class="slide-container">
			
			</div>
		</div>
	</body>
</html>