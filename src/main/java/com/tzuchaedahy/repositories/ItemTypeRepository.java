package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemTypeRepository {
    private Connection conn;

    public ItemTypeRepository() {
        this.conn = Db.getConnection();
    }

    public List<ItemType> findAll() {
        Statement statement = null;
        ResultSet result = null;

        String query = "select * from item_type";

        List<ItemType> itemTypes = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                ItemType itemType = new ItemType();

                itemType.setId(UUID.fromString(result.getString("id")));
                itemType.setName(result.getString("name"));

                var attributes = (String[]) result.getArray("default_attributes").getArray();

                for (String attribute : attributes) {
                    itemType.setDefaultAttribute(attribute);
                }

                var names = (String[]) result.getArray("default_names").getArray();

                for (String name : names) {
                    itemType.setDefaultName(name);
                }

                itemTypes.add(itemType);
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os tipos de item");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return itemTypes;
    }

    public ItemType findByName(String name) {
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = "select * from item_type where name = ?";

        ItemType itemType = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, name);

            result = statement.executeQuery();
            if (result.next()) {
                itemType = new ItemType();
                itemType.setId(result.getObject("id", UUID.class));
                itemType.setName(result.getString("name"));

                var attributes = (String[]) result.getArray("default_attributes").getArray();
                for (String attribute : attributes) {
                    itemType.setDefaultAttribute(attribute);
                }

                var names = (String[]) result.getArray("default_names").getArray();
                for (String defaultName : names) {
                    itemType.setDefaultName(defaultName);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar um tipo de item");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return itemType;
    }

    public String[] findDefaultNames(ItemType itemType) {
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = "select it.default_names from item_type it where it.id = ?";

        String[] defaultNames = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, itemType.getId());

            result = statement.executeQuery();
            if (result.next()) {
                defaultNames = (String[]) result.getArray("default_names").getArray();
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar um tipo de item");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return defaultNames;
    }
}
