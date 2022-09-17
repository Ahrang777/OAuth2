package study.oAuth.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth2")
public class OAuth2SignController {

    @GetMapping("/redirect")
    public String redirect(@RequestParam(required = false) String accessToken, @RequestParam(required = false) String refreshToken) {

        log.info("access token : {}", accessToken);
        log.info("refresh token : {}", refreshToken);

        return "ok";
    }
}
