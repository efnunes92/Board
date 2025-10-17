package br.com.erikferreira.persistence.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(Timestamp value) {
        return OffsetDateTime.ofInstant(value.toInstant(), UTC);
    }
}
