# Spring Cloud 
with Netflix Eureka Server

```yaml
spring:
  application:
    name: discovery-eureka-service
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```
* spring.application.name : 해당 서비스의 이름을 명명함

* server.port : 유레카 서버의 포트를 명시함.

* eureka.client.register-with-eureka : 독립적으로 동작하는 유레카 서버는 다른 Eureka 서버에 등록될 필요가 없음.
* eureka.client.fetch-registry : 다른 Eureka 서버로부터 서비스 레지스트리 값을 가져올 필요가 없어서 false로 설정

만약 클러스터 환경이라면 register-with-eureka와 fetch-registry 값을 true로 변경해야 됨.