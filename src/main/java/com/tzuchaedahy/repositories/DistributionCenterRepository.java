package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistributionCenterRepository {
    private final Connection conn;

    public DistributionCenterRepository() {
        this.conn = Db.getConnection();
    }

    public List<DistributionCenter> findAll() {
        Statement statement = null;
        ResultSet result = null;

        String query = "select * from distribution_center";

        List<DistributionCenter> distributionCenters = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                DistributionCenter distributionCenter = new DistributionCenter();

                distributionCenter.setId(UUID.fromString(result.getString("id")));
                distributionCenter.setName(result.getString("name"));
                distributionCenter.setAddress(result.getString("address"));

                distributionCenters.add(distributionCenter);
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os centros de distribuicao");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return distributionCenters;
    }

    public DistributionCenter findByName(String name) {
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = "select * from distribution_center where name = ?";

        DistributionCenter distributionCenter = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, name);

            result = statement.executeQuery();
            if (result.next()) {
                distributionCenter = new DistributionCenter();
                distributionCenter.setId(result.getObject("id", UUID.class));
                distributionCenter.setName(result.getString("name"));
                distributionCenter.setAddress(result.getString("address"));
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar um centro de distribuicao");
        } finally {
            Db.closeStatement(statement);
            Db.closeResultSet(result);
        }

        return distributionCenter;
    }
}
