package org.olamide.academicrecordmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class AcademicRecordManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicRecordManagementSystemApplication.class, args);
    }

}
