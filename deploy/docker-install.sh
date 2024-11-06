#!/bin/bash

BASEDIR=$(cd "$(dirname "$0")" && pwd)
cd "${BASEDIR}" || exit

yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine

yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
yum -y install docker-ce docker-ce-cli containerd.io
systemctl enable docker
systemctl start docker
docker version
docker run hello-world

# add registry-mirrors
mkdir -p /etc/docker
mkdir /home/docker_root
tee /etc/docker/daemon.json <<-'EOF'
{
  "data-root": "/home/docker_root",
  "registry-mirrors": ["https://ed47r0z0.mirror.aliyuncs.com", "https://mirror.ccs.tencentyun.com", "https://registry.docker-cn.com", "https://hub.docker.com"]
}
EOF
systemctl enable docker
systemctl restart docker

# test docker
docker version
docker run hello-world

# install docker compose
unameS=$(uname -s | tr '[:upper:]' '[:lower:]')
unameM=$(uname -m | tr '[:upper:]' '[:lower:]')
curl -L "https://github.com/docker/compose/releases/download/v2.23.0/docker-compose-${unameS}-${unameM}" -o /usr/local/bin/docker-compose
cp "docker-compose-${unameS}-${unameM}" /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose version

# install tools
yum -y install git net-tools dos2unix sshpass zip unzip


# docker login
docker login ccr.ccs.tencentyun.com --username=100011806527
