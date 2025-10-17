package br.com.erikferreira.dto;

import java.time.OffsetDateTime;

public record CardDetails(Long id,
                          String title,
                          String description,
                          boolean blocked,
                          OffsetDateTime blockedAt,
                          String blockReason,
                          int blocksAmount,
                          Long columnId,
                          String columnName) {
}
