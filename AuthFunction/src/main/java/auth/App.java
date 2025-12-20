package auth;

import java.util.HashMap;
import java.util.Map;

import auth.dto.ErrorResponseDto;
import auth.exception.BadRequestException;
import auth.exception.ForbiddenException;
import auth.service.AuthService;
import auth.service.JwtService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import auth.dto.LoginRequestDto;
import auth.dto.LoginResponseDto;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper;
    private final AuthService authService;

    static {
        objectMapper = new ObjectMapper();
    }

    public App() {
        JwtService jwtService = new JwtService();
        this.authService = new AuthService(jwtService);
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            LoginRequestDto requestDto = this.buildLoginRequestDto(input.getBody());
            LoginResponseDto loginResponseDto = authService.generateAuthToken(requestDto);
            String responseDto = this.buildResponse(loginResponseDto);

            return response
                    .withStatusCode(200)
                    .withBody(responseDto);
        } catch (BadRequestException e) {
            context.getLogger().log(e.getMessage());
            return response
                    .withStatusCode(400)
                    .withBody(buildErrorResponse(e.getMessage()));
        } catch (ForbiddenException e) {
            context.getLogger().log(e.getMessage());
            return response
                    .withStatusCode(403)
                    .withBody(buildErrorResponse(e.getMessage()));
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
            return response
                    .withStatusCode(500)
                    .withBody(buildErrorResponse("Algo inesperado aconteceu"));
        }

    }

    private LoginRequestDto buildLoginRequestDto(String body) {
        try {
            return objectMapper.readValue(body, LoginRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildResponse(LoginResponseDto loginResponseDto) {
        try {
            return objectMapper.writeValueAsString(loginResponseDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildErrorResponse(String message) {
        try {
            return objectMapper.writeValueAsString(
                    new ErrorResponseDto(message)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
