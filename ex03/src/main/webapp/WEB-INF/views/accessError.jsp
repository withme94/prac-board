<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page import ="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content= "text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>Access Denied Page</h1>

<h1><c:out value = "${SPRING_SECURITY_403_EXCEPTION.getMessage()}"/></h1>
<h1><c:out value = "${msg}"/></h1>
</body>
</html>