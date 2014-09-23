package fi.helsinki.cs.tmc.snapshot.api.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${security.basic.realm}")
    private String basicRealm;

    @Value("${security.basic.path}")
    private String basicPath;

    @Value("${security.user.name}")
    private String username;

    @Value("${security.user.password}")
    private String password;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(final HttpSecurity security) throws Exception {

        security.authorizeRequests()
                .antMatchers(basicPath)
                .hasRole("USER")
                .and()
                .httpBasic()
                .realmName(basicRealm);
    }

    @Override
    public void configure(final AuthenticationManagerBuilder managerBuilder) throws Exception {

        managerBuilder.jdbcAuthentication()
                      .dataSource(dataSource)
                      .withDefaultSchema()
                      .withUser(username)
                      .password(password)
                      .roles("USER");
    }
}