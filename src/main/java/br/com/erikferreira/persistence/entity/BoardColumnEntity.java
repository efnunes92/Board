package br.com.erikferreira.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {

    private Long id;
    private String name;
    private int orderIndex;
    private BoardColumnKindEnum kind;
    private BoardEntity board = new BoardEntity();

}
