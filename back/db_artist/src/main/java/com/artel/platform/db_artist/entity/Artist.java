package com.artel.platform.db_artist.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public record Artist(
        UUID id,
        String email,
        String login,
        ZonedDateTime dateAdd
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Artist artist)) {
            return false;
        }
        return this.email.equals(artist.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }
}
