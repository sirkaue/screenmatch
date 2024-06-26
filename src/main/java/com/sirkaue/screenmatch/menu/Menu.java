package com.sirkaue.screenmatch.menu;

import com.sirkaue.screenmatch.model.*;
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
    private Optional<Serie> serieBuscada;

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
                    5 - Buscar série por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódios por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios à partir de uma data
                    
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
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorDataDeLancamento();
                    break;
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
        serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.printf("Dados da série: %s", serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome para a busca? ");
        String nomeAtor = sc.nextLine();
        System.out.println("Avalicações à partir de qual valor? ");
        Double avaliacao = sc.nextDouble();
        List<Serie> seriesEncontradas = repository
                .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        if (!seriesEncontradas.isEmpty()) {
            System.out.printf("Séries em que o ator %s trabalhou: %n", nomeAtor);
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " - Avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("Não encontrado");
        }
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s -> System.out.println(s.getTitulo() + " - Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar série de qual categoria/gênero? ");
        String nomeGenero = sc.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        if (!seriesPorCategoria.isEmpty()) {
            System.out.printf("Séries da categoria %s %n", nomeGenero);
            seriesPorCategoria.forEach(System.out::println);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        Integer totalTemporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        Double avaliacao = sc.nextDouble();
        sc.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - Avaliação: " + s.getAvaliacao()));

    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca? ");
        String trechoEpisodio = sc.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.println("Série: "
                + e.getSerie().getTitulo()
                + " - Temporada: "
                + e.getTemporada()
                + " - Episódio: "
                + e.getNumeroEpisodio() + " - "
                + e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.println("Série: "
                    + e.getSerie().getTitulo()
                    + " - Temporada: "
                    + e.getTemporada()
                    + " - Episódio: "
                    + e.getNumeroEpisodio() + " - "
                    + e.getTitulo() + " - "
                    + "Avaliação: "
                    + e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosPorDataDeLancamento() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            System.out.println();
            System.out.println("Digite o ano limite de lançamento: ");
            Integer anoLancamento = sc.nextInt();
            sc.nextLine();

            List<Episodio> episodiosAno = repository.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }
}
