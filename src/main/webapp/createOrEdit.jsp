<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Edit</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>${param.action == 'edit' ?'Edit Meal' : 'Create Meal'}</h3>

<c:if test="${not empty param.get('meal')}">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
</c:if>
<c:set var="isPresent" value="${not empty meal}"/>

<form action="meals" method="post" name="editMeal">
    <input type="hidden" name="id" value="${isPresent ? meal.id : -1}">
    <label>
        DateTime : <input type="datetime-local"
                          style="width: 180px;"
                          id="dateTime"
                          name="dateTime"
                          value="${isPresent ? meal.dateTime : " "}"
                          required/>
    </label>
    <br><br>
    <label>
        Description : <input type="text"
                             style="width: 180px;"
                             name="description"
                             value="${meal.description}" required>
    </label>
    <br><br>
    <label>
        Calories : <input type="number"
                          style="width: 180px;"
                          name="calories"
                          value="${meal.calories}" required>
    </label>
    <br><br>
    <button type="submit">Save</button>
    <button><a href="meals" onclick="window.history.back()"></a>Cancel</button>
</form>
</body>
<script>
    if (document.getElementById("dateTime").value === '') {
        document.getElementById("dateTime").value = new Date().toISOString().slice(0, 16);
    }
</script>
</html>