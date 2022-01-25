<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 성공</title>
</head>
<body>
	<h1>${loginMember.memberName}님이 로그인 하셨습니다.</h1>
	<ul>
		<li>ID : ${loginMember.memberId }</li>
		<li>PW : ${loginMember.memberPw }</li>
		<li>NM : ${loginMember.memberName }</li>
	</ul>
</body>
</html>