package executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import service_client.security.TokenAuthenticationFilter;
import service_client.security.TokenService;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class App extends WebSecurityConfigurerAdapter {

    @Value("${token.key}")
    private String tokenKey;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf()
                .disable()
                .addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(name = "tokenAuthenticationFilter")
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenService());
    }

    @Bean(name = "tokenService")
    public TokenService tokenService() {
        TokenService tokenService = new TokenService();
        tokenService.setKey(tokenKey);
        return tokenService;
    }
}