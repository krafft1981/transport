#!/bin/bash

user=krafft1981@mail.ru
pass=12345Q

#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

create=""

if [ ! -z $create ]
then

    curl -X POST "$url/registration?account=$user&password=$pass&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/parking" --digest --user "$user:$pass" -H "accept: */*"

else
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
    curl -X POST "$url/transport?type=1" --digest --user "$user:$pass"
fi
