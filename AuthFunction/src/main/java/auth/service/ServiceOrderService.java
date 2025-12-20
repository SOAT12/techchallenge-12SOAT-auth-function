package auth.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServiceOrderService {

    private static final String SQL = """
        SELECT 1
        FROM service_order
        WHERE document = ?
          AND status = 'OPEN'
        LIMIT 1
        """;

    public boolean hasActiveServiceOrder(String document) {
        try (
                Connection conn = DriverManager.getConnection(
                        System.getenv("DB_URL"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD")
                );
                PreparedStatement ps = conn.prepareStatement(SQL)
        ) {
            ps.setString(1, document);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar OS no banco", e);
        }
    }

}
