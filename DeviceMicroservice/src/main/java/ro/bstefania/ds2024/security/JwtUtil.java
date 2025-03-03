package ro.bstefania.ds2024.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "my_super_secret_key_which_is_very_long";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    //generate JWT Token
    public String generateToken(String username, boolean isAdmin) {
        Map<String, Object> claims = new HashMap<>();
        if (isAdmin) {
            claims.put("roles", List.of("ROLE_ADMIN"));
        } else {
            claims.put("roles", List.of("ROLE_USER"));
        }        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //extract username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    public boolean extractIsAdmin(String token) {
        return extractClaims(token).get("isAdmin", boolean.class);
    }


    //validate token
    public boolean validateToken(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    //extract claims
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return claims.get("roles", List.class); // Ensure roles are stored as a List
    }

    //check expiration
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
