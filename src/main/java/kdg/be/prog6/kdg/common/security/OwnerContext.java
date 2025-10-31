package kdg.be.prog6.kdg.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OwnerContext {
    public UUID getOwnerId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("No authenticated owner");
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        String sub = jwt.getClaimAsString("sub");  // Keycloak subject (user ID)

        return UUID.fromString(sub);
    }

    /**
     * Get owner's email from JWT
     */
    public String getOwnerEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("No authenticated owner");
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaimAsString("email");
    }

    /**
     * Get owner's username from JWT
     */
    public String getOwnerUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("No authenticated owner");
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaimAsString("preferred_username");
    }
}
