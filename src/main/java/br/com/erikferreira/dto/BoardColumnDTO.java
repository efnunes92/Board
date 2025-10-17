package br.com.erikferreira.dto;

import br.com.erikferreira.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id, String name, BoardColumnKindEnum kind, int cardsAmaunt) {
}
