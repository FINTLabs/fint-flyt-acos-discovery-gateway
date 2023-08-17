package no.fintlabs;

import no.fintlabs.resourceserver.security.client.ClientAuthorizationUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class ClientAuthorizationService {
    public Long getSourceApplicationId(Authentication authentication) {
        return ClientAuthorizationUtil.getSourceApplicationId(authentication);
    }
}
