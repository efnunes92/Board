package br.com.erikferreira.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.erikferreira.persistence.util.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final Long cardId, final String reason) throws SQLException {
        var sql = "INSERT INTO blocks (blocked_at, block_reason, card_id) VALUES (?, ?, ?)";
        try(var ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            ps.setString(2, reason);
            ps.setLong(3, cardId);
            ps.executeUpdate();
        }
    }
}
