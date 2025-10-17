package br.com.erikferreira.service;

import br.com.erikferreira.dto.BoardDetailsDTO;
import br.com.erikferreira.persistence.dao.BoardColumnDAO;
import br.com.erikferreira.persistence.dao.BoardDAO;
import br.com.erikferreira.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        var optional = dao.findById(id);
        if(optional.isPresent()) {
            var entity = optional.get();
            entity.setBoardColumns(boardColumnDao.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public boolean exists(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        return dao.exists(id);
    }

    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        var optional = dao.findById(id);
        if(optional.isPresent()) {
            var entity = optional.get();
            var columns = boardColumnDao.findByBoardIdWithDatails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
