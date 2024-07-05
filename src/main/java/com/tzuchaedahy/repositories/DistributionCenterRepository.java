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
}
