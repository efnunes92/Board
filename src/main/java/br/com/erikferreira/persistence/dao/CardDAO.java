package br.com.erikferreira.persistence.dao;

import br.com.erikferreira.dto.CardDetailsDTO;
import br.com.erikferreira.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.erikferreira.persistence.util.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO cards (title, description, board_column_id) VALUES (?, ?, ?)";

        try(var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var i = 1;
            ps.setString(i++, entity.getTitle());
            ps.setString(i++, entity.getDescription());
            ps.setLong(i, entity.getBoardColumnEntity().getId());
            ps.executeUpdate();
            try(var rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getLong(1));
                }
            }
        }
        return entity;
    }

    public void moveToColumn(final Long cardId, final Long cardColumnId) throws SQLException {
        var sql = """
                UPDATE cards set board_column_id = ? where id = ?; 
                """;
        try(var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, cardColumnId);
            ps.setLong(2, cardId);
            ps.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(Long id) throws SQLException {
        List<CardEntity> list = new ArrayList<>();
        var sql = "SELECT " +
                        " c.id, " +
                        " c.title, " +
                        " c.description, " +
                        " c.BOARD_COLUMN_ID, " +
                        " b.blocked_at, " +
                        " b.block_reason, " +
                        " bc.name, " +
                        " (SELECT COUNT(sub_b.id) FROM BLOCKS sub_b WHERE sub_b.card_id = c.id) AS blocks_amount " +
                        "FROM CARDS c " +
                        "LEFT JOIN BLOCKS b ON c.id = b.card_id AND b.UNBLOCKED_AT IS NULL " +
                        "INNER JOIN BOARD_COLUMNS bc ON bc.id = c.board_column_id " +
                        "WHERE c.id = ?";

        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            if(rs.next()) {
                var dto = new CardDetailsDTO(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        nonNull(rs.getString("block_reason")),
                        toOffsetDateTime(rs.getTimestamp("blocked_at")),
                        rs.getString("block_reason"),
                        rs.getInt("blocks_amount"),
                        rs.getLong("BOARD_COLUMN_ID"),
                        rs.getString("name")
                );
                return Optional.of(dto);
            }

        }
        return Optional.empty();
    }
}
