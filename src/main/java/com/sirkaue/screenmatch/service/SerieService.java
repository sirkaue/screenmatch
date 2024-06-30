package com.sirkaue.screenmatch.service;

import com.sirkaue.screenmatch.dto.SerieDto;
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
