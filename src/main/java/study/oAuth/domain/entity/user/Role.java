package study.oAuth.domain.entity.user;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_GUEST("ROLE_GUEST"),
    ROLE_USER("ROLE_USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
