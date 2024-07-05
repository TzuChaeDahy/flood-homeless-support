package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class ItemRepository {
    private Connection conn;

    public ItemRepository() {
        this.conn = Db.getConnection();
    }

    public void save(Item item) {
        PreparedStatement statement = null;

        String query = "insert into item (name, attributes, item_type_id) values (?, ?::jsonb, ?)";

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getName());
            statement.setObject(2, item.attributesToJson(), java.sql.Types.OTHER);
            statement.setObject(3, item.getItemType().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                var result = statement.getGeneratedKeys();
                if (result.next()) {
                    item.setId(result.getObject(1, UUID.class));
                }
            } else {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um item");
        } finally {
            Db.closeStatement(statement);
        }
    }
}
