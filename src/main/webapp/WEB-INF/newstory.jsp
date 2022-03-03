<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   <!-- c:out ; c:forEach ; c:if -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
   <!-- Formatting (like dates) -->
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
   <!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
   <!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" 
      rel="stylesheet" 
      integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" 
      crossorigin="anonymous">
<link rel="stylesheet" href="/style.css" />
<title>Dashboard</title>
</head>
<body>
	<div class="container" style="width: 50%">
		<a class="homelink btn btn-secondary" href="/logout">Logout</a>
		<a class="homelink btn btn-info" href="/home">Home</a>
		
		<form:form action="/story/create" method="post" modelAttribute="story">
		<hr />
		<div class="row">
			<div class="col-5">
				<h3>Tell us your Story</h3>
				<hr />
				<div class="form-group">
				<p>
					<form:label path="title">Title</form:label>
					<form:input path="title" class="form-control"/>
					<br /><form:errors path="title" class="text-danger"/>
				</p>
				<p>
					<form:textarea placeholder="Tell your story here" path="details" name="" id="" cols="41" rows="10"></form:textarea>
					<br /><form:errors path="details" class="text-danger"/>
				</p>
				<input type="submit" value="Submit" class="postlink"/>
				</div>
			</div>
			<div class="col mx-auto">
				<h3 style="text-align: center">Categories:</h3>
				<hr />
				<div>
				<c:forEach var="category" items="${allCategories}">
				<label to="">|| ${category.name}</label>
				<input type="checkbox" name="selected" value="${category.id}"/>
				</c:forEach>
				<br /><form:errors path="categories" class="text-danger"/>
				</div>
				<hr />
				<h3>Tag your friends in this post!</h3>
			</div>
		</div>
		
		</form:form>
	</div>
</body>
</html>