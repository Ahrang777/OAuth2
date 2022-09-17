package study.oAuth.domain.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.oAuth.domain.entity.user.Role;
import study.oAuth.domain.entity.user.Token;
import study.oAuth.domain.entity.user.User;
import study.oAuth.domain.repository.TokenRepository;
import study.oAuth.domain.repository.UserRepository;
import study.oAuth.domain.service.dto.TokenDto;
import study.oAuth.payload.request.SignUpRequest;
import study.oAuth.payload.request.SigninRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public Long signup(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        String name = signUpRequest.getName();

        if (userRepository.existsByEmail(email)) {
            throw new UsernameNotFoundException(email + "이 이미 존재합니다.");
        }

        User user = User.createUser(email, name, password, Role.ROLE_USER);
        userRepository.save(user);

        return user.getId();
    }

    public TokenDto signin(SigninRequest signinRequest) {
        String email = signinRequest.getEmail();
        String password = signinRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = customTokenProviderService.createTokenDto(authentication);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + "찾을 수 없습니다."));

        Token token = Token.builder().key(user.getId()).refreshToken(tokenDto.getRefreshToken()).build();
        tokenRepository.save(token);

        return tokenDto;
    }
}
