<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Meal Form</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>${param.action == 'edit' ?'Edit Meal' : 'Create Meal'}</h3>

<form action="meals" method="post" name="editMeal">
    <input type="hidden" name="id" value="${meal.id}">
    <label>
        DateTime : <input type="datetime-local"
                          style="width: 180px;"
                          id="dateTime"
                          name="dateTime"
                          value="${meal.dateTime}"
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
    <button type="button" class="btn btn-primary" onclick="window.history.back();">Cancel</button>
</form>
</body>
</html>