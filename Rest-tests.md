<h2>Get all meals</h2>
curl --location --request GET 'http://localhost:8080/topjava/rest/meals' \
--data-raw ''

<h2>Get meal by id</h2>
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100004'

<h2>Create new meal</h2>
curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data-raw '{
"dateTime": "2023-07-23T00:51",
"description": "Обед",
"calories": 1001
}'

<h2>Update meal</h2>
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100004' \
--header 'Content-Type: application/json' \
--data-raw '{
"dateTime": "2023-07-25T00:51",
"description": "Обед",
"calories": 1001
}'

<h2>Delete meal by id</h2>
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100005'

<h2>Get meals filtered by date and time</h2>
curl --location --request
GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=00:01&endDate=2020-02-01&endTime=23:59'

<h2>Get user with it meals</h2>
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/100000/with-meals'