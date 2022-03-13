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

<title>New Story</title>
</head>
<body>
<div>
	<div>
		<h1 class="infotext" style="font-family: Copperplate, Papyrus, fantasy; font-size: 100px; margin-top: 10px; text-align: center;">Read it and Weep</h1>
	</div>
	<hr class="infotext"/>
	<div class="container" style="width: 50%">
		<a class="homelink btn btn-secondary" href="/logout">Logout</a>
		<a class="homelink btn btn-info" href="/home">Home</a>
		
		<form:form action="/story/create" method="post" modelAttribute="story">
		<div class="row">
			<div class="col-5">
				<h2 class="textlight" style="text-align: center">Tell us your Story</h2>
				<hr  class="textlight"/>
				<div class="form-group">
				<p>
					<form:label path="title"  class="textlight">Title</form:label>
					<form:input path="title" class="form-control"/>
					<br /><form:errors path="title" class="text-danger"/>
				</p>
				<p>
					<form:textarea class="textlight" placeholder="Tell your story here" path="details" name="" id="" cols="35" rows="10"></form:textarea>
					<br /><form:errors path="details" class="text-danger"/>
				</p>
				<input type="submit" value="Submit" class="btn btn-info"/>
				</div>
			</div>
			<div class="col mx-auto">
				<h2 style="text-align: center" class="textlight">Categories:</h2>
				<hr  class="textlight"/>
				<div>
				<c:forEach var="category" items="${allCategories}">
				<label to=""  class="infotext categorytag"> ${category.name}</label>
				<input type="checkbox" name="selectedCategories" value="${category.id}"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</c:forEach>
				<br /><form:errors path="categories" class="text-danger"/>
				</div>
				<hr class="textlight"/>
				<h3  class="textlight" style="text-align: center">Tag your friends in this story!</h3>
				<hr class="textlight"/>
						<c:forEach var="user" items="${currentlyFollowing}">
							<label to=""  class="infotext categorytag"> ${user.username}</label>
							<input type="checkbox" name="selectedUsers" value="${user.id}"/>
							&nbsp;&nbsp;&nbsp;
						</c:forEach>				
			</div>
		</div>
		
		</form:form>
	</div>
</div>
</body>
</html>