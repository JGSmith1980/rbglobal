# Sample curl commands

## Create New Customer
curl --location 'http://localhost:8080/api/customers' \
--header 'Authorization: static_token' \
--header 'Content-Type: application/json' \
--data-raw '{
"firstName": "Davey",
"lastName": "Davison",
"email": "dave@davison.com"
}'

## Get all customers
curl --location --request GET 'http://localhost:8080/api/customers' \
--header 'Authorization: static_token' \
--header 'Content-Type: application/json' \
--data '    '

## Get specific customer
curl --location --request GET 'http://localhost:8080/api/customers/4' \
--header 'Authorization: static_token' \
--header 'Content-Type: application/json' \
--data '    '

## Update customer
curl --location --request PUT 'http://localhost:8080/api/customers/4' \
--header 'Authorization: static_token' \
--header 'Content-Type: application/json' \
--data-raw '{
"id":4,
"firstName": "Daveyy",
"lastName": "Davison",
"email": "dave@davison.com"
}'