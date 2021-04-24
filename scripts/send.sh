#!/bin/bash

user=krafft1981@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

find images -type f | while read file
do
	temp=$(mktemp)
	base64 "$file" > $temp

	curl -X POST "$url/image" --digest --user "$user:$pass" -H "Content-Type: application/json" --data-binary "@$temp"
	if [ $? -eq 0 ]
	then
		rm "$file"
	fi

	rm $temp
done
