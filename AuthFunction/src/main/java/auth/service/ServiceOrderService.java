package auth.service;

import auth.util.SecretsManagerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class ServiceOrderService {

    private static final String SQL = """
        SELECT 1
        FROM service_order so
        INNER JOIN customer c ON so.customer_id = c.id
        WHERE c.cpf = ?
          AND so.status IN ('OPENED', 'IN_DIAGNOSIS', 'WAITING_FOR_APPROVAL', 'APPROVED', 'WAITING_ON_STOCK', 'IN_EXECUTION', 'FINISHED')
        LIMIT 1
        """;

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public ServiceOrderService() {
        Map<String, String> credentials = SecretsManagerUtil.getDbCredentials("techchallenge-db-credentials");
        this.dbUser = credentials.get("username");
        this.dbPassword = credentials.get("password");
        this.dbUrl = credentials.get("jdbc_url");
    }

    public boolean hasActiveServiceOrder(String document) {
        try (
                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement ps = conn.prepareStatement(SQL)
        ) {
            ps.setString(1, document);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao consultar OS no banco: " + e.getMessage(), e);
        }
    }

}
