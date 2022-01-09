package at.tuwien.indmp.config;

import at.tuwien.indmp.util.Constants;

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
                .antMatchers(HttpMethod.POST, Constants.CREATE_SYSTEM).permitAll()
                .antMatchers(HttpMethod.PUT, Constants.UPDATE_MADMP, Constants.IDENTIFIER_CHANGE).permitAll()
                .antMatchers(HttpMethod.GET, Constants.GET_MADMP, Constants.GET_ALL_SYSTEMS,
                        Constants.GET_MADMP_IDENTIFIERS, "/**")
                .permitAll()
                .antMatchers(HttpMethod.DELETE, Constants.DELETE_INSTANCE).permitAll();
    }
}
