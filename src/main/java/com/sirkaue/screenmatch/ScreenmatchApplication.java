package com.sirkaue.screenmatch;

import com.sirkaue.screenmatch.service.ConsumoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    @Autowired
    private ConsumoApi consumoApi;

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(consumoApi
                .obterDados("https://www.omdbapi.com/?t=Guardians+of+the+Galaxy+Vol.+2&apikey=6789de82"));
    }
}
