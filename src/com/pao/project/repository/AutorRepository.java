package com.pao.project.repository;

import com.pao.project.model.Autor;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorRepository implements Repository<Autor, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Autor mapRow(ResultSet rs) throws SQLException {
        Autor a = new Autor();
        a.setId(rs.getLong("id"));
        a.setNume(rs.getString("nume"));
        return a;
    }

    @Override
    public void save(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nume) VALUES (?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, autor.getNume());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    autor.setId(keys.getLong(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException("Eroare la obtinerea conexiunii: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Autor> findById(Long id) throws SQLException {
        String sql = "SELECT id, nume FROM autor WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Autor> findAll() throws SQLException {
        String sql = "SELECT id, nume FROM autor ORDER BY id";
        List<Autor> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Autor autor) throws SQLException {
        String sql = "UPDATE autor SET nume = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, autor.getNume());
            ps.setLong(2, autor.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}