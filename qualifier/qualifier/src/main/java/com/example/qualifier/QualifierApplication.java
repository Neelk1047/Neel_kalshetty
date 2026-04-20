package com.example.qualifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class QualifierApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QualifierApplication.class, args);
    }

    @Override
    public void run(String... args) {

        RestTemplate restTemplate = new RestTemplate();

        String url =
        "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String,String> request = new HashMap<>();
        request.put("name","Neel Kalshetty");
        request.put("regNo","ADT24SOCBD073");
        request.put("email","neelkalshetty9888@gmail.com");

        ResponseEntity<Map> response =
        restTemplate.postForEntity(url, request, Map.class);

        String webhook =
        response.getBody().get("webhook").toString();

        String token =
        response.getBody().get("accessToken").toString();

        String finalQuery =
        "SELECT p.AMOUNT AS SALARY, " +
        "CONCAT(e.FIRST_NAME,' ',e.LAST_NAME) AS NAME, " +
        "TIMESTAMPDIFF(YEAR,e.DOB,CURDATE()) AS AGE, " +
        "d.DEPARTMENT_NAME " +
        "FROM PAYMENTS p " +
        "JOIN EMPLOYEE e ON p.EMP_ID=e.EMP_ID " +
        "JOIN DEPARTMENT d ON e.DEPARTMENT=d.DEPARTMENT_ID " +
        "WHERE DAY(p.PAYMENT_TIME)<>1 " +
        "ORDER BY p.AMOUNT DESC LIMIT 1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        Map<String,String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpEntity<Map<String,String>> entity =
        new HttpEntity<>(body, headers);

        ResponseEntity<String> submit =
        restTemplate.postForEntity(webhook, entity, String.class);

        System.out.println(submit.getBody());
    }
}