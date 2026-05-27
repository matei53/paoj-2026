package com.pao.project.repository;

import com.pao.project.model.*;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImprumutRepository implements Repository<Imprumut, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Imprumut mapRow(ResultSet rs) throws SQLException {
        Imprumut i = new Imprumut();
        CititorRepository cititorRepo = new CititorRepository();
        CarteRepository carteRepo = new CarteRepository();
        i.setId(rs.getLong("id"));
        i.setCititor(cititorRepo.findById(rs.getLong("id_cititor")).get());
        i.setCarte(carteRepo.findById(rs.getLong("id_carte")).get());
        i.setDataImprumut(LocalDate.parse(rs.getString("data_imprumut")));
        i.setDataScadenta(LocalDate.parse(rs.getString("data_scadenta")));
        i.setDataReturnare(LocalDate.parse(rs.getString("data_returnare"))); // poate fi null

        // exemplar
        String sql = "SELECT id, cod FROM exemplar WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, rs.getLong("id_exemplar"));
            try (ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    Exemplar exemplar = new Exemplar(rs2.getLong("id"), rs2.getInt("cod"));
                    i.setExemplar(exemplar);
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        // penalizari
        String sql2 = "SELECT id, nivel_deteriorare FROM penalizare_deteriorare WHERE id_imprumut = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql2)) {
            ps.setLong(1, rs.getLong("id"));
            try (ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    Penalizare penalizare = new PenalizareDeteriorare(
                            rs2.getLong("id"), rs2.getDouble("nivel_deteriorare"));
                    i.adaugaPenalizare(penalizare);
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        String sql3 = "SELECT id, zile_intarziere FROM penalizare_intarziere WHERE id_imprumut = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql3)) {
            ps.setLong(1, rs.getLong("id"));
            try (ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    Penalizare penalizare = new PenalizareIntarziere(
                            rs2.getLong("id"), rs2.getInt("zile_intarziere"));
                    i.adaugaPenalizare(penalizare);
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return i;
    }

    @Override
    public void save(Imprumut imprumut) throws SQLException {
        try {
            Connection conn = getConn();
            String sql = "INSERT INTO imprumut (id_cititor, id_exemplar, data_imprumut, data_scadenta, data_returnare) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, imprumut.getCititor().getId());
                ps.setLong(2, imprumut.getExemplar().getId());
                ps.setString(3, String.valueOf(imprumut.getDataImprumut()));
                ps.setString(4, String.valueOf(imprumut.getDataScadenta()));
                ps.setString(5, String.valueOf(imprumut.getDataReturnare()));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        imprumut.setId(keys.getLong(1));
                    }
                }
            }

            // penalizari
            for (Penalizare p : imprumut.getPenalizari()) {
                if (p.getTip().equals("deteriorare")) {
                    PenalizareDeteriorare pd = (PenalizareDeteriorare) p;
                    String sql2 = "INSERT INTO penalizare_deteriorare (id_imprumut, nivel_deteriorare) VALUES (?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql2,
                            Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, imprumut.getId());
                        ps.setDouble(2, pd.getNivelDeteriorare());
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (keys.next()) {
                                pd.setId(keys.getLong(1));
                            }
                        }
                    }
                } else if (p.getTip().equals("intarziere")) {
                    PenalizareIntarziere pi = (PenalizareIntarziere) p;
                    String sql2 = "INSERT INTO penalizare_deteriorare (id_imprumut, zile_intarziere) VALUES (?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql2,
                            Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, imprumut.getId());
                        ps.setDouble(2, pi.getZileIntarziere());
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (keys.next()) {
                                pi.setId(keys.getLong(1));
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
    public Optional<Imprumut> findById(Long id) throws SQLException {
        String sql = "SELECT id, id_cititor, id_exemplar, data_imprumut, data_scadenta, data_returnare FROM imprumut WHERE id = ?";
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
    public List<Imprumut> findAll() throws SQLException {
        String sql = "SELECT id, id_cititor, id_exemplar, data_imprumut, data_scadenta, data_returnare FROM imprumut ORDER BY id";
        List<Imprumut> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Imprumut imprumut) throws SQLException {
        try {
            Connection conn = getConn();
            String sql = "UPDATE imprumut SET id_cititor = ?, id_exemplar = ?, data_imprumut = ?, data_scadenta = ?, data_returnare = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, imprumut.getCititor().getId());
                ps.setLong(2, imprumut.getExemplar().getId());
                ps.setString(3, String.valueOf(imprumut.getDataImprumut()));
                ps.setString(4, String.valueOf(imprumut.getDataScadenta()));
                if (imprumut.getDataReturnare() != null) ps.setString(5, String.valueOf(imprumut.getDataReturnare()));
                else ps.setNull(5, Types.VARCHAR);
                ps.setLong(6, imprumut.getId());
                ps.executeUpdate();
            }

            // penalizari
            for (Penalizare p : imprumut.getPenalizari()) {
                if (p.getTip().equals("deteriorare")) {
                    PenalizareDeteriorare pd = (PenalizareDeteriorare) p;
                    if (p.getId() != 0L) {
                        String sql2 = "UPDATE penalizare_deteriorare SET nivel_deteriorare = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql2,
                                Statement.RETURN_GENERATED_KEYS)) {
                            ps.setDouble(1, pd.getNivelDeteriorare());
                            ps.setLong(2, pd.getId());
                            int randuriModificate = ps.executeUpdate();
                            if (randuriModificate == 0) {
                                String sql3 = "INSERT INTO penalizare_deteriorare (id_imprumut, nivel_deteriorare) VALUES (?, ?)";
                                try (PreparedStatement ps2 = conn.prepareStatement(sql3,
                                        Statement.RETURN_GENERATED_KEYS)) {
                                    ps2.setLong(1, imprumut.getId());
                                    ps2.setDouble(2, pd.getNivelDeteriorare());
                                    ps2.executeUpdate();
                                    try (ResultSet keys = ps2.getGeneratedKeys()) {
                                        if (keys.next()) {
                                            pd.setId(keys.getLong(1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        String sql2 = "INSERT INTO penalizare_deteriorare (id_imprumut, nivel_deteriorare) VALUES (?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(sql2,
                                Statement.RETURN_GENERATED_KEYS)) {
                            ps.setLong(1, imprumut.getId());
                            ps.setDouble(2, pd.getNivelDeteriorare());
                            ps.executeUpdate();
                            try (ResultSet keys = ps.getGeneratedKeys()) {
                                if (keys.next()) {
                                    pd.setId(keys.getLong(1));
                                }
                            }
                        }
                    }
                } else if (p.getTip().equals("intarziere")) {
                    PenalizareIntarziere pi = (PenalizareIntarziere) p;
                    if (pi.getId() != 0L) {
                        String sql2 = "UPDATE penalizare_intarziere SET zile_intarziere = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql2,
                                Statement.RETURN_GENERATED_KEYS)) {
                            ps.setDouble(1, pi.getZileIntarziere());
                            ps.setLong(2, pi.getId());
                            int randuriModificate = ps.executeUpdate();
                            if (randuriModificate == 0) {
                                String sql3 = "INSERT INTO penalizare_deteriorare (id_imprumut, zile_intarziere) VALUES (?, ?)";
                                try (PreparedStatement ps2 = conn.prepareStatement(sql3,
                                        Statement.RETURN_GENERATED_KEYS)) {
                                    ps2.setLong(1, imprumut.getId());
                                    ps2.setDouble(2, pi.getZileIntarziere());
                                    ps2.executeUpdate();
                                    try (ResultSet keys = ps2.getGeneratedKeys()) {
                                        if (keys.next()) {
                                            p.setId(keys.getLong(1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        String sql2 = "INSERT INTO penalizare_deteriorare (id_imprumut, zile_intarziere) VALUES (?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(sql2,
                                Statement.RETURN_GENERATED_KEYS)) {
                            ps.setLong(1, imprumut.getId());
                            ps.setDouble(2, pi.getZileIntarziere());
                            ps.executeUpdate();
                            try (ResultSet keys = ps.getGeneratedKeys()) {
                                if (keys.next()) {
                                    p.setId(keys.getLong(1));
                                }
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
        String sql = "DELETE FROM imprumut WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}