package pillihuaman.com.pe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.security.dto.ResponseUser;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.util.MyJsonWebToken;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${app.jwt.default-tenant-id-for-testing:#{null}}")
    private String defaultTenantIdForTesting;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        extraClaims.putAll(createClaimsFromUser(userDetails));
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(User userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> claims, User userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey()) // Uses the correct signing key
                    .build()
                    .parseClaimsJws(token) // Throws an exception if the signature is invalid
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token has expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token format: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying JWT token: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> createClaimsFromUser(User userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("El objeto User no puede ser nulo.");
        }

        Map<String, Object> claims = new HashMap<>();

        // 1. Poblar datos del usuario
        Map<String, Object> userMap = new HashMap<>();
        if (userDetails.getId() != null) {
            userMap.put("id", userDetails.getId().toHexString());
        }
        userMap.put("mobilPhone", userDetails.getMobilPhone());
        userMap.put("email", userDetails.getEmail());
        userMap.put("alias", userDetails.getAlias());

        // 2. Poblar otros datos
        Map<String, Object> applicationMap = new HashMap<>();
        applicationMap.put("applicationID", "1");

        claims.put("user", userMap);
        claims.put("application", applicationMap);
        claims.put("role", userDetails.getRoles());

        // 3. Lógica de Tenant ID segura, asumiendo que getTenantId() devuelve ObjectId
        String tenantId = userDetails.getTenantId();
        String finalTenantId;

        if (tenantId != null) {
            // Caso Normal: El usuario tiene un tenantId válido. Se convierte a String.
            finalTenantId = tenantId;
        } else {
            // Caso de Fallback: El usuario NO tiene tenantId.
            if (defaultTenantIdForTesting != null && !defaultTenantIdForTesting.isBlank()) {
                finalTenantId = defaultTenantIdForTesting;
            } else {
                throw new IllegalStateException("Error crítico: El usuario " + userDetails.getUsername() + " no tiene un tenantId asignado.");
            }
        }

        // 4. Añadir el tenantId al nivel raíz de los claims (UNA SOLA VEZ).
        claims.put("tenantId", finalTenantId);

        return claims;
    }

    public MyJsonWebToken parseTokenToMyJsonWebToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = extractAllClaims(token);

        MyJsonWebToken myJsonWebToken = new MyJsonWebToken();
        Map<String, Object> userMap = (Map<String, Object>) claims.get("user");
        String tenantIdFromToken = claims.get("tenantId", String.class);

        if (userMap != null) {
            ResponseUser user = new ResponseUser();

            Object rawId = userMap.get("id");
            if (rawId != null) user.setId(new ObjectId(rawId.toString()));

            Object rawEmail = userMap.get("email");
            if (rawEmail != null) user.setMail(rawEmail.toString());

            // Asignar el tenantId leído desde el nivel raíz de los claims
            user.setTenantId(tenantIdFromToken);

            myJsonWebToken.setUser(user);
        }
        return myJsonWebToken;
    }
}
