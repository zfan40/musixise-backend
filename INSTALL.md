安装环境

查看原版本
rpm -qa | grep -E '^open[jre|jdk]|j[re|dk]'

清理老版本
yum remove java-1.6.0-openjdk
yum remove java-1.7.0-openjdk

下载地址
http://www.oracle.com/technetwork/java/javase/downloads/index.html

wget http://download.oracle.com/otn-pub/java/jdk/8u91-b14/jdk-8u91-linux-x64.rpm?AuthParam=1463316279_db665944d0a0d395e12f24f5c3de7e4e

执行安装
rpm -ivh jdk-8u25-linux-x64.rpm


安装持续集成工具

Installing Jenkins
https://wiki.jenkins-ci.org/display/JENKINS/Installing+Jenkins+on+RedHat+distributions

sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat/jenkins.repo
sudo rpm --import http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
sudo yum install jenkins

sudo service jenkins start

生产秘钥
sudo -u jenkins git ls-remote -h git@github.com:zfan40/musixise-backend.git HEAD

ip:8080 
curl -X POST http://JENKINS_URL/job/JOB_NAME/lastBuild/stop;
vim /etc/sysconfig/jenkins

# specify which version we want
export NODE_VERSION=4.3.1

# download
cd /tmp
wget http://nodejs.org/dist/v$NODE_VERSION/node-v4.3.1.tar.gz
tar xvfz node-v$NODE_VERSION.tar.gz

# build it and install it only locally
cd node-v$NODE_VERSION
./configure --prefix=/var/lib/jenkins/node-v$NODE_VERSION && make && make install

# Check versions of node and  npm
export PATH=/var/lib/jenkins/node-v$NODE_VERSION/bin:$PATH
node --version
# v4.3.1
npm --version
# 3.7.5

# install tools
npm install -g bower gulp
bower --version
# 1.7.7
gulp --version
# 3.9.1

git remote add origin https://git.oschina.net/wzhao/musixise.git
git remote set-url --add --push origin https://github.com/zfan40/musixise-backend.git
git push add --all
  

node 安装依赖

https://rpm.nodesource.com/pub_4.x/el/6/x86_64/
yum localinstall nodejs-4.2.6-2nodesource.el6.x86_64.rpm

mysql install

sudo yum install mysql-server
sudo /sbin/chkconfig --levels 235 mysqld on 

sudo service mysqld start
sudo mysql_secure_installation

create user &database
create database musixise;
grant all on musixise.* to 'musixise' identified by 'password';

git update
wget https://github.com/git/git/archive/v2.2.1.tar.gz
tar zxvf v2.2.1.tar.gz
cd git-2.2.1
make configure
./configure --prefix=/usr/local/git --with-iconv=/usr/local/libiconv
make all doc
make install install-doc install-html
echo "export PATH=$PATH:/usr/local/git/bin" >> /etc/bashrc
source /etc/bashrc

cp /usr/local/git/bin/git /usr/bin/

gcc update

curl -O ftp://ftp.gnu.org/gnu/gcc/gcc-4.8.2/gcc-4.8.2.tar.gz; tar zxf gcc-4.8.2.tar.gz
mkdir gcc-build-4.8.2
cd gcc-build-4.8.2
../configure --prefix=/usr
make && make install

install phantomjs
git clone git://github.com/ariya/phantomjs.git
cd phantomjs
git checkout 2.1.1
git submodule init
git submodule update

python build.py

or 

wget wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2



#### 自动启动
切换用户 sudo su jenkins -s /bin/bash
/var/lib/jenkins/spring-boot-jenkins/api-deploy.sh dev 8082 musixise-test application-localhost.yml

设置参数
java -jar jhipster-0.0.1-SNAPSHOT.war --spring.profiles.active=prod --server.port=9000

上传文件跨域问题
vim /etc/nginx/conf.d/virtual.conf
location / {
                proxy_set_header X-Forwarded-Host $host;
                proxy_set_header X-Forwarded-Server $host;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      dav_methods DELETE PUT;
      auth_basic "Restricted";
      auth_basic_user_file /etc/nginx/.htpasswd;

        if ($request_method = 'OPTIONS') {
                    add_header 'Access-Control-Allow-Origin' $http_origin;
                    #
                    # Om nom nom cookies
                    #
                    add_header 'Access-Control-Allow-Credentials' 'true';
                    add_header 'Access-Control-Allow-Methods' 'GET, DELETE, PUT, POST, OPTIONS';
                    #
                    #
                        # Custom headers and headers various browsers *should* be OK with but aren't
                        #
                        add_header 'Access-Control-Allow-Headers' 'Authorization,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
                        #
                        # Tell client that this pre-flight info is valid for 20 days
                        #
                        add_header 'Access-Control-Max-Age' 1728000;
                        add_header 'Content-Type' 'text/plain charset=UTF-8';
                        add_header 'Content-Length' 0;
                        return 204;
                }
                if ($request_method = 'POST') {
                        add_header 'Access-Control-Allow-Origin' '*';
                        add_header 'Access-Control-Allow-Credentials' 'true';
                        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
                        add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
                }
                if ($request_method = 'GET') {
                        add_header 'Access-Control-Allow-Origin' '*';
                        add_header 'Access-Control-Allow-Credentials' 'true';
                        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
                        add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
                }
    }


### jenkins 配置

#### Execute shell 01
 #!/bin/sh
 bower install

 ./mvnw -Pdev package -Dmaven.test.skip=true -X

#### Execute shell 02
 BUILD_ID=dontKillMe /var/lib/jenkins/spring-boot-jenkins/api-deploy.sh dev 8082 musixise-test application-localhost.yml