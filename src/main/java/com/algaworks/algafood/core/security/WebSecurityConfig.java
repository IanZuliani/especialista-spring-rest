package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity //Abilita webSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()//vamos fazer em memoria
                .withUser("thiago")//usuario
                .password(passwordEncoder().encode("123"))//senha
                .roles("ADMIN")//Temos que informar as permissoes de acesso
                .and()
                .withUser("joao")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Ja que e uma api nao vamos utilizar FormLogin como vimos em outra aula
         * http.httpBasic().and().formLogin()
         * vamos deixa apenas HttpBasic
         */
        http.httpBasic() //tira  o form login da aplicacao e deixa apenas pela api
                .and()// e
                .authorizeRequests()//autoriza as requisicoes
                .antMatchers("/cozinhas/**").permitAll()//permita tudo nessa URi, tem que ser antes de authenticated
                .anyRequest().authenticated() //qualquer requisicao que esteja autenticada
                .and() // e
                .sessionManagement()// vamos trabalhar com a session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //informando que e STATELESS e nao possui estado, nem cookie
                .and()
                .csrf().disable();//desabilita o CSRF
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//metodo de criptografia seguranca para criptografar a senha
    }
}
