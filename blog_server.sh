#!/bin/bash
ip="watermelon02.cn"
password=$(sed -n '1p' password.txt)
#上传
expect -c "
set timeout 500
spawn scp /Users/xigua/Desktop/server/blog/target/blog-0.0.1-SNAPSHOT.jar root@${ip}:/usr/local/docker/blog_server/blog_server.jar
expect {
\"*password:\"  {send $password\r}
}
expect eof
"
#远程部署
expect -c "
spawn ssh  root@$ip
expect {
\"*password:\"  {send $password\r}
}
expect \"#*\"
send \"cd /usr/local/docker/blog_server\r\"
send \"docker build -t watermelon02/blog_server .\r\"
send \"cd ..\r\"
send \"docker-compose up -d\r\"
send \"exit\r\"
interact
"
