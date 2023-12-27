package org.packman.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
