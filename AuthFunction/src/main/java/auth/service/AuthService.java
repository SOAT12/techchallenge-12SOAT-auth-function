package auth.service;

import auth.dto.LoginRequestDto;
import auth.dto.LoginResponseDto;
import auth.exception.BadRequestException;
import auth.exception.ForbiddenException;

public class AuthService {

    private final JwtService jwtService;
    private final ServiceOrderService serviceOrderService;

    public AuthService(JwtService jwtService, ServiceOrderService serviceOrderService) {
        this.jwtService = jwtService;
        this.serviceOrderService = serviceOrderService;
    }

    public LoginResponseDto generateAuthToken(LoginRequestDto requestDto) {
        String document = requestDto.document();

        if (document == null || document.isBlank()) {
            throw new BadRequestException("Documento inválido");
        }

        if (!serviceOrderService.hasActiveServiceOrder(document)) {
            throw new ForbiddenException("Cliente não possui OS ativa");
        }

        String token = jwtService.generateToken(document);
        return new LoginResponseDto(token);
    }

}
