#!/bin/bash

user=bobby@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

curl -X GET "$url/order/calendar/transport?day=1618089681735&transport_id=2" --digest --user "$user:$pass"
