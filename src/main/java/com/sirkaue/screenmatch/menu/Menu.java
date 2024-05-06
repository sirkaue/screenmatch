package com.sirkaue.screenmatch.menu;

import com.sirkaue.screenmatch.model.DadosSerie;
import com.sirkaue.screenmatch.model.DadosTemporada;
import com.sirkaue.screenmatch.service.ConsumoApi;
import com.sirkaue.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6789de82";

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome da série para busca: ");
        String nomeSerie = sc.nextLine();

        String json = consumoApi
                .obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoApi.obterDados
                    (ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        for (DadosTemporada temporada : temporadas) {
            System.out.println(temporada);
        }
    }
}
