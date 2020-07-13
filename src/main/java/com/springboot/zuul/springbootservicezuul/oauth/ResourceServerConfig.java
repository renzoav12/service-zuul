package com.springboot.zuul.springbootservicezuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Value("${config.security.oauth.jwt.key}")
  private String jwtKey;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.tokenStore(tokenStorage());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/api/oauth/oauth/token").permitAll()
        .antMatchers(HttpMethod.GET, "/api/products/products", "/api/items/getAll",
            "/api/users/users").permitAll()
        .antMatchers(HttpMethod.GET, "/api/products/ver/{id}",
            "/api/items/product/{id}/count/{count}",
            "/api/users/user/{id}").hasAnyRole("ADMIN", "USER")
        .antMatchers("/api/products/**", "/api/items/**", "/api/users/**").hasRole("ADMIN")
        .anyRequest().authenticated();
  }

  @Bean
  public TokenStore tokenStorage() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
    tokenConverter.setSigningKey(jwtKey);
    return tokenConverter;
  }
}
