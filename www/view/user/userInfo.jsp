<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.Madeleine.Entity.User.USER_TYPE" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
		
		<script type="text/javascript" src="../../js/jquery-1.7.1.js"></script>
		<script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
		<script type="text/javascript" src="../../js/sprintf.js"></script>
		<script type="text/javascript" src="../../js/facebookAuth.js"></script>
		<script type="text/javascript">
			var isFBInited = false;
			function setFormWithFacebook(button){
				if(!isFBInited)
				{
					isFBInited = true;
					FBAuthInit("191785150891389",function(authResponse){
						FB.api("me",function(me){
							$("input[name='user.name']").val(me.name);
							$("input[name='user.email']").val(me.email);
						});
					});
				}
			}
		</script>
	</head>
	<body>
		<form action ="./editUserInfo.m" method="get">
			<ul>
				<li>아이디 : ${user.idx } </li>
				<li>이름	 : <input type="text" name = "user.name" value="${user.name}"/></li>
				<li>이메일 : <input type="text" name = "user.email" value="${user.email}"/></li>
				<li>외부계정타입 : ${user.userType}</li>
				<li>외부계정 : ${user.foreginUserId}</li>
				<li>등록된 마들렌 : ${fn:length(user.madeleineList)}</li>
			</ul>
			<input type="submit" value="수정"/>
		</form>
		<c:if test="${user.userType == 'FACEBOOK_USER'}">
			<button onclick="setFormWithFacebook(this)">페이스북 계정 정보로 리셋</button>
		</c:if>
	</body>
</html>