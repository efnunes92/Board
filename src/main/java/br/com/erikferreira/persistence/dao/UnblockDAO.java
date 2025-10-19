package br.com.erikferreira.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.erikferreira.persistence.util.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class UnblockDAO {

    private final Connection connection;

    public void unblock(final Long cardId, final String reason) throws SQLException {
        var sql = "UPDATE blocks " +
                "SET unblocked_at = ?, unblock_reason = ? " +
                "WHERE card_id = ? AND unblock_reason IS NULL";
        try(var ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            ps.setString(2, reason);
            ps.setLong(3, cardId);
            ps.executeUpdate();
        }
    }
}
