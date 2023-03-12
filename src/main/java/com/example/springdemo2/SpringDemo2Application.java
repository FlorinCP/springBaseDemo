package com.example.springdemo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class SpringDemo2Application {

    public static void main(String[] args)  {
        SpringApplication.run(SpringDemo2Application.class, args);

    }

}
