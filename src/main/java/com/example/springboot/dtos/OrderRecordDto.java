package com.example.springboot.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRecordDto(@NotNull UUID clientId, @NotNull List<UUID> productIds) {
}
