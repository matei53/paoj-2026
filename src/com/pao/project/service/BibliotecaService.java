package com.pao.project.service;

import com.pao.project.model.*;
import com.pao.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaService {
    private static BibliotecaService instance;

    private BibliotecaService() {}

    public static BibliotecaService getInstance() {
        if (instance == null) instance = new BibliotecaService();
        return instance;
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // imprumuta o carte unui cititor
    public long imprumutaCarte(long id_cititor, long id_carte, LocalDate data_scadenta) throws SQLException, IOException {
        Connection conn = getConn();
        try {
            // disponibilitate carte
            String sql1 = """
                    SELECT e.id AS id_exemplar, e.cod AS cod_exemplar
                    FROM exemplar e
                    LEFT JOIN imprumut i ON i.id_exemplar = e.id AND i.data_returnare IS NULL
                    WHERE i.id IS NULL AND e.id_carte = ?
                    ORDER BY cod_exemplar
                    """;
            List<Exemplar> exemplare = new ArrayList<>();
            try (PreparedStatement ps = getConn().prepareStatement(sql1)) {
                ps.setLong(1, id_carte);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        exemplare.add(new Exemplar(rs.getLong("id_exemplar"), rs.getInt("cod_exemplar")));
                    }
                }
            }
            if (exemplare.isEmpty()) {
                throw new SQLException("Cartea cu id=" + id_carte + " nu este disponibila.");
            }

            // inserare imprumut
            String sql2 = """
                    INSERT INTO imprumut
                        (id_cititor, id_exemplar, data_imprumut, data_scadenta, data_returnare)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            long id_imprumut = 0L;
            try (PreparedStatement ps = conn.prepareStatement(sql2,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, id_cititor);
                ps.setLong(2, exemplare.getFirst().getId());
                ps.setString(3, String.valueOf(LocalDate.now()));
                ps.setString(4, String.valueOf(data_scadenta));
                ps.setNull(5, Types.VARCHAR);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        id_imprumut = keys.getLong(1);
                    }
                }
            }
            System.out.println("Imprumutare facuta cu succes. ID=" + id_imprumut);
            return id_imprumut;
        } catch (SQLException e) {
            System.out.println("Imprumutare esuata datorita: " + e.getMessage());
            throw e;
        }
    }

    // returneaza o carte, setand si penalizari - tranzactie explicita
    public void returneazaCarte(long id_imprumut, double nivel_deteriorare) throws SQLException, IOException {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            // update data returnare
            String sql = "UPDATE imprumut SET data_returnare = ? where id = ?";
            try (PreparedStatement updatePs = conn.prepareStatement(sql)) {
                updatePs.setString(1, String.valueOf(LocalDate.now()));
                updatePs.setLong(2, id_imprumut);
                int randuri = updatePs.executeUpdate();
                if (randuri == 0) throw new SQLException("Imprumutul cu id=" + id_imprumut + " nu exista.");
            }

            // gasim data_scadenta
            String data_scadenta;
            String sql2 = "SELECT data_scadenta FROM imprumut WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                ps.setLong(1, id_imprumut);
                try (ResultSet rs = ps.executeQuery()) {
                    data_scadenta = rs.getString("data_scadenta");
                }
            }

            // adauga penalizare de intarziere daca data_returnare depaseste data_scadenta
            if (LocalDate.parse(data_scadenta).isBefore(LocalDate.now())) {
                String sql3 = "INSERT INTO penalizare_intarziere (id_imprumut, zile_intarziere) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql3,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, id_imprumut);
                    ps.setDouble(2, ChronoUnit.DAYS.between(LocalDate.parse(data_scadenta), LocalDate.now()));
                    ps.executeUpdate();
                }
            }

            // adauga penalizare de deteriorare daca nivel_deteriorare != 0
            if (nivel_deteriorare != 0) {
                String sql3 = "INSERT INTO penalizare_deteriorare (id_imprumut, nivel_deteriorare) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql3,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, id_imprumut);
                    ps.setDouble(2, nivel_deteriorare);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Returnare facuta cu succes");
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Returnare esuata datorita: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void returneazaCarte(long id_imprumut) throws SQLException, IOException {
        returneazaCarte(id_imprumut, 0);
    }


    // istoricul imprumuturilor unui cititor - join
    public List<String> istoricImprumut(long id_cititor) throws SQLException, IOException {
        String sql = """
            SELECT i.id, cit.nume, car.titlu, ex.cod, i.data_imprumut, i.data_scadenta,
                   IFNULL(i.data_returnare, 'nereturnat') AS returnare,
                   IFNULL(pd.suma_deteriorare, 0) AS deteriorare,
                   IFNULL(pi.suma_intarziere, 0) AS intarziere
            FROM imprumut i
            JOIN cititor cit ON i.id_cititor = cit.id
            JOIN exemplar ex ON i.id_exemplar = ex.id
            JOIN carte car ON ex.id_carte = car.id
            LEFT JOIN (
                SELECT id_imprumut, 10 * nivel_deteriorare AS suma_deteriorare
                FROM penalizare_deteriorare
            ) pd ON pd.id_imprumut = i.id
            LEFT JOIN (
                SELECT id_imprumut, 1.5 * zile_intarziere AS suma_intarziere
                FROM penalizare_intarziere
            ) pi ON pi.id_imprumut = i.id
            WHERE cit.id = ?
            ORDER BY i.data_imprumut;
            """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql))
        {
            ps.setLong(1, id_cititor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("IMPRUMUT ID: %d\n" +
                                    "- cititor: %s\n- carte: %s\n- exemplar: %d\n" +
                                    "- data imprumut: %s\n- data scandenta: %s\n- data returnare: %s\n" +
                                    "- pen. deteriorare: %.2f\n - pen. intarziere: %.2f",
                            rs.getLong(1), rs.getString(2), rs.getString(3),
                            rs.getInt(4), rs.getString(5), rs.getString(6),
                            rs.getString(7), rs.getDouble(8), rs.getDouble(9)));
                }
            }
        }
        return results;
    }

    // top 5 carti imprumutate - join
    public List<String> topCartiImprumutate() throws SQLException, IOException {
        String sql = """
                SELECT c.titlu, a.nume AS nume_autor, COUNT(i.id) AS nr_imprumuturi
                FROM autor a
                RIGHT JOIN carte c ON a.id = c.id_autor
                LEFT JOIN exemplar e ON c.id = e.id_carte
                LEFT JOIN imprumut i ON e.id = i.id_exemplar
                GROUP BY c.id, c.titlu, a.nume
                ORDER BY nr_imprumuturi DESC
                LIMIT 5
                """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(String.format("'%s' de %s — %d imprumuturi",
                        rs.getString("titlu"),
                        rs.getString("nume_autor"),
                        rs.getLong("nr_imprumuturi")));
            }
        }
        return results;
    }

    // disponibilitatea unei carti - join
    public List<Exemplar> exemplareDisponibile(long id_carte) throws SQLException, IOException {
        String sql = """
                SELECT e.id AS id_exemplar, e.cod AS cod_exemplar
                FROM exemplar e
                LEFT JOIN imprumut i ON i.id_exemplar = e.id AND i.data_returnare IS NULL
                WHERE i.id IS NULL AND e.id_carte = ?
                ORDER BY cod_exemplar
                """;
        List<Exemplar> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id_carte);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Exemplar(rs.getLong("id_exemplar"), rs.getInt("cod_exemplar")));
                }
            }
        }
        return results;
    }
}
