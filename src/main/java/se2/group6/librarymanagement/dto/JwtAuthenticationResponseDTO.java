package se2.group6.librarymanagement.dto;

public class JwtAuthenticationResponseDTO {
    private String token;
    private String role;
    private String username;

    public JwtAuthenticationResponseDTO(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public JwtAuthenticationResponseDTO(String token, String role, String username) {
        this.token = token;
        this.role = role;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
