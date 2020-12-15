#!/bin/bash

user=krafft1981@mail.ru
pass=password
url="http://138.124.187.10:8080"

users=$(curl -X GET "$url/customer/count" --digest --user "$user:$pass" -H "Content-Type: application/json" 2> /dev/null)

#for user in $(ls images)
#do

#	$(cat images/$file | base64 > /tmp/$file)

#	curl -X POST "$url/image" --digest --user "$user:$pass" -H "Content-Type: application/json" --data-binary "@/tmp/$file"
#	echo

#	rm images/$file
#	rm /tmp/$file

#	curl -X POST "$url/transport?type=11" --digest --user "$user:$pass"
#	echo
#done
