package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
