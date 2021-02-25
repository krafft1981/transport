#!/bin/bash

container=transport
image=transport

docker rm transport -f
docker rmi transport

docker build . --tag=transport
docker run -d --name $container \
--restart unless-stopped \
--net=host \
$image

docker logs transport -f
