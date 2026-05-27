package com.pao.project.repository;

import com.pao.project.model.Carte;
import com.pao.project.model.Exemplar;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarteRepository implements Repository<Carte, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Carte mapRow(ResultSet rs) throws SQLException {
        AutorRepository autorRepo = new AutorRepository();
        CategorieRepository categRepo = new CategorieRepository();
        Carte c = new Carte();
        c.setId(rs.getLong("id"));
        c.setTitlu(rs.getString("titlu"));
        if (rs.getObject("an_lansare") != null)
            c.setAnLansare(rs.getInt("an_lansare"));
        else
            c.setAnLansare(null);
        if (rs.getObject("id_autor") != null)
            c.setAutor(autorRepo.findById(rs.getLong("id_autor")).get());
        else
            c.setAutor(null);
        c.setCategorie(categRepo.findById(rs.getLong("id_categorie")).get());
        c.setNrPagini(rs.getInt("nr_pagini"));

        //lista de exemplare
        String sql = "SELECT id, cod FROM exemplar ORDER BY cod";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs2 = ps.executeQuery()) {
            while (rs2.next())
                c.adaugaExemplar(new Exemplar(rs2.getLong("id"), rs2.getInt("cod")));
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return c;
    }

    @Override
    public void save(Carte carte) throws SQLException {
        try {
            Connection conn = getConn();
            String sql = "INSERT INTO carte (titlu, an_lansare, id_autor, id_categorie, nr_pagini) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, carte.getTitlu());
                if (carte.getAnLansare() != null)
                    ps.setInt(2, carte.getAnLansare());
                else
                    ps.setNull(2, Types.INTEGER);
                if (carte.getAutor() != null)
                    ps.setLong(3, carte.getAutor().getId());
                else
                    ps.setNull(3, Types.INTEGER);
                ps.setLong(4, carte.getCategorie().getId());
                ps.setInt(5, carte.getNrPagini());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        carte.setId(keys.getLong(1));
                    }
                }
            }

            for (Exemplar ex : carte.getExemplare()) {
                String sql2 = "INSERT INTO exemplar (cod, id_carte) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql2,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, ex.getCod());
                    ps.setLong(2, carte.getId());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            ex.setId(keys.getLong(1));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Eroare la obtinerea conexiunii: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Carte> findById(Long id) throws SQLException {
        String sql = "SELECT id, titlu, an_lansare, id_autor, id_categorie, nr_pagini FROM carte WHERE id = ?";
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
    public List<Carte> findAll() throws SQLException {
        String sql = "SELECT id, titlu, an_lansare, id_autor, id_categorie, nr_pagini FROM carte ORDER BY id";
        List<Carte> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Carte carte) throws SQLException {
        try {
            Connection conn = getConn();
            String sql = "UPDATE carte SET titlu = ?, an_lansare = ?, id_autor = ?, id_categorie = ?, nr_pagini = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, carte.getTitlu());
                if (carte.getAnLansare() != null)
                    ps.setInt(2, carte.getAnLansare());
                else
                    ps.setNull(2, Types.INTEGER);
                if (carte.getAutor() != null)
                    ps.setLong(3, carte.getAutor().getId());
                else
                    ps.setNull(3, Types.INTEGER);
                ps.setLong(4, carte.getCategorie().getId());
                ps.setInt(5, carte.getNrPagini());
                ps.setLong(6, carte.getId());
                ps.executeUpdate();
            }

            for (Exemplar ex : carte.getExemplare()) {
                if (ex.getId() != 0L) {
                    String sql2 = "UPDATE exemplar SET cod = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql2,
                            Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, ex.getCod());
                        ps.setLong(2, ex.getId());
                        int randuriModificate = ps.executeUpdate();
                        if (randuriModificate == 0) {
                            String sql3 = "INSERT INTO exemplar (cod, id_carte) VALUES (?, ?)";
                            try (PreparedStatement ps2 = conn.prepareStatement(sql3,
                                    Statement.RETURN_GENERATED_KEYS)) {
                                ps2.setInt(1, ex.getCod());
                                ps2.setLong(2, carte.getId());
                                ps2.executeUpdate();
                                try (ResultSet keys = ps2.getGeneratedKeys()) {
                                    if (keys.next()) {
                                        ex.setId(keys.getLong(1));
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    String sql2 = "INSERT INTO exemplar (cod, id_carte) VALUES (?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql2,
                            Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, ex.getCod());
                        ps.setLong(2, carte.getId());
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (keys.next()) {
                                ex.setId(keys.getLong(1));
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            throw new SQLException("Eroare la obtinerea conexiunii: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM carte WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}