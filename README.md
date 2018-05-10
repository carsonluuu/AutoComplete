# AutoComplete Based On Hadoop and MAMP
*The project is divided into two parts. First of all, after setting up the development enviroment, we use hadoop offline create the database for N-gram model output. Sencodly, we deploy online MAMP server to display the result online*

Enviroment
-----
1. You will need Docker(For Mac): https://docs.docker.com/docker-for-mac/
```Shell
$ mkdir bigdata-class2
$ cd bigdata-class2
$ sudo docker pull joway/hadoop-cluster # From dockerhub to get image
$ git clone https://github.com/joway/hadoop-cluster-docker # get from github
$ sudo docker network create --driver=bridge hadoop #network bridge for cummunication
$ cd hadoop-cluster-docker
```
Into Docker
```Shell
$ sudo ./start-container.sh
```
Run hadoop
```Shell
$ ./start-hadoop.sh
```
2. You will need MAMP(For Mac): https://www.mamp.info/en/
- Start server 
- Browse into localhost:8888

Run
-----
1. Get IP for your PC:
```Shell
$ ifconfig | grep inet | grep broadcast #inet
```
2. Get the port for MySQL:
```Shell
$ SHOW VARIABLES WHERE Variable_name = 'port' ; #under mysql
```
3. Configuration for MySQL:
```Shell
$ cd /Applications/MAMP/Library/bin/
$ ./mysql -uroot -p 
$ create database test;  # create test database 
$ use test; 
$ create table output(starting_phrase VARCHAR(250), following_word VARCHAR(250), count INT); 
$ GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '**_your-password_** ' WITH GRANT OPTION;   //enable remote data transfer
$ FLUSH PRIVILEGES;
```
4. Configuration for Hadoop:
```Shell
$ cd src
$ wget https://s3-us-west-2.amazonaws.com/jiuzhang-bigdata/mysql-connector-java-5.1.39-bin.jar
$ hdfs dfs -mkdir /mysql # hdfs -> mysql file
$ hdfs dfs -put mysql-connector-java-*.jar /mysql/  #hdfs path to mysql-connector*
$ wget https://s3-us-west-2.amazonaws.com/jiuzhang-bigdata/wikispeedia_articles_plaintext.tar.gz
$ cd NGram
$ hdfs dfs -mkdir -p input
$ hdfs dfs -rm -r /output 
$ hdfs dfs -put bookList/*  input/ 
$ cd src
```
```Java
DBConfiguration.configureDB(conf2,
  "com.mysql.jdbc.Driver", // driver class
  "jdbc:mysql://local_ip_address:MySQL_port/test", // db url
  "root", // user name
  “your_password”); //password
job2.addArchiveToClassPath(new Path(“hdfs_path_to_mysql-connector”));
```
5. Run Project
```Shell
$ hadoop com.sun.tools.javac.Main *.java
$ jar cf ngram.jar *.class
$ hadoop jar ngram.jar Driver input /output 2 3 4
2 is ngram_size 
3 is threashold_size，omit when count<threashold 
4 is following_word_size
```
6. Check
```MySQL
$ select * from output limit 10 ;
```
7. MAMP Deployment
- put Autocomplete into web server path MAMP/htdocs
- Edit Autocomplete/ajax_refresh.php to change port number and password
- Restart server
- Go to http://localhost/autocomplete/
8. Problems
```
INFO mapreduce.Job: Task Id : attempt_1474070831522_0002_r_000000_1, Status : FAILED
Error: java.io.IOException: null, message from server: "Host 'x.x.x.x' is not allowed to connect to this MySQL server"
```
```mysql
$ GRANT ALL ON *.* to 'root'@'id_address' IDENTIFIED BY 'your-password'; //enable remote data transfer
$ FLUSH PRIVILEGES;
```
```
root use no PRIVILEGES
```
```mysql
$ GRANT ALL ON *.* to 'root'@'ip_address' IDENTIFIED BY 'your-password'; //enable remote data transfer
$ GRANT ALL PRIVILEGES ON *.* TO 'root'@'ip_address' WITH GRANT OPTION;
```
```
Communication links failure --> Check port and IP
```

