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
<meta charset="UTF-8">
<title>Read it and Weep</title>
  <!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" 
      rel="stylesheet" 
      integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" 
      crossorigin="anonymous">
<link rel="stylesheet" href="/style.css" />

</head>
<body>
	
<script>
function change_text1(){
    document.getElementById("aboutus").innerHTML = "Read it and Weep is a repository of anonymous life stories. Our community documents life's beautiful messiness through narrative storytelling. Do something embarassing lately? Come share it with us. Going through a breakup? We'd like to read about it. Looking for vicarious life experiences? Join us today and read/react to our stories.";
}
</script>


<div>
<div class="container">
        <h1 class="infotext" style="font-family: Copperplate, Papyrus, fantasy; font-size:100px; margin-top: 20px;">Read it and Weep</h1>
		<em><p class="textlight" style="text-indent: 35px;">The Internet's Collection of Anonymous Anecdotes</p></em>
		<br />
		<br />
        <h4 class="infotext" style="cursor:pointer; width: 14%; display: inline-block; text-decoration: underline;"  onclick="change_text1()">About Us</h4>
		<p id="aboutus" class="textlight"></p>
        <br />
        <br />
        
        <hr class="textlight"/>
        <div class="row">
            <div class="col">
                <h4 class="textlight">Register</h4>
                <hr class="textlight"/>
                <form:form action="/register" method="post" modelAttribute="newUser">
                    <div>
                        <form:label path="username" class="textlight" style="font-size: 25px;">Username</form:label>
                        <form:errors path="username" class="text-danger"/>
                        <form:input path="username" type="text" class= "form-control"/>
                    </div>
                    <div>
                        <form:label path="email" class="textlight" style="font-size: 25px;">Email</form:label>
                        <form:errors path="email" class="text-danger"/>
                        <form:input path="email" type="email" class= "form-control"/>
                    </div>
                    <div>
                        <form:label path="password" class="textlight" style="font-size: 25px;">Password</form:label>
                        <form:errors path="password" class="text-danger"/>
                        <form:input path="password" type="password" class= "form-control"/>
                    </div>
                    <div>
                        <form:label path="confirm" class="textlight" style="font-size: 25px;">Confirm Password</form:label>
                        <form:errors path="confirm" class="text-danger"/>
                        <form:input path="confirm" type="password" class= "form-control"/>
                        <em><p class="text-danger">By Registering, you agree to be respectful to your fellow community members</p></em>
                    </div>
                    
                    <input type="submit" value="Register" class= "btn submit mt-3"/>
                </form:form>   
            </div>
            <div class="col">
                <h4 class="textlight">Login</h4>
                <hr class="textlight"/>
                <form:form action="/login" method="post" modelAttribute="newLogin">

                    <div>
                        <form:label path="email" class="textlight" style="font-size: 25px;">Email</form:label>
                        <form:errors path="email" class="text-danger"/>
                        <form:input path="email" type="email" class= "form-control"/>
                    </div>
                    <div>
                        <form:label path="password" class="textlight" style="font-size: 25px;">Password</form:label>
                        <form:errors path="password" class="text-danger"/>
                        <form:input path="password" type="password" class= "form-control"/>
                    </div>
                    
                    <input type="submit" value="Login" class= "btn submit mt-3" style="float: right"/>
                </form:form>   
            </div>
        </div>
    </div>
</div>
</body>
</html>