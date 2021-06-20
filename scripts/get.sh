#!/bin/bash

customer=krafft1981@mail.ru

driver1=driver1@mail.ru
driver2=driver2@mail.ru
driver3=driver3@mail.ru
driver4=driver4@mail.ru
driver5=driver5@mail.ru

account=maks00700@mail.ru
tester=veresk81@yandex.ru

p1=b
p2=1

#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

create="yes"

if [ ! -z $create ]
then

#    curl -X POST "$url/registration?account=$customer&password=b&phone=88006000500&fio=test"
#    curl -X POST "$url/registration?account=$driver1&password=b&phone=88004000500&fio=test"
#    curl -X POST "$url/registration?account=$driver2&password=b&phone=88004000500&fio=test"
#    curl -X POST "$url/registration?account=$driver3&password=b&phone=88004000500&fio=test"
#    curl -X POST "$url/registration?account=$driver4&password=b&phone=88004000500&fio=test"
#    curl -X POST "$url/registration?account=$driver5&password=b&phone=88004000500&fio=test"
#    curl -X POST "$url/registration?account=$account&password=1&phone=88004000500&fio=test"
    curl -X POST "$url/registration?account=$tester&password=b&phone=88004000500&fio=Sergey"

    curl -X POST "$url/parking" --digest --user "$driver1:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver2:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver3:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver4:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver5:$p1" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$account:$p2" -H "accept: */*"
else
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$p1"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$p1"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$account:$p2"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$account:$p2"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$account:$p2"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$account:$p2"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$account:$p2"
fi
