package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ShelterRepository {
    private Connection conn;

    public ShelterRepository() {
        this.conn = Db.getConnection();
    }

    public void save(Shelter shelter) {
        PreparedStatement statement = null;

        String query = "insert into shelter (name, address, responsible, phone, email, capacity, occupation) values (?, ?, ?, ?, ?, ?, ?)";

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, shelter.getName());
            statement.setString(2, shelter.getAddress());
            statement.setString(3, shelter.getResponsible());
            statement.setString(4, shelter.getPhone());
            statement.setString(5, shelter.getEmail());
            statement.setInt(6, shelter.getCapacity());
            statement.setInt(7, shelter.getOccupation());

            if (statement.executeUpdate() < 1) {
                throw new RepositoryException("nenhuma linha do banco de dados foi alterada");
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao salvar um item");
        } finally {
            Db.closeStatement(statement);
        }
    }
}
