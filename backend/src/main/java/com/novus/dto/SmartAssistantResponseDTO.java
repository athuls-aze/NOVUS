package com.novus.dto;

public class SmartAssistantResponseDTO {
    private String reply;

    public SmartAssistantResponseDTO(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
