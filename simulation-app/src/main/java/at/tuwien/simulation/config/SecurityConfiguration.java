package at.tuwien.simulation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import at.tuwien.simulation.util.Endpoints;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;

@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${indmp.host}")
    private String host;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().and()
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers(HttpMethod.PUT, Endpoints.MADMP)
                        .hasIpAddress(host.replaceAll("http://", "").replaceAll(":8080", ""))
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/indmp-client"))
                .oauth2Client(withDefaults());
        return http.build();
    }
}
