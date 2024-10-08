package com.ia.smartkaufen;

import org.springframework.boot.SpringApplication;

public class TestSmartkaufenApplication {

    public static void main(String[] args) {
        SpringApplication.from(SmartkaufenApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
