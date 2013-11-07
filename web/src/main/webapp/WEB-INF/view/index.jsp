<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
		 <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
	</head> 
	<body>
		<script type="text/javascript">
			$(document).ready(function(){
			 $("#msgid").html("Damn, JQuery!");
			});
		</script>
		<div id="msgid"></div>
		<div id="message">hi: ${message}</div>
		<c:url value="/showMessage.html" var="messageUrl" />
		<a href="${messageUrl}">Click to enter</a>
		
		<form method="post">
			<input type="text" name="heatmap_name" id="heatmap_name" />
			<input type="submit" name="add_heatmap" id="id_heatmap" />			
		</form>
	</body>
</html>
