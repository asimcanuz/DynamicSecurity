package org.asodev.dynamicsecurity.payload.response;

public record TokenVerificationResponse(boolean valid, String message) {
}