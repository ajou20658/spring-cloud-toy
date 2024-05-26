# Catalog Service
* 물품의 정보를 알려주는 서비스(회원의 식별이 필요없음)
## Eureka Client Config
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    fetch-registry: true
    register-with-eureka: true
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
```
* eureka.client.service-url.defaultZone : Eureka 서버의 Availability zone
* eureka.client.fetch-registry : Eureka 서버에 등록된 서비스들의 정보를 가져와 동기화한다.
* eureka.client.register-with-eureka : Eureka 서버에 자신의 정보를 등록한다.
* eureka.instance.instance-id : 0번 포트의 값을 랜덤한 값을 할당하여 Eureka 서버에서 구분 가능하게 설정. 
## Spring Config
```yaml
server:
  port: 0

spring:
  application:
    name: catalog-service
  h2:
    console:
      enabled: true
      settings:
        web-allow-others: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    generate-ddl: true
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
    username: sa
    password:

logging:
  level:
    com.example.catalogservice: DEBUG
```
* server.port=0
  * 테스트 환경에서 사용하는 방법.
  * 중복된 포트를 피할 수 있으며
  * 사용 가능한 포트를 할당해준다.

## API(1개)
* URL : `/catalogs`
* Method : GET
* Response : 
```json
[
    {
        "productId": "CATALOG-001",
        "productName": "Berlin",
        "unitPrice": 1500,
        "stock": 100,
        "createdAt": "2024-05-26T14:13:02.006+00:00"
    },
    {
      "...": "..."
    }
]
```