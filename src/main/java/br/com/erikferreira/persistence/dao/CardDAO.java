package br.com.erikferreira.persistence.dao;

import br.com.erikferreira.dto.CardDetails;
import br.com.erikferreira.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.erikferreira.persistence.util.OffsetDateTimeConverter.toOffsetDateTime;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public Optional<CardDetails> findById(Long id) throws SQLException {
        List<CardEntity> list = new ArrayList<>();
        var sql = "SELECT c.id, c.title, c.description, c.BOARD_COLUMN_ID" +
                  "       b.blocked_at, b.block_reason, " +
                  "       bc.name" +
                  "       COUNT(SELECT sub_b.id " +
                  "               FROM BLOCKS sub_b " +
                  "              WHERE sub_b.card_id = c.id) blocks_amount" +
                  "  FROM CARDS c " +
                  "  LEFT JOIN BLOCKS b" +
                  "    ON c.id = b.card_id" +
                  "   AND b.UNBLOCKED_AT IS NULL" +
                  " INNER JOIN BOARDS_COLUMNS bc " +
                  "    ON bc.id = c.board_column_id" +
                  " WHERE id = ? ";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            if(rs.next()) {
                var dto = new CardDetails(
                        rs.getLong("c.id"),
                        rs.getString("c.title"),
                        rs.getString("c.description"),
                        true,
                        toOffsetDateTime(rs.getTimestamp("b.blocked_at")),
                        rs.getString("b.block_reason"),
                        rs.getInt("blocks_amount"),
                        rs.getLong("c.BOARD_COLUMN_ID"),
                        rs.getString("c.name")
                );
                return Optional.of(dto);
            }

        }
        return Optional.empty();
    }
}
