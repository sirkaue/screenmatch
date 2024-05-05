package com.sirkaue.screenmatch;

import com.sirkaue.screenmatch.model.DadosFilme;
import com.sirkaue.screenmatch.service.ConsumoApi;
import com.sirkaue.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConsumoApi consumoApi = new ConsumoApi();
        String json = consumoApi
                .obterDados("https://www.omdbapi.com/?t=Guardians+of+the+Galaxy+Vol.+2&apikey=6789de82");
        System.out.println(json);

        ConverteDados conversor = new ConverteDados();

        DadosFilme dadosFilme = conversor.obterDados(json, DadosFilme.class);
        System.out.println(dadosFilme);
    }
}
