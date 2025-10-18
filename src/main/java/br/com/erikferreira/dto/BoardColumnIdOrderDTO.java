package br.com.erikferreira.dto;

import br.com.erikferreira.persistence.entity.BoardColumnKindEnum;

public record BoardColumnIdOrderDTO(Long id, int order, BoardColumnKindEnum kind) {
}
