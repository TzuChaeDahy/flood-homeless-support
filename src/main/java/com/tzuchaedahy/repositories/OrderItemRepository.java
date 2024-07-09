package com.tzuchaedahy.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

public class OrderItemRepository {
    private Connection conn;

    public OrderItemRepository() {
        conn = Db.getConnection();
    }

    public void saveAll(List<OrderItem> orderItems) {
        PreparedStatement statement = null;

        String query = "insert into order_item (item_id, order_id, quantity) values (?, ?, ?)";

        try {
            for (OrderItem orderItem : orderItems) {
                statement = conn.prepareStatement(query);
                statement.setObject(1, orderItem.getItem().getId());
                statement.setObject(2, orderItem.getOrder().getId());
                statement.setInt(3, orderItem.getQuantity());

                int affectedRows = statement.executeUpdate();
                if (affectedRows < 1) {
                    throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um item de pedido" + e.getMessage());
        }
    }
}
