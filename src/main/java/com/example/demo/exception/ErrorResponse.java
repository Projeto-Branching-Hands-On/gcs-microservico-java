package com.example.demo.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String mensagem;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMensagem() { return mensagem; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

