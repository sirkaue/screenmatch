package com.sirkaue.screenmatch.controller;

import com.sirkaue.screenmatch.dto.SerieDto;
import com.sirkaue.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping("/series")
    public List<SerieDto> obterSeries() {
        return service.findAll();
    }
}
