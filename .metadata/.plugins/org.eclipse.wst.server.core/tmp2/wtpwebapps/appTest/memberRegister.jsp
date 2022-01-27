<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form name="memberRegisterFrm" action="<%= ctxPath%>/beginSpring/memberRegister.action" method="POST">
		<input type="text" name="userid" placeholder="아이디" required>
		<input type="password" name="passwd" placeholder="비밀번호" required>
		<input type="text" name="name" placeholder="이름" required>
		<input type="email" name="email" placeholder="이메일" required>
		<input type="tel" name="tel" placeholder="-없이입력하세요" required>
		<input type="submit" value="가입하기">
		<input type="reset" value="취소하기">
	</form>
</body>
</html>