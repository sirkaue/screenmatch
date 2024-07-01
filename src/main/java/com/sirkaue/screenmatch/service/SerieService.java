package com.sirkaue.screenmatch.service;

import com.sirkaue.screenmatch.dto.EpisodioDto;
import com.sirkaue.screenmatch.dto.SerieDto;
import com.sirkaue.screenmatch.model.Categoria;
import com.sirkaue.screenmatch.model.Serie;
import com.sirkaue.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDto> findAll() {
        return convertEntityToDtoList(repository.findAll());
    }

    public List<SerieDto> findByTop5Series() {
        return convertEntityToDtoList(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDto> findLatestReleases() {
        return convertEntityToDtoList(repository.latestReleases());
    }

    public SerieDto findById(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDto> findAllSeason(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDto(e.getTemporada(),
                    e.getNumeroEpisodio(),
                    e.getTitulo())).collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDto> findSeasonByNumber(Long id, Long number) {
        return repository.findEpisodesBySeason(id, number)
                .stream()
                .map(e -> new EpisodioDto(e.getTemporada(),
                e.getNumeroEpisodio(),
                e.getTitulo())).collect(Collectors.toList());
    }

    public List<SerieDto> findSeriesByCategory(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return convertEntityToDtoList(repository.findByGenero(categoria));
    }

    private List<SerieDto> convertEntityToDtoList(List<Serie> series) {
        return series.stream().map(s -> new SerieDto(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()))
                .collect(Collectors.toList());
    }
}
