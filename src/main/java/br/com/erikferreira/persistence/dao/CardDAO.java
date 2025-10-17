package br.com.erikferreira.persistence.dao;

import br.com.erikferreira.dto.CardDetails;
import br.com.erikferreira.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardDetails findById(Long id) throws SQLException {
        List<CardEntity> list = new ArrayList<>();
        var sql = "SELECT id, title, description, board_column FROM CARDS WHERE id = ? ";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeQuery();
            var rs = ps.getResultSet();
            while (rs.next()) {
                var entity = new CardEntity();
                entity.setId(rs.getLong("id"));
                entity.setTitle(rs.getString("title"));
                entity.setDescription(rs.getString("description"));

                list.add(entity);
            }
        }
        return null;
    }

}
