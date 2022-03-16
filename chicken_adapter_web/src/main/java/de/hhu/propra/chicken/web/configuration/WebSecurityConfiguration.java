package de.hhu.propra.chicken.web.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final List<String> tutoren;
  private final List<String> organisatoren;

  public WebSecurityConfiguration(
      @Value("${gruppen.tutoren}") List<String> tutoren,
      @Value("${gruppen.organisatoren}") List<String> organisatoren) {
    this.tutoren = tutoren;
    this.organisatoren = organisatoren;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    HttpSecurity security = http.authorizeRequests(a -> a
        .antMatchers("/css/*", "/js/*", "/error", "/stats").permitAll()
        .anyRequest().authenticated()
    );
    security
        .logout()
        .clearAuthentication(true)
        .deleteCookies()
        .invalidateHttpSession(true)
        .permitAll()
        .and()
        .oauth2Login().userInfoEndpoint().userService(createUserService());
  }

  private OAuth2UserService<OAuth2UserRequest, OAuth2User> createUserService() {
    return new OAuth2UserService<>() {

      final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

      @Override
      public OAuth2User loadUser(OAuth2UserRequest userRequest)
          throws OAuth2AuthenticationException {
        OAuth2User oauth2User = defaultService.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String login = attributes.get("login").toString();

        if (tutoren.contains(login)) {
          authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (organisatoren.contains(login)) {
          authorities.add(new SimpleGrantedAuthority("ROLE_LEADER"));
        }

        return new DefaultOAuth2User(authorities, attributes, "login");
      }


    };


  }

}