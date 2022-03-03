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
<script src="https://kit.fontawesome.com/54d4188d6f.js" crossorigin="anonymous"></script>
<title>Story Details</title>
</head>
<body>
	<div class="container" style="width: 50%">
		<a class="homelink btn btn-secondary" href="/logout">Logout</a>
		<a class="homelink btn btn-info" href="/home">Home</a>
		<h2 class="text-secondary">${story.title}</h2>
		<h3 class="text-secondary">Told by: <span style="color:purple">${story.user.username}</span> </h3>
		<hr />

		<div class="row">
			<div class="col-6">
				<p>${story.details}</p>
				<hr />
				<div>
				<c:if test="${!story.userLikes.contains(currentUser) }">
				<a href="/like/story/${story.id}" ></a>
				<i onclick="location.href = '/like/story/${story.id}';" class="fa-solid fa-heart likebutton"></i>
				</c:if>
				<c:if test="${story.userLikes.contains(currentUser) }">
				<i onclick="location.href = '/dislike/story/${story.id}';" class="fa-solid fa-heart-crack likebutton"></i>
				</c:if>
				${story.userLikes.size()}
				</div>
				<hr>
				<form:form action="/comment/create/${story.id}" method="post" modelAttribute="comment" style="width: 50%">
				<form:textarea placeholder="Leave a comment" path="details" name="" id="" cols="40" rows="2"></form:textarea>
				<br /><form:errors path="details" class="text-danger"/>
				<input type="submit" value="Submit" class="btn btn-info"/>
				</form:form>

				</div>
			<div class="col">
				<h4 class="text-secondary">Comments:</h4>
				<hr />
				<c:forEach var='comment' items='${currentStoryComments}'>
				<p>${comment.user.username}: ${comment.details}</p>
				</c:forEach>
			</div>

			
			
			<!--<c:forEach var='user' items='${story.userLikes}'>
				<p>${user.username}</p>
			</c:forEach>
			<!-- to implement a dislike button, use c:if tag to check if currentUser is present in story.userLikes -->
		</div>
	</div>
</body>
</html>