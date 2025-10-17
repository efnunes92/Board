package br.com.erikferreira.dto;

import java.util.List;

public record BoardDetailsDTO(Long id, String name, List<BoardColumnDTO> boardColumnDTO) {
}
