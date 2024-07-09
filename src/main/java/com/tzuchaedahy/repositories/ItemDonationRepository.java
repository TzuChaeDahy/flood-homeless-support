package com.tzuchaedahy.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

public class ItemDonationRepository {
    private Connection conn;

    public ItemDonationRepository() {
        this.conn = Db.getConnection();
    }

    public void save(ItemDonation itemDonation) {
        PreparedStatement statement = null;

        String query = "insert into item_donation (item_id, donation_id, quantity) values (?, ?, ?)";

        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, itemDonation.getItem().getId());
            statement.setObject(2, itemDonation.getDonation().getId());
            statement.setInt(3, itemDonation.getQuantity());

            int affectedRows = statement.executeUpdate();
            if (affectedRows < 1) {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um item de doacao" + e.getMessage());
        }

    }

    public List<ItemDonation> findAllSimplifiedByDistributionCenter(DistributionCenter distributionCenter) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "SELECT it.name AS item_type_name, i.name AS item_name, i.attributes - 'validade' AS attributes, it.default_names AS default_names,  SUM(idn.quantity) AS total_quantity FROM item i JOIN item_type it ON i.item_type_id = it.id JOIN item_donation idn ON i.id = idn.item_id JOIN donation d ON idn.donation_id = d.id WHERE d.distribution_center_id = (?) GROUP BY it.name, i.name, i.attributes,it.default_names ORDER BY it.name, i.name";

        ObjectMapper mapper = new ObjectMapper();
        List<ItemDonation> itemDonations = new ArrayList<>();
        try {
            statement = conn.prepareStatement(query);
            statement.setObject(1, distributionCenter.getId());
            result = statement.executeQuery();
            while (result.next()) {
                var donation = new Donation();
                var item = new Item();
                var itemType = new ItemType();
                var itemDonation = new ItemDonation();

                item.setName(result.getString("item_name"));

                itemType.setName(result.getString("item_type_name"));
                item.setItemType(itemType);

                var attributes = result.getString("attributes");
                Map<String, String> attributesMap = mapper.readValue(attributes, new TypeReference<>() {
                });
                item.setAttributes(attributesMap);

                itemDonation.setDonation(donation);
                itemDonation.setItem(item);
                itemDonation.setQuantity(result.getInt("total_quantity"));

                itemDonations.add(itemDonation);
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os items");
        } catch (JsonProcessingException e) {
            throw new RepositoryException("ocorreu ao converter atributos");
        }finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return itemDonations;
    }

    public Integer countItemsByDistributionCenterAndType(DistributionCenter distributionCenter, ItemType itemType) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select sum(itd.quantity) from item_donation itd inner join donation d on itd.donation_id = d.id inner join item i on itd.item_id = i.id where d.distribution_center_id = (?) and i.item_type_id = (?)";

        int quantity = 0;
        try {
            statement = conn.prepareStatement(query);

            statement.setObject(1, distributionCenter.getId());
            statement.setObject(2, itemType.getId());

            result = statement.executeQuery();

            if (result.next()) {
                quantity = result.getInt("sum");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os items");
        }finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return quantity;
    }

    public Map<String, List<ItemDonation>> findAvailableItemsByName(String name) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select dc.name as name, it.id as id, it.attributes - 'validade' as attributes, sum (itd.quantity) as quantity, it.id from item_donation itd inner join item it on itd.item_id = it.id inner join donation d on d.id = itd.donation_id inner join distribution_center dc on dc.id = d.distribution_center_id where it.name = (?) group by it.id, dc.name, it.attributes - 'validade' order by quantity desc";

        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<ItemDonation>> items = new LinkedHashMap<>();
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, name);
            result = statement.executeQuery();
            
            while (result.next()) {
                var item = new Item();
                var itemDonation = new ItemDonation();

                var attributes = result.getString("attributes");
                Map<String, String> attributesMap = mapper.readValue(attributes, new TypeReference<Map<String,String>>() {
                    
                });

                Map<String, String> orderedAttributesMap = new TreeMap<>();
                var treeSet = new TreeSet<>(attributesMap.keySet());

                treeSet.forEach(key -> {
                    orderedAttributesMap.put(key, attributesMap.get(key));
                });


                item.setId(result.getObject("id", UUID.class));
                item.setAttributes(orderedAttributesMap);

                itemDonation.setItem(item);
                itemDonation.setQuantity(result.getInt("quantity"));

                var distributionCenterName = result.getString("name");
                if (items.containsKey(distributionCenterName)) {
                    var list = items.get(distributionCenterName);

                    list.add(itemDonation);

                    items.put(distributionCenterName, list);
                } else {
                    items.put(distributionCenterName, List.of(itemDonation));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os items");
        } catch (JsonProcessingException e) {
            throw new RepositoryException("ocorreu ao converter atributos");
        }finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return items;
    }
}
