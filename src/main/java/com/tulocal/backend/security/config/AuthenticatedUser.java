package com.tulocal.backend.security.config;
 
import com.tulocal.backend.common.exception.AppException;
import com.tulocal.backend.security.jwt.TokenPayload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
 
import java.util.UUID;
 
/**
 * Helper estático para obtener el usuario autenticado en cualquier lugar
 * Equivalente a req.user en Node.js
 */
public class AuthenticatedUser {
 
    private AuthenticatedUser() {}
 
    /**
     * Retorna el TokenPayload del usuario autenticado
     * Equivalente a req.user en los controllers de Node
     */
    public static TokenPayload get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof TokenPayload payload)) {
            throw AppException.unauthorized("Usuario no autenticado");
        }
        return payload;
    }
 
    public static UUID getUserId() {
        return UUID.fromString(get().getUserId());
    }
 
    public static String getRole() {
        return get().getRole();
    }
}
 