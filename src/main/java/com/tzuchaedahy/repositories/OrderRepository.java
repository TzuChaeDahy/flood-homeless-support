package com.tzuchaedahy.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.domain.Shelter;
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

    public List<Order> pendingOrders(DistributionCenter distributionCenter) {
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = "select o.id as id, s.name as shelter_name from \"order\" o inner join shelter s on s.id = o.shelter_id inner join order_item oi on oi.order_id = o.id inner join item i on i.id = oi.item_id inner join item_donation itd on itd.item_id = i.id inner join donation d on d.id = itd.donation_id where o.order_status_id = '1e6ea845-8af0-4468-afb5-b9e6bed8738b' and d.distribution_center_id = (?)";

        List<Order> pendingOrders = new ArrayList<>();
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, distributionCenter.getId());

            result = statement.executeQuery();
            while (result.next()) {
                Shelter shelter = new Shelter();
                shelter.setName(result.getString("shelter_name"));

                Order order = new Order();
                order.setId(result.getObject("id", UUID.class));
                order.setShelter(shelter);

                pendingOrders.add(order);
            }

        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um pedido");
        }

        return pendingOrders;
    }

    public void changeStatus(UUID orderID, UUID orderStatusID, String description) {
        PreparedStatement statement = null;

        String query = "update \"order\" set order_status_id = (?), description = (?) where id = (?)";
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, orderStatusID);
            if (description != null) {
                statement.setString(2, description);
            } else {
                statement.setString(2, "");
            }
            statement.setObject(3, orderID);

            int affectedRows = statement.executeUpdate();
            if (affectedRows <= 0) {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao mudar o status de um pedido" + e.getMessage());
        }
    }
}
