package com.expmanagment;

import com.expmanagment.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableOpenApi
@EnableSwagger2
@EnableWebMvc
@ServletComponentScan(basePackages = "com.expmanagment.utils")
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ExpManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpManagmentApplication.class, args);
    }

}
