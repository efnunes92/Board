package br.com.erikferreira.persistence.dao;

import br.com.erikferreira.dto.BoardColumnDTO;
import br.com.erikferreira.persistence.entity.BoardColumnEntity;

import br.com.erikferreira.persistence.entity.CardEntity;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.erikferreira.persistence.entity.BoardColumnKindEnum.findByName;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(BoardColumnEntity boardColumnEntity) throws SQLException{
        var sql = "INSERT INTO BOARD_COLUMNS (name, order_index, kind, board_id) VALUES (?, ?, ?, ?)";
        try(var ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, boardColumnEntity.getName());
            ps.setInt(2, boardColumnEntity.getOrderIndex());
            ps.setString(3, boardColumnEntity.getKind().name());
            ps.setLong(4, boardColumnEntity.getBoard().getId());
            ps.executeUpdate();
            try(var rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    boardColumnEntity.setId(rs.getLong(1));
                }
            }
            return boardColumnEntity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException {
        List<BoardColumnEntity> list = new ArrayList<>();
        var sql = "SELECT id, name, order_index, kind FROM BOARD_COLUMNS WHERE board_id = ? ORDER BY order_index";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            while (rs.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(rs.getLong("id"));
                entity.setName(rs.getString("name"));
                entity.setOrderIndex(rs.getInt("order_index"));
                entity.setKind(findByName(rs.getString("kind")));
                list.add(entity);
            }
        }
        return list;
    }

    public List<BoardColumnDTO> findByBoardIdWithDatails(Long id) throws SQLException {
        List<BoardColumnDTO> list = new ArrayList<>();
        var sql = "SELECT bc.id, bc.name, bc.kind, " +
                   "(SELECT COUNT(c.id) " +
                    "from CARDS c " +
                   "where c.board_column_id = bc.id) cards_amount " +
                   " FROM BOARD_COLUMNS bc" +
                   " WHERE board_id = ? " +
                   "ORDER BY order_index";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            while (rs.next()) {
                var dtoId = rs.getLong("id");
                var dtoName = rs.getString("name");
                var dtoKind = findByName(rs.getString("kind"));
                var dtoCardsAmaunt = rs.getInt("cards_amount");
                var dto = new BoardColumnDTO(dtoId, dtoName, dtoKind, dtoCardsAmaunt);
                list.add(dto);
            }
        }
        return list;
    }

    public Optional<BoardColumnEntity> findById(Long id) throws SQLException {
        List<BoardColumnEntity> list = new ArrayList<>();
        var sql = "SELECT bc.name, bc.kind, c.id, c.title, c.description" +
                  "  FROM BOARD_COLUMNS bc" +
                  "  LEFT JOIN CARDS c " +
                  "    ON c.board_column_id = bc.id " +
                  " WHERE board_id = ? ";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            if(rs.next()){
                var entity = new BoardColumnEntity();
                entity.setName(rs.getString("name"));
                entity.setKind(findByName(rs.getString("kind")));
                do {
                    if(isNull(rs.getString("title"))){
                        break;
                    }
                    var card = new CardEntity();
                    card.setId(rs.getLong("id"));
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    entity.getCards().add(card);
                }while (rs.next());
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }
}
