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
<title>Stories by User</title>
</head>
<body>
<div>
	<div>
		<h1 class="infotext" style="font-family: Copperplate, Papyrus, fantasy; font-size: 100px; margin-top: 10px; text-align: center;">Read it and Weep</h1>
		<hr class="infotext"/>
	</div>
		<div class="container" style="width: 50%">
			<a class="homelink btn btn-secondary" href="/logout">Logout</a>
			
			<c:if test="${loggedInUser != selectedUser}">
				
			<c:if test="${loggedInUser.following.contains(selectedUser)}">
				<a class="homelink btn btn-danger" href="/unfollow/${selectedUser.id}">Unfollow</a>
			</c:if>
			
			<c:if test="${!loggedInUser.following.contains(selectedUser)}">
				<a class="homelink btn btn-info" href="/follow/${selectedUser.id}">Follow</a>
			</c:if>
				
			</c:if>
			
			
			
			<a class="homelink btn btn-light" href="/home">Home</a>
			<h1 class="textlight">Stories By <span style="color: rgb(91, 192, 222);">${selectedUser.username}</span></h1>
			<hr class="textlight"/>
			<br />
			<table class="table container">
				<thead>
					<tr class="tablelight">
						<th>Title</th>
						<th>Tagged Users</th>
						<th>Related Categories</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="story" items="${selectedUser.stories}">
						<tr class="tablelight">
							<td ><a href="/story/${story.id}" style="color: black">${story.title}</a></td>
								<td>
								<c:forEach var="taggedUser" items="${story.getUsersTagged()}" varStatus="status">
									<c:if test="${status.last}">
									<a href="/user/${taggedUser.id}" style="color: black">${taggedUser.username}</a>
									</c:if>
									<c:if test="${!status.last}">
									<a href="/user/${taggedUser.id}" style="color: black">${taggedUser.username},</a>&nbsp;
									</c:if>
								</c:forEach>
							</td>
							<td>
								<c:forEach var="category" items="${story.categories}" varStatus="status">
									<c:if test="${status.last}">
										<a href="/stories/category/${category.id}" style="color: black">${category.name}</a>
									</c:if>
									<c:if test="${!status.last}">
										<a href="/stories/category/${category.id}" style="color: black">${category.name}</a>, 
									</c:if>
								
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br />
		<br />
		<h3 class="textlight">Browse by Category</h3>

		<hr  class="textlight"/>
		<c:forEach var="category" items="${allCategories}">
			<a href="/stories/category/${category.id}"  class="a:hover categorytag infotext">${category.name}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</c:forEach>
		<br /><br /><br /><br />
		<div class="row">
			<div class="col-6">
				<h3 class="textlight">Users You Follow <span style="color: rgb(91, 192, 222)">(${currentlyFollowing.size()})</span></h3>
					<hr class="textlight"/>	
						<c:forEach var="user" items="${currentlyFollowing}">
							<a href="/user/${user.id}"  class="categorytag infotext" style="display: inline-block">${user.username}</a>&nbsp;&nbsp;&nbsp;
						</c:forEach>
			</div>
			<div class="col">
				<h3 class="textlight">Explore User Stories</h3>
					<hr class="textlight"/>
					<c:forEach var="user" items="${allUsers}">
						<c:if test="${user.id != loggedInUser.id}">
						<c:if test="${!currentlyFollowing.contains(user)}">
							<a href="/user/${user.id}"  class="a:hover infotext categorytag">${user.username}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</c:if>
						</c:if>
					</c:forEach>
		
			</div>
		</div>
		</div>
</div>
</body>
</html>