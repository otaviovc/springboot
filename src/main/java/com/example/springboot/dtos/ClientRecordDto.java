package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

public record ClientRecordDto(@NotBlank String name, @NotBlank String login, @NotBlank String email) {
}
