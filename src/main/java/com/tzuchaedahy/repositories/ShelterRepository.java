package com.tzuchaedahy.repositories;

import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.exceptions.RepositoryException;
import com.tzuchaedahy.repositories.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            throw new RepositoryException("ocorreu um erro ao salvar um abrigo");
        } finally {
            Db.closeStatement(statement);
        }
    }

    public List<Shelter> findAll() {
        Statement statement = null;
        ResultSet result = null;

        String query = "select * from shelter";
        
        List<Shelter> shelters = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                Shelter shelter = new Shelter();
            
                shelter.setId(result.getObject("id", UUID.class));
                shelter.setName(result.getString("name"));
                shelter.setEmail(result.getString("email"));
                shelter.setAddress(result.getString("address"));
                shelter.setResponsible(result.getString("responsible"));
                shelter.setPhone(result.getString("phone"));
                shelter.setOccupation(result.getInt("occupation"));
                shelter.setCapacity(result.getInt("capacity"));

                shelters.add(shelter);
            }
        } catch (SQLException e) {
            throw new RepositoryException("ocorreu um erro ao buscar todos os abrigos");
        }

        return shelters;
    }
}
