package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.*;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<ItemDonation> findAllByDistributionCenter(DistributionCenter distributionCenter) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "SELECT it.name AS item_type_name, i.name AS item_name, i.attributes - 'validade' AS attributes, SUM(idn.quantity) AS total_quantity FROM item i JOIN item_type it ON i.item_type_id = it.id JOIN item_donation idn ON i.id = idn.item_id JOIN donation d ON idn.donation_id = d.id WHERE d.distribution_center_id = (?) GROUP BY it.name, i.name, i.attributes ORDER BY it.name, i.name";

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

                result.getObject("attributes", Map.class).forEach((index, value) -> {
                    item.setAttribute((String) index, (String) value);
                });

                itemDonation.setDonation(donation);
                itemDonation.setItem(item);
                itemDonation.setQuantity(result.getInt("total_quantity"));

                itemDonations.add(itemDonation);
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os items");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return itemDonations;
    }
}
