<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=utf-8" %>
<jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <c:set var="dateTime" value="${mealTo.dateTime}"/>
            <td>${fn:replace(dateTime,"T"," ")}</td>
            <td>${mealTo.description}</td>
            <td> ${mealTo.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
