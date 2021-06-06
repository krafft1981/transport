#!/bin/bash

user=driver1@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

curl -X GET "$url/order/customer" --digest --user "$user:$pass"
