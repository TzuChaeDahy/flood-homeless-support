package com.tzuchaedahy.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

public class OrderRepository {
    private Connection conn;

    public OrderRepository() {
        conn = Db.getConnection();
    }

    public void save(Order order) {
        PreparedStatement statement = null;

        String query = "insert into \"order\" (order_status_id, shelter_id) values (?, ?)";

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, order.getStatus().getId());
            statement.setObject(2, order.getShelter().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                var result = statement.getGeneratedKeys();
                if (result.next()) {
                    order.setId(result.getObject(1, UUID.class));
                }
            } else {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um pedido");
        }
    }
}
