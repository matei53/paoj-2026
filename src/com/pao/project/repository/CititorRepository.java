package com.pao.project.repository;

import com.pao.project.model.Cititor;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CititorRepository implements Repository<Cititor, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Cititor mapRow(ResultSet rs) throws SQLException {
        Cititor c = new Cititor();
        c.setId(rs.getLong("id"));
        c.setNume(rs.getString("nume"));
        c.setEmail(rs.getString("email"));
        return c;
    }

    @Override
    public void save(Cititor cititor) throws SQLException {
        String sql = "INSERT INTO cititor (nume, email) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cititor.getNume());
            ps.setString(2, cititor.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cititor.setId(keys.getLong(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException("Eroare la obtinerea conexiunii: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cititor> findById(Long id) throws SQLException {
        String sql = "SELECT id, nume, email FROM cititor WHERE id = ?";
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
    public List<Cititor> findAll() throws SQLException {
        String sql = "SELECT id, nume, email FROM cititor ORDER BY id";
        List<Cititor> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Cititor cititor) throws SQLException {
        String sql = "UPDATE cititor SET nume = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cititor.getNume());
            ps.setString(2, cititor.getEmail());
            ps.setLong(3, cititor.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM cititor WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}