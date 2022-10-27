#!/usr/bin/env bash
redis = 'redis'
server = 'blog_server'
front = 'blog_front'
docker stop ${redis} ${server} ${front}
echo '----stop container----'
docker rm ${redis} ${server} ${front}
echo '----rm container----'
cd /usr/local/docker
docker-compose up
echo '----start container----'
