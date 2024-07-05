package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DonationRepository {
    private Connection conn;

    public DonationRepository() {
        this.conn = Db.getConnection();
    }

    public void save(Donation donation) {
        PreparedStatement statement = null;

        String query = "insert into donation (distribution_center_id) values (?)";

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, donation.getDistributionCenter().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                var result = statement.getGeneratedKeys();
                if(result.next()) {
                    donation.setId(result.getObject(1, UUID.class));
                }
            } else {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar uma doaçao" + e.getMessage());
        }

    }
}
