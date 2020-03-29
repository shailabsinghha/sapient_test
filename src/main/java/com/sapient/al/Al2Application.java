package com.sapient.al;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(value = {"com.sapient.al.repo"})
//@EnableMongoRepositories (value = {"com.pulse.proto.repo.mongo"})
public class Al2Application {

    public static void main(String[] args) {
        SpringApplication.run(Al2Application.class, args);
    }

}
