upstream xwing.devops {
    server 172.17.177.201;
    server 172.17.177.202;
    server 172.17.177.203;
    server 172.17.177.201:8080;
    server 172.17.177.202:8080;
    server 172.17.177.203:8080;
}

server {
    listen  80;
    listen  8080;
    server_name {{ nginx.servername }};

    location / {
        proxy_set_header  Host             $host;
        proxy_set_header  X-Real-IP        $remote_addr;
        proxy_set_header  X-Forwarded-For  $remote_addr;
        proxy_pass http://xwing.devops;
    }
}
