<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=utf-8" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="meals?action=create">Add Meal</a></h4>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${requestScope.mealsTo}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <c:set var="dateTime" value="${mealTo.dateTime}"/>
            <td>${fn:replace(dateTime,"T"," ")}</td>
            <td>${mealTo.description}</td>
            <td> ${mealTo.calories}</td>

            <td><a href="meals?action=edit&mealId=<c:out value="${mealTo.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${mealTo.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
