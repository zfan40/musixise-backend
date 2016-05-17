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
