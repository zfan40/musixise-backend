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

jenkins plugin
textFinder plugin

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
```
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
```

### jenkins 配置

#### Execute shell 01
 #!/bin/sh
 bower install

 ./mvnw -Pdev package -Dmaven.test.skip=true -X

#### Execute shell 02
 BUILD_ID=dontKillMe /var/lib/jenkins/spring-boot-jenkins/api-deploy.sh dev 8082 musixise-test application-localhost.yml
 
 
 ### 微信回调调试
 本机执行
 ```
 #ssh -NfR 9999:localhost:8082 root@101.200.212.87
 ```
 浏览器访问 wx.musixise.com
 
 2.00gqZxZB06HrJQ4f93f164af0_9UZs
 
 
 org.springframework.social.connect.web.completeConnection
 
 
 
### 服务器
SWAP 分区设置
阿里云服务器默认关闭交换分区,为了减少因内存不足导致的CRASH ,开启SWAP.
创建用于交换分区的文件
dd if=/dev/zero of=/mnt/swap bs=block_size count=number_of_block
注：block_size、number_of_block 大小可以自定义，比如bs=1M count=1024 代表设置1G大小swap分区

设置交换分区文件
mkswap /mnt/swap

立即启用交换分区文件
swapon /mnt/swap
如果在/etc/rc.local中有swapoff -a 需要修改为swapon -a 

设置开机时自启用swap分区

需要修改文件/etc/fstab中的swap行。

添加 /mnt/swap swap swap defaults 0 0


查看内核参数vm.swappiness中的数值是否为0，如果为0则根据实际需要调整成30或者60
cat /proc/sys/vm/swappiness   
sysctl -a | grep swappiness    
sysctl -w vm.swappiness=60
注：若想永久修改，则编辑/etc/sysctl.conf文件
(内核参数中有一个vm.swappiness参数，此参数代表了内核对于交换空间的喜好(或厌恶)程度。Swappiness 可以有 0 到 100 的值，默认的大小通常是60，但也有的是30。设置这个参数为较低的值会减少内存的交换，从而提升一些系统上的响应度。如果内存较为充裕，则可以将vm.swappiness大小设定为30，如果内存较少，可以设定为60。如果将此数值调整的过大，可能损失内存本来能提供的性能，并增加磁盘IO消耗和CPU的消耗。)


### 开发问题 

js与后端交互采用 JSON串,使用前先打包.
JSON.stringify({
            message: $scope.newMessage,
            username: $scope.username
        }));


日期转换
String str = "1986-04-08 12:30";
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
LocalDateTime dateTime = LocalDateTime.of(1986, Month.APRIL, 8, 12, 30);
String formattedDateTime = dateTime.format(formatter); // "1986-04-08 12:30"

JPA
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#project
