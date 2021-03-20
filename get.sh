#!/bin/bash

user=krafft1981@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

curl -X GET "$url/order/customer?page=0&size=100" --digest --user "$user:$pass" -H "accept: */*"	
