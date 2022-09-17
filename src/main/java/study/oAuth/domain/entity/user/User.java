package study.oAuth.domain.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oAuth.domain.entity.time.DefaultTime;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends DefaultTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String providerId;

    public static User createUser(String email, String name, String password, Role role) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.password = password;
        user.role = role;
        return user;
    }

    @Builder
    public User(String name, String email, String password, Role role, Provider provider, String providerId){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
