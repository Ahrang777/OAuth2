package study.oAuth.config.security.auth.company;

import study.oAuth.config.security.auth.OAuth2UserInfo;
import study.oAuth.domain.entity.user.Provider;

import java.util.Map;

public class Facebook extends OAuth2UserInfo {
    public Facebook(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public String getProvider(){
        return Provider.facebook.toString();
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
