package at.tuwien.indmp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import at.tuwien.indmp.util.Endpoints;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(authz -> authz
                        // maDMP
                        .antMatchers(HttpMethod.PUT, Endpoints.UPDATE_MADMP, Endpoints.IDENTIFIER_CHANGE,
                                Endpoints.DELETE_INSTANCE)
                        .hasAuthority("SCOPE_update")
                        .antMatchers(HttpMethod.GET, Endpoints.GET_MADMP, Endpoints.GET_MADMP_IDENTIFIERS)
                        .hasAuthority("SCOPE_update")
                        // Services
                        .antMatchers("/system/**").hasAuthority("SCOPE_update")
                        // Other
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }
}
