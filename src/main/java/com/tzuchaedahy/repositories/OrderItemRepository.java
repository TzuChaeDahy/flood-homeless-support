package com.tzuchaedahy.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.domain.Shelter;
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

    public OrderItem findByOrderID(UUID orderUUID) {
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = "select i.id as id, i.name as name, i.attributes as attributes, oi.quantity as quantity from order_item oi inner join item i on i.id = oi.item_id where oi.order_id = (?)";

        ObjectMapper mapper = new ObjectMapper();
        OrderItem orderItem = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, orderUUID);

            result = statement.executeQuery();
            if (result.next()) {
                orderItem = new OrderItem();

                var item = new Item();

                var attributes = result.getString("attributes");
                Map<String, String> attributesMap = mapper.readValue(attributes,
                        new TypeReference<Map<String, String>>() {
                        });

                Map<String, String> orderedAttributesMap = new TreeMap<>();
                var treeSet = new TreeSet<>(attributesMap.keySet());

                treeSet.forEach(key -> {
                    orderedAttributesMap.put(key, attributesMap.get(key));
                });

                item.setId(result.getObject("id", UUID.class));
                item.setName(result.getString("name"));
                item.setAttributes(orderedAttributesMap);

                orderItem.setItem(item);
                orderItem.setQuantity(result.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar um item de pedido por id");
        } catch (JsonProcessingException e) {
            throw new RepositoryException("ocorreu ao converter atributos");
        }

        return orderItem;
    }

    public Integer countItemsByDistributionCenterAndType(Shelter shelter, ItemType itemType) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select sum(oi.quantity) from order_item oi inner join \"order\" o on oi.order_id = o.id inner join item i on oi.item_id = i.id where o.shelter_id = (?) and i.item_type_id = (?) and o.order_status_id = '75040965-0e33-4eba-a94a-4f21d3be3ce0'";

        int quantity = 0;
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, shelter.getId());
            statement.setObject(2, itemType.getId());

            result = statement.executeQuery();

            if (result.next()) {
                quantity = result.getInt("sum");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os items");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return quantity;
    }
}
