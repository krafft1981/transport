#!/bin/bash

user=krafft1981@mail.ru
pass=b
#url="http://127.0.0.1:8080"
url="http://138.124.187.10:8080"

#find images -type f | while read file
for file in $(find images -type f)
do
	curl -X POST "$url/image" --digest --user "$user:$pass" -H "Content-Type: image/jpeg" --data-binary @$file
	if [ $? -eq 0 ]
	then
		rm "$file"
	fi
done
