<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<form action="loginTest" method="POST">
	ID : <input type="text" name="memberId"> <br>
	PW : <input type="password" name="memberPw"> <br>
	이름 : <input type="text" name="memberName"> <br>
	나이 : <input type="number" name="memberAge"> <br>
	<button type="submit">로그인</button>
</form>
</body>
</html>
