package com.minxing.fileconventer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableScheduling
public class FileConventerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileConventerApplication.class, args);
    }
}
