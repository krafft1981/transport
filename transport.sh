#!/bin/bash

container=transport
image=transport

docker rm transport -f
docker rmi transport

./gradlew build

docker build . --tag=transport
docker run -d --name $container \
-v /home.dima/work/my:/dist \
--restart unless-stopped \
--net=host \
$image

docker logs transport -f
