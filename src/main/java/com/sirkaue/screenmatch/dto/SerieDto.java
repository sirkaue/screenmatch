package com.sirkaue.screenmatch.dto;

import com.sirkaue.screenmatch.model.Categoria;

public record SerieDto(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
