#!/bin/bash

user=krafft1981@mail.ru
pass=password
url="http://88.200.201.2:8080"

for file in $(ls images)
do

	$(cat images/$file | base64 > /tmp/$file)

	curl -X POST "$url/image" --digest --user "$user:$pass" -H "Content-Type: application/json" --data-binary "@/tmp/$file"
	echo

	rm /tmp/$file

#	curl -X POST "$url/transport?type=11" --digest --user "$user:$pass"
#	echo
done
