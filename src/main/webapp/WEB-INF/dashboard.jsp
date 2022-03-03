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
<style>
	background-color: grey;
</style>
<title>Dashboard</title>
</head>
<body>
	<div class="container" style="width: 50%">
		<a class="homelink btn btn-secondary" href="/logout">Logout</a>
		<h1 class="text-secondary">Welcome, ${loggedInUser.username}</h1>
		<hr />
		<h3 class="text-secondary">Here's our most recent stories.</h3>
			<table class="table table-secondary container">
				<thead>
					<tr>
						<th>Told By</th>
						<th>Title</th>
						<th>Related Categories</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="story" items="${allStories}">
						<tr>
							<td>${story.user.username}</td>
							<td ><a href="/story/${story.id}">${story.title}</a></td>
							<td>
								
							<!--	// use for loop? //  -->
								
								<c:forEach var="category" items="${story.getCategories()}" varStatus="status">
										<c:if test="${status.last}">
										<a href="/stories/category/${category.id}">${category.name}</a>
									</c:if>
									<c:if test="${!status.last}">
										<a href="/stories/category/${category.id}">${category.name}</a>, 
									</c:if>
									 
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		<a href="/story/new">Tell a story</a>
		<hr />
		<h3 class="text-secondary">Browse by Category</h3>

		<hr />
		<c:forEach var="category" items="${allCategories}">
			|| <a href="/stories/category/${category.id}">${category.name}</a>
		</c:forEach>
	</div>
</body>
</html>