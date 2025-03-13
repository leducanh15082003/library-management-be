package se2.group6.librarymanagement.dto;

public class ForgotPasswordResponseDTO {
    private String newPassword;

    public ForgotPasswordResponseDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}