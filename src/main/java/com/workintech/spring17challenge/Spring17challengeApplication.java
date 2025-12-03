package com.workintech.spring17challenge;

import com.workintech.spring17challenge.entity.HighCourseGpa;
import com.workintech.spring17challenge.entity.LowCourseGpa;
import com.workintech.spring17challenge.entity.MediumCourseGpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Spring17challengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring17challengeApplication.class, args);
    }

    // Bean tanımlamaları, dependency injection için
    @Bean
    public LowCourseGpa lowCourseGpa() {
        return new LowCourseGpa();
    }

    @Bean
    public MediumCourseGpa mediumCourseGpa() {
        return new MediumCourseGpa();
    }

    @Bean
    public HighCourseGpa highCourseGpa() {
        return new HighCourseGpa();
    }
}
