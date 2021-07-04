#!/bin/bash

customer1=krafft1981@mail.ru

driver1=driver1@mail.ru
driver2=driver2@mail.ru
driver3=driver3@mail.ru
driver4=driver4@mail.ru
driver5=driver5@mail.ru

customer2=maks00700@mail.ru
customer3=veresk81@yandex.ru
customer4=mask001@mail.ru

p1=b
p2=1

#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

create=""

if [ ! -z $create ]
then

    curl -X POST "$url/registration?account=$driver1&password=b&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$driver2&password=b&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$driver3&password=b&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$driver4&password=b&phone=88004000500&fio=fio&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$driver5&password=b&phone=88004000500&fio=fio&time_zone=Europe/Samara"

    curl -X POST "$url/registration?account=$customer1&password=1&phone=89084007388&fio=Dmitriy&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$customer2&password=1&phone=89272110102&fio=Maks&time_zone=Asia/Baku"
    curl -X POST "$url/registration?account=$customer3&password=b&phone=89063381655&fio=Sergey&time_zone=Europe/Samara"
    curl -X POST "$url/registration?account=$customer4&password=b&phone=89272110102&fio=Maks&time_zone=Asia/Baku"

    curl -X POST "$url/parking" --digest --user "$driver1:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver2:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver3:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver4:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver5:$p1" -H "accept: */*"

    curl -X POST "$url/parking" --digest --user "$customer1:$p2" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$customer2:$p2" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$customer3:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$customer4:$p1" -H "accept: */*"

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
