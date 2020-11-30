#!/bin/bash
AUTHOR=xiyoulinux
DOCKER_VERSION=lastest
DOCKER_TAG=${AUTHOR}/qq-bot:${DOCKER_VERSION}

readonly framework_dir=framework
workdir=$(pwd)
cd ../${framework_dir} || exit
echo 'build framework dependency ...'
mvn clean package -Dmaven.test.skip=true
echo 'build framework dependency ok'
echo ''

cd "${workdir}" || exit
echo 'build qqbot ...'
mvn clean package -Dmaven.test.skip=true
echo 'build qqbot ok'
echo ''

if [ $# -gt 0 ]
then
    case $1 in 
    docker)
        echo 'build docker image...'
        docker build -t ${DOCKER_TAG} .
        echo 'build docker image ok'
        echo ''
        echo -e "you can run use: \e[1mdocker run -itd --rm --name qqBot ${DOCKER_TAG}\e[21m run this image"
        ;;
    esac
fi