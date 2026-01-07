package auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

public class SecretsManagerUtil {

    private static final SecretsManagerClient client = SecretsManagerClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, String> getDbCredentials(String secretName) {
        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse response = client.getSecretValue(request);
        try {
            return mapper.readValue(response.secretString(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler secret JSON", e);
        }
    }

}
