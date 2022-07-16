package ru.job4j.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.job4j.chat.filter.JWTAuthenticationFilter;
import ru.job4j.chat.filter.JWTAuthorizationFilter;
import ru.job4j.chat.service.PersonDetailService;

import static ru.job4j.chat.filter.JWTAuthenticationFilter.SIGN_UP_URL;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 16.07.2022
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final PersonDetailService personDetailService;
    private final BCryptPasswordEncoder encoder;

    public WebSecurity(PersonDetailService personDetailService, BCryptPasswordEncoder encoder) {
        this.personDetailService = personDetailService;
        this.encoder = encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailService).passwordEncoder(encoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
                "/**", new CorsConfiguration()
                        .applyPermitDefaultValues());
        return source;
    }
}
