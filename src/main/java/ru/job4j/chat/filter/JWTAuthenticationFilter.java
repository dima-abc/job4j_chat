package ru.job4j.chat.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import ru.job4j.chat.domain.Person;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * JWTAuthenticationFilter
 * Метод attempt проверяет, что логин и пароль верные.
 * Метод success генерирует token.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 16.07.2022
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String SECRET = "SecretKeyToGenJWTs";
    /**
     * 10 days
     */
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/person/";

    private final AuthenticationManager auth;

    public JWTAuthenticationFilter(AuthenticationManager auth) {
        this.auth = auth;
    }

    /**
     * Метод attempt проверяет, что логин и пароль верные.
     *
     * @param req  from which to extract parameters and perform the authentication
     * @param resp the response, which may be needed if the implementation has to do a
     *             redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication
     * @throws AuthenticationException throws
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws AuthenticationException {
        try {
            Person person = new ObjectMapper()
                    .readValue(req.getInputStream(), Person.class);
            return this.auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            person.getLogin(),
                            person.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Метод success генерирует token.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param chain FilterChain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException exception
     * @throws ServletException exception
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        resp.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
