package com.example.gateway.filter;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
    Environment env;
    public AuthorizationFilter(Environment env){
        super(Config.class);
        this.env=env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ","");
            if(!isJwtValid(jwt)){
                return onError(exchange, "Jwt token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        String subject=null;

        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        try{
            subject = parser.parseSignedClaims(jwt).getPayload().getSubject();
        }catch (Exception ex){
            returnValue = false;
        }

        if(subject==null||subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;
    }
    // Mono,Flux 란? Webflux에서 데이터를 처리하는 단위 단일 이냐 다중이냐에 따라서 구분해서 사용됨
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response= exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

    public static class Config{

    }
}
