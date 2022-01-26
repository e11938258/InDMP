package at.tuwien.indmp.config;

import at.tuwien.indmp.util.Endpoints;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, Endpoints.CREATE_NEW_RDM_SERVICE).permitAll()
                .antMatchers(HttpMethod.PUT, Endpoints.UPDATE_MADMP, Endpoints.IDENTIFIER_CHANGE).permitAll()
                .antMatchers(HttpMethod.GET, Endpoints.GET_MADMP, Endpoints.GET_ALL_RDM_SERVICES,
                        Endpoints.GET_MADMP_IDENTIFIERS, "/**")
                .permitAll()
                .antMatchers(HttpMethod.DELETE, Endpoints.DELETE_INSTANCE).permitAll();
    }
}
