package ru.golikov.notes.domain.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.golikov.notes.domain.security.jwt.JwtTokenFilter;
import ru.golikov.notes.domain.security.model.AuthenticationEntryPointImpl;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${endpoints.auth}")
    private String authUrl;

    @Value("${endpoints.notes}")
    private String notesUrl;

    @Value("${endpoints.admin}")
    private String adminUrl;

    private final JwtTokenFilter jwtTokenFilter;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(authUrl, "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
                .antMatchers(adminUrl).hasRole("ADMIN")
                .antMatchers(notesUrl).hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
