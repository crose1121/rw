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
<title>Stories by Category</title>
</head>
<body>
		<div class="container" style="width: 50%">
			<a class="homelink btn btn-secondary" href="/logout">Logout</a>
			<a class="homelink btn btn-info" href="/home">Home</a>
			<h1 class="text-secondary">${currentCategory.name} stories</h1>
			<table class="table table-secondary container">
				<thead>
					<tr>
						<th>Told By</th>
						<th>Title</th>
						<th>Other Related Categories</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="story" items="${categoryStories}">
						<tr>
							<td>${story.user.username}</td>
							<td ><a href="/story/${story.id}">${story.title}</a></td>
							<td>
								
							<!--	// use for loop? //  -->
								
								<c:forEach var="category" items="${story.categories}" varStatus="status">
								<!-- if the story.categories contains the current category, then dont show it -->
									<c:if test="${currentCategory!=category}">
									<c:if test="${status.last}">
										<a href="/stories/category/${category.id}">${category.name}</a>
									</c:if>
									<c:if test="${!status.last}">
										<a href="/stories/category/${category.id}">${category.name}</a>, 
									</c:if>
									</c:if>
								
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
</body>
</html>