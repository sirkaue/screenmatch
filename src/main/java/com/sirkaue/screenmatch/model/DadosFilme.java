package com.sirkaue.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosFilme(@JsonAlias("Title") String titulo,
                         @JsonAlias("Runtime") String minutos,
                         @JsonAlias("imdbRating") String avaliacao) {
}
