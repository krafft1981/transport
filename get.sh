#!/bin/bash

customer=krafft1981@mail.ru

driver1=driver1@mail.ru
driver2=driver2@mail.ru
driver3=driver3@mail.ru
driver4=driver4@mail.ru
driver5=driver5@mail.ru

pass=b

url="http://127.0.0.1:8080"
#url="http://138.124.187.10:8080"

create=""

if [ ! -z $create ]
then

    curl -X POST "$url/registration?account=$customer&password=b&phone=88006000500&fio=test"

    curl -X POST "$url/registration?account=$driver1&password=b&phone=88004000500&fio=test"
    curl -X POST "$url/registration?account=$driver2&password=b&phone=88004000500&fio=test"
    curl -X POST "$url/registration?account=$driver3&password=b&phone=88004000500&fio=test"
    curl -X POST "$url/registration?account=$driver4&password=b&phone=88004000500&fio=test"
    curl -X POST "$url/registration?account=$driver5&password=b&phone=88004000500&fio=test"

    curl -X POST "$url/parking" --digest --user "$driver1:$pass" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver2:$pass" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver3:$pass" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver4:$pass" -H "accept: */*"
    curl -X POST "$url/parking" --digest --user "$driver5:$pass" -H "accept: */*"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$pass"
else

#    curl -X POST "$url/order?day=1617235200000&start=1617271200000&stop=1617285600000&transport_id=1" --digest --user "$customer:$pass"
#    curl -X POST "$url/order/request/confirm?request_id=1" --digest --user "$driver2:$pass"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$pass"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$pass"

    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver1:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver2:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver3:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver4:$pass"
    curl -X POST "$url/transport?type=%D0%AF%D1%85%D1%82%D0%B0" --digest --user "$driver5:$pass"
fi
