Deploy backend
==============

## Setup docker
   * [link](https://docs.docker.com/install/linux/docker-ce/ubuntu/)  for setup on ubuntu.
## Download necessary repos
   * This repo
   * Discuz repo: [link](https://gitee.com/ComsenzDiscuz/DiscuzX)

## Build local php-apache with mysql plugin
The docker at: `${repo_root}/bbuhot_legency/php_docker_build`
```bash
docker build bbuhot_legency/php_docker_build --tag bbuhot-php:7.0
```

## Start mysql
Ref：[docker mysql doc](https://github.com/docker-library/docs/tree/master/mysql)
```bash
docker run --name bbuhot-mysql \ 
  -e MYSQL_ROOT_PASSWORD=bbuhot \
  -e MYSQL_ROOT_HOST=% \
  -p 3306:3306 -d \
  mysql:5.7
```
* Keep in mind that above script will expose mysql to the wild, so don't save sensitive data on it.
* The root password in this case is: `bbuhot`.
* This will use docker volume for storage, for other options, see the docker mysql doc.

## Start php
Ref：[docker php doc](https://github.com/docker-library/docs/tree/master/php)
```bash
docker run --name bbuhot-php \
  --network host \
  -v ${THE_DISCUZ_UPLOAD_FOLDER}:/var/www/html \
  -d bbuhot-php:7.0
```
Modify the `${THE_DISCUZ_UPLOAD_FOLDER}` to the fold you populate DISCUZ to.
Then browser to `http://{SERVER_IP_ADDRESS}/install` to install discuz.

## Start the server in the repo
Copy `bbuhot_server/configuration.default.json` to `bbuhot_server/configuration.json` and update the config.
Then with gradle:
```
bash gradlew :bbuhot_server:run
```
The server will be available at http://locahost:8080
