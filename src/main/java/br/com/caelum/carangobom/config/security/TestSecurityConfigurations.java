package br.com.caelum.carangobom.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Profile("test")
public class TestSecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .and().csrf()
            .ignoringAntMatchers("/usuarios")
            .ignoringAntMatchers("/usuarios/*")
            .ignoringAntMatchers("/auth")
            .ignoringAntMatchers("/veiculos")
            .ignoringAntMatchers("/veiculos/*")
            .ignoringAntMatchers("/marcas")
            .ignoringAntMatchers("/marcas/*");
    }
}
