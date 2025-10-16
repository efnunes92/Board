package br.com.erikferreira.persistence.dao;

import br.com.erikferreira.persistence.entity.BoardColumnEntity;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.erikferreira.persistence.entity.BoardColumnKindEnum.findByName;

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
}
