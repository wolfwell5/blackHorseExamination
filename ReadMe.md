
###1. (to run rabbitMQ locally for app)
docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management


blackHorse part:
1. gradle build first, like this ![](gralde-refresh.png)
2. after the refresh finished, right click on test Directory, then choose `Run Tests in blackHorse`
3. 本地想启动的话，需要先docker运行数据库 `docker-compose -f docker-compose.yml up -d blackHorse-database`
