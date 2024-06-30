package com.sirkaue.screenmatch.controller;

import com.sirkaue.screenmatch.dto.EpisodioDto;
import com.sirkaue.screenmatch.dto.SerieDto;
import com.sirkaue.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDto> obterSeries() {
        return service.findAll();
    }

    @GetMapping("/top5")
    public List<SerieDto> top5Series() {
        return service.findByTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos() {
        return service.findLatestReleases();
    }

    @GetMapping("{id}")
    public SerieDto obterPorId(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> obterTodasTemporadas(@PathVariable Long id) {
        return service.findAllSeason(id);
    }
}
