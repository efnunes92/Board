package br.com.erikferreira.persistence.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private Long id;
    private String name;
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();
}
