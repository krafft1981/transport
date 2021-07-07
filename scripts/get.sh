#!/bin/bash

user=krafft1981@mail.ru
pass=1234567890

#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

create="yes"

if [ ! -z $create ]
then

    curl -X POST "$url/registration?account=$user&password=$pass&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/parking" --digest --user "$user:$pass" -H "accept: */*"

else
    curl -X POST "$url/transport?type=1" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=1" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=1" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=1" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=1" --digest --user "$customer1:$p1"
    curl -X POST "$url/transport?type=2" --digest --user "$customer1:$p1"
    curl -X POST "$url/transport?type=3" --digest --user "$customer1:$p1"
    curl -X POST "$url/transport?type=4" --digest --user "$customer1:$p1"

    curl -X POST "$url/transport?type=1" --digest --user "$customer2:$p2"
    curl -X POST "$url/transport?type=4" --digest --user "$customer2:$p2"

    curl -X POST "$url/transport?type=1" --digest --user "$customer3:$p1"
    curl -X POST "$url/transport?type=2" --digest --user "$customer3:$p1"
    curl -X POST "$url/transport?type=3" --digest --user "$customer3:$p1"
    curl -X POST "$url/transport?type=4" --digest --user "$customer3:$p1"
fi
