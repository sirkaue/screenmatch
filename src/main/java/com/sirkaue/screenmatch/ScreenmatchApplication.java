package com.sirkaue.screenmatch;

import com.sirkaue.screenmatch.model.DadosEpisodio;
import com.sirkaue.screenmatch.model.DadosSerie;
import com.sirkaue.screenmatch.model.DadosTemporada;
import com.sirkaue.screenmatch.service.ConsumoApi;
import com.sirkaue.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

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

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas() ; i++) {
            json = consumoApi
                    .obterDados("https://www.omdbapi.com/?t=Game+of+Thrones&season=" + i + "&apikey=6789de82");
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        for (DadosTemporada temporada : temporadas){
            System.out.println(temporada);
        }
    }
}
