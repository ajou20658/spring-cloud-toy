# Order Service
* 주문을 처리하고, 주문 내역을 조회하는 서비스(회원의 식별이 필요함)
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
    name: order-service
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

## API(2개)
### 주문 요청
* URL : `/{userId}/orders`
* Method : POST
* RequestBody : 
```json
{
    "productId":"CATALOG-001",
    "qty":10,
    "unitPrice":1500
}
```
* Response :
```json
{
  "productId": "CATALOG-001",
  "qty": 10,
  "unitPrice": 1500,
  "totalPrice": 15000,
  "orderId": "7ad05bd9-0548-4313-932c-6a1cd22682d1"
}
```

### 주문 조회
* URL : `/{userId}/orders`
* Method : GET
* Response :
```json
[
  {
    "productId": "CATALOG-001",
    "qty": 10,
    "unitPrice": 1500,
    "totalPrice": 15000,
    "createdAt": "2024-05-26T14:41:20.468+00:00",
    "orderId": "7ad05bd9-0548-4313-932c-6a1cd22682d1"
  }
]
```
