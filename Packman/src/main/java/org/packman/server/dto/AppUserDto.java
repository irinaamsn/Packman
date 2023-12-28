package org.packman.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private String username;
    private Integer bestPoints;
    @Override
    public String toString() {
        return username + "," + bestPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserDto that = (AppUserDto) o;
        return Objects.equals(username, that.username) && Objects.equals(bestPoints, that.bestPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, bestPoints);
    }
}
