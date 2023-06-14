<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <br>

    <hr/>
    <h2>Meals</h2>
    <form method="get" action="meals">
        <input type="hidden" name="action" value="filter">
        <label for="startDate">От даты (включая)</label>
        <input type="date" id="startDate" name="startDate" onchange="updateDate()">

        <label for="startTime">От времени (включая)</label>
        <input type="time" id="startTime" name="startTime" onchange="updateTime()">

        <br><br>
        <label for="endDate">До даты (включая)</label>
        <input type="date" id="endDate" name="endDate" onchange="updateDate()">

        <label for="endTime">До времени (включая)</label>
        <input type="time" id="endTime" name="endTime" onchange="updateTime()">
        <br><br>
        <button type="submit">Отфильтровать</button>
    </form>
    <br><br>
    <a href="meals?action=create">Add Meal</a>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
<script>
    function updateDate() {
        var startDateInput = document.getElementById("startDate");
        var endDateInput = document.getElementById("endDate");
        endDateInput.min = startDateInput.value;
        startDateInput.max = endDateInput.value;
    }

    function updateTime() {
        var startTimeInput = document.getElementById("startTime");
        var endTimeInput = document.getElementById("endTime");
        endTimeInput.min = startTimeInput.value;
        startTimeInput.max = endTimeInput.value;
    }
</script>
</html>