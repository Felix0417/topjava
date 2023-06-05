<%@ page contentType="text/html;charset=utf-8" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Create</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>Edit meal</h3>
<form action="meals" method="post" name="createMeal">
    <label>
        DateTime : <input type="datetime-local" name="dateTime" value="2023-05-25T08:30"/>
    </label>
    <br><br>
    <label>
        Description : <input type="text" name="description">
    </label>
    <br><br>
    <label>
        Calories : <input type="number" name="calories" value="0">
    </label>
    <br><br>
    <input type="submit" value="Save">
    <button><a href="meals?action=default" class="previous"></a>Cancel</button>
</form>
</body>
</html>
