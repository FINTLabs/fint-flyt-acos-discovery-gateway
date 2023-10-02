package no.fintlabs;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class SourceApplicationAuthorizationUtil {
    public Long getSourceApplicationId(Authentication authentication) {
        return no.fintlabs.resourceserver.security.client.sourceapplication.SourceApplicationAuthorizationUtil.getSourceApplicationId(authentication);
    }
}
