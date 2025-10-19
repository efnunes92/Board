package br.com.erikferreira.service;

import br.com.erikferreira.dto.BoardColumnIdOrderDTO;
import br.com.erikferreira.dto.CardDetailsDTO;
import br.com.erikferreira.exception.CardBlockedException;
import br.com.erikferreira.exception.CardFinishedException;
import br.com.erikferreira.exception.EntityNotFoundException;
import br.com.erikferreira.persistence.dao.CardDAO;
import br.com.erikferreira.persistence.entity.BoardColumnKindEnum;
import br.com.erikferreira.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class CardService {
    private final Connection connection;

    public CardEntity insert(final CardEntity cardEntity) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            dao.insert(cardEntity);
            connection.commit();
            return cardEntity;
        }catch(Exception e){
            connection.rollback();
            throw e;
        }
    }

    public void moveCard(final Long cardId, final List<BoardColumnIdOrderDTO> boardColumnInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));
            if(dto.blocked()){
                var message = "O card %s está bloqueado, é necessário desbloquea-lo para mover".formatted(dto.id());
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL)) {
                throw new CardFinishedException("O card de id %s já foi finalizado.".formatted(cardId));
            };
            var nextColumn = boardColumnInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(nextColumn.id(), cardId);
            connection.commit();
        }catch(Exception e){
            connection.rollback();
            throw e;
        }
    }

    public void cancel(final Long cardId,
                       final Long cancelColumnId,
                       final List<BoardColumnIdOrderDTO> boardColumnInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));
            if(dto.blocked()){
                var message = "O card %s está bloqueado, é necessário desbloquea-lo para mover".formatted(dto.id());
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board."));
            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL)) {
                throw new CardFinishedException("O card de id %s já foi finalizado.".formatted(cardId));
            };
            boardColumnInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(cancelColumnId, cardId);
            connection.commit();
        }catch(Exception e){
            connection.rollback();
            throw e;
        }
    }

}
