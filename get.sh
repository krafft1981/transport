#!/bin/bash

customer=krafft1981@mail.ru
driver=driver@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

curl -X POST "$url/registration?account=$customer&password=b&phone=88006000500&fio=test"
curl -X POST "$url/registration?account=$driver&password=b&phone=88004000500&fio=test"

curl -X POST "$url/parking" --digest --user "$driver:$pass" -H "accept: */*"
curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver:$pass"

curl -X POST "$url/order?day=1619395200000&start=1619431200000&stop=1619452800000&transport_id=1" --digest --user "$customer:$pass"
curl -X POST "$url/order/request/confirm?request_id=1" --digest --user "$driver:$pass"
