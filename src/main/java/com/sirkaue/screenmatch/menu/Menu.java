package com.sirkaue.screenmatch.menu;

import com.sirkaue.screenmatch.model.DadosSerie;
import com.sirkaue.screenmatch.model.DadosTemporada;
import com.sirkaue.screenmatch.model.Episodio;
import com.sirkaue.screenmatch.model.Serie;
import com.sirkaue.screenmatch.repository.SerieRepository;
import com.sirkaue.screenmatch.service.ConsumoApi;
import com.sirkaue.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6789de82";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();

    public Menu(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        int opcao = -1;

        while (opcao != 0) {
            String menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                                        
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
//        dadosSeries.add(dados);
        repository.save(serie);
        System.out.println(dados);
        System.out.println();
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        String nomeSerie = sc.nextLine();
        String json = consumoApi
                .obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escola uma série pelo nome: ");
        String nomeSerie = sc.nextLine();
        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            Serie serieEncotrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncotrada.getTotalTemporadas(); i++) {
                String json = consumoApi.obterDados(ENDERECO + serieEncotrada
                        .getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            System.out.println();

            List<Episodio> episodios = temporadas.stream().flatMap(d -> d.episodios().stream()
                    .map(e -> new Episodio(d.temporada(), e))).collect(Collectors.toList());
            serieEncotrada.setEpisodios(episodios);
            repository.save(serieEncotrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
        System.out.println();
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escola uma série pelo nome: ");
        String nomeSerie = sc.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.printf("Dados da série: %s", serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }
}
