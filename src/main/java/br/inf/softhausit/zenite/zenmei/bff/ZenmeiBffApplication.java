package br.inf.softhausit.zenite.zenmei.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ZenMei BFF - Backend for Frontend
 * <p>
 * Main application class that bootstraps the BFF service.
 * This service acts as an integration layer between the frontend and all microservices.
 * </p>
 *
 * @author ZenMei Development Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
public class ZenmeiBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZenmeiBffApplication.class, args);
    }
}
