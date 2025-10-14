package br.com.erikferreira.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;
@Data
public class BlockEntity {
    
    private Long id;
    private OffsetDateTime blockedAt;
    private String blockedReason;
    private OffsetDateTime unblockedAt;
    private String unblockedReason;

}
