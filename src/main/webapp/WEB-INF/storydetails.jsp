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
	<div>
	<div>
		<h1 class="infotext" style="font-family: Copperplate, Papyrus, fantasy; font-size: 100px; margin-top: 10px; text-align: center;">Read it and Weep</h1>
		<hr class="infotext"/>
	</div>
	<div class="container" style="width: 50%">
		<a class="homelink btn btn-secondary" href="/logout">Logout</a>
		<a class="homelink btn btn-info" href="/home">Home</a>
		<h1 class="textlight">${story.title}</h1>
		<h3 class="textlight">Told by: <span style="color:rgb(91, 192, 222)">${story.user.username}</span>&nbsp;&nbsp;&nbsp;&nbsp;
		Tagged Users:
		<c:forEach var="taggedUser" items="${story.usersTagged}">
			<span style="color:rgb(91, 192, 222)">#${taggedUser.username}</span>&nbsp;&nbsp;
		</c:forEach>
		</h3>
		<hr  class="textlight"/>


		<div class="row">
			<div class="col-6">
				<p class="textlight">${story.details}</p>
				<hr  class="textlight"/>
				<div>
				<c:if test="${!story.userLikes.contains(currentUser) }">
				<i onclick="location.href = '/like/story/${story.id}';" class="fa-solid fa-heart likebutton" style="font-size: 30px;"></i>
				</c:if>
				<c:if test="${story.userLikes.contains(currentUser) }">
				<i onclick="location.href = '/dislike/story/${story.id}';" class="fa-solid fa-heart" style="color:red; font-size: 30px;"></i>
				</c:if>
				
				<!-- Delete a story -->
				<c:if test="${story.user == currentUser}">
				<i onclick="location.href = '/story/delete/${story.id}';" class="fa-solid fa-trash-can-arrow-up" style="color:red; font-size: 30px; float:right;"></i>
				</c:if>
				
				<div>
				<c:if test="${story.userLikes.size()==0}">
					<p class="infotext">${story.userLikes.size()} Likes</p>
				</c:if>
				<c:if test="${story.userLikes.size()==1}">
					<p class="infotext"style="display:inline-block;">${story.userLikes.size()} Like</p>
				</c:if>
				<c:if test="${story.userLikes.size()>1}">
					<p class="infotext" style="display:inline-block;">${story.userLikes.size()} Likes</p>
				</c:if>
				
				<c:if test="${story.user == currentUser}">
				</c:if>
				</div>
				
				</div>
				<hr class="textlight">
				<form:form action="/comment/create/${story.id}" method="post" modelAttribute="comment" style="width: 50%">
				<form:textarea placeholder="Leave a comment" path="details" name="" id="" cols="40" rows="2" class="textlight"></form:textarea>
				<br /><form:errors path="details" class="text-danger"/>
				<input type="submit" value="Submit" class="btn btn-info"/>
				</form:form>

				</div>
			<div class="col">
				<h4 class="textlight">Comments:</h4>
				<hr class="textlight"/>
				
				<div class="row">
				
			
				<c:forEach var='comment' items='${currentStoryComments}'>
					<div class="col-sm-10">
				
						<p class="textlight" style="display: inline-block;">${comment.user.username}: <span style="color: 	rgb(91, 192, 222);">${comment.details}</span> </p>
						
						<c:if test="${comment.commentLikes.size()==0}">
						<p class="infotext">${comment.commentLikes.size()} Likes</p>
						</c:if>
						<c:if test="${comment.commentLikes.size()==1}">
						<p class="infotext">${comment.commentLikes.size()} Like</p>
						</c:if>
						<c:if test="${comment.commentLikes.size()>1}">
						<p class="infotext">${comment.commentLikes.size()} Likes</p>
						</c:if>
	
					
					</div>
					
					<div class="col-sm-2">
						<c:if test="${comment.user == currentUser }">
							<i onclick="location.href = '/comment/delete/${story.id}/${comment.id}';" class="fa-solid fa-delete-left" style=" margin-top: 10px; margin-right: 10px; color:red; font-size: 30px;"></i>
						</c:if>
						<c:if test="${!comment.commentLikes.contains(currentUser) }">
							<i onclick="location.href = '/like/comment/${story.id}/${comment.id}';" class="fa-solid fa-heart likebutton" style=" margin-top: 10px; font-size: 30px;"></i>
						</c:if>
						
						<c:if test="${comment.commentLikes.contains(currentUser) }">
							<i onclick="location.href = '/dislike/comment/${story.id}/${comment.id}';" class="fa-solid fa-heart" style=" margin-top: 10px; font-size: 30px; color:red;"></i>
						</c:if>
					</div>
					
					
				</c:forEach>
					</div>
			</div>
			</div>

		</div>
	</div>
</body>
</html>