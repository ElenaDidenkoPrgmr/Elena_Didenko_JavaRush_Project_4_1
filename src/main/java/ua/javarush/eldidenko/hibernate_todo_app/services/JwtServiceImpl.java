package ua.javarush.eldidenko.hibernate_todo_app.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.NoResultException;
import ua.javarush.eldidenko.hibernate_todo_app.entites.UserToken;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.BadTokenException;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.TokenRepository;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.UserRepository;
import ua.javarush.eldidenko.hibernate_todo_app.request.RefreshTokenRequest;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.TokenValidation;
import ua.javarush.eldidenko.hibernate_todo_app.services.token_entity.Tokens;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class JwtServiceImpl implements JwtService {
    private static final String USER_ID_CLAIM = "userId";
    public static final String JWT_SECRET_KEY_ENV = "dcsZL{DSX_(*^@HUDHggdhdh8";
    public static final int ACCESS_TOKEN_EXPIRE_MILLIS = (60 /* mins */ * 60 /* sec in min */ * 1000 /* millisecond in sec */);
    public static final int REFRESH_TOKEN_EXPIRE_MILLIS = (24 /*hours*/ * 60 /* mins */ * 60 /* sec in min */ * 1000 /* millisecond in sec */);

    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    public JwtServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Tokens generateAndSaveTokens(Long userId) {
        String accessToken = createJWT(userId, ACCESS_TOKEN_EXPIRE_MILLIS);
        String refreshToken = createJWT(userId, REFRESH_TOKEN_EXPIRE_MILLIS);

        UserToken userToken = UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(userToken, userId);

        return new Tokens(accessToken, refreshToken);
    }

    /*@Override
    public TokenValidation validateAccessToken(String token) {
        try {
            Claims claims = decodeJWT(token);
            Long userId = claims.get(USER_ID_CLAIM, Long.class);
            Date expToken = claims.get("exp", Date.class);
            String userToken = tokenRepository.fetchAccessTokenByUserId(userId);
            return TokenValidation.builder()
                    .isValid(userToken.equals(token) && tokenNotExpired(expToken))
                    .build();
        } catch (RuntimeException e) {
            return TokenValidation.builder()
                    .isValid(false)
                    .build();
        }
    }*/

    @Override
    public TokenValidation validateAccessTokenByUserId(String token, Long userId) {
        try {
            Claims claims = decodeJWT(token);
            Long tokensUserId = claims.get(USER_ID_CLAIM, Long.class);
            Date expToken = claims.get("exp", Date.class);
            String userToken = tokenRepository.fetchAccessTokenByUserId(userId);
            return TokenValidation.builder()
                    .isValid(userToken.equals(token)
                            && tokenNotExpired(expToken)
                            && userId.equals(tokensUserId))
                    .build();
        } catch (RuntimeException e) {
            return TokenValidation.builder()
                    .isValid(false)
                    .build();
        }
    }

    @Override
    public Tokens refreshTokens(RefreshTokenRequest request) throws BadTokenException {
        if (request.getRefreshToken().isBlank()) {
            throw new BadTokenException("refresh token is not present");
        }

        UserToken userToken;
        try {
            userToken = tokenRepository.fetchByRefreshToken(request.getRefreshToken());
        } catch (NoResultException ex) {
            throw new BadTokenException("Refresh token is not found!");
        }

        Long userId = userToken.getUser().getId();

        Tokens tokens = generateAndSaveTokens(userId);

        return Tokens.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
    }

    private boolean tokenNotExpired(Date dateToConvert) {
        return LocalDateTime.now().isBefore(dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }

    private Claims decodeJWT(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY_ENV))
                .parseClaimsJws(token)
                .getBody();
    }

    private String createJWT(Long userId, long expirationMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY_ENV);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);

        long expMillis = nowMillis + expirationMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }
}
