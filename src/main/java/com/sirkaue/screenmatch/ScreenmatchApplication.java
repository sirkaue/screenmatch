package com.sirkaue.screenmatch;

import com.sirkaue.screenmatch.model.DadosEpisodio;
import com.sirkaue.screenmatch.model.DadosSerie;
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
                .obterDados("https://www.omdbapi.com/?t=Game+of+Thrones&apikey=6789de82");
        System.out.println(json);

        ConverteDados conversor = new ConverteDados();

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        json = consumoApi
                .obterDados("https://www.omdbapi.com/?t=Game+of+Thrones&season=1&episode=2&apikey=6789de82");
        DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
        System.out.println(dadosEpisodio);
    }
}
