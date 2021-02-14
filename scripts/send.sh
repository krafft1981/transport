#!/bin/bash

user=krafft1981@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

for file in $(ls images)
do

	$(cat images/$file | base64 > /tmp/$file)

	curl -X POST "$url/image" --digest --user "$user:$pass" -H "Content-Type: application/json" --data-binary "@/tmp/$file"
	echo

	rm images/$file
	rm /tmp/$file
done
