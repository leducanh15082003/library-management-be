package se2.group6.librarymanagement.dto;

public class JwtAuthenticationResponseDTO {
    private String token;

    public JwtAuthenticationResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
