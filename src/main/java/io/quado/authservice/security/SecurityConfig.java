package io.quado.authservice.security;

import io.quado.authservice.domain.AppUser;
import io.quado.authservice.filters.CustomAuthenticationFilter;
import io.quado.authservice.filters.CustomAuthorizationFilter;
import io.quado.authservice.repo.AppUserRepo;
import io.quado.authservice.shared.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.Collection;

import static io.quado.authservice.security.MyCustomDsl.customDsl;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {


    private AppUserRepo userRepo;

    @Autowired
    public SecurityConfig(AppUserRepo userRepo){
        this.userRepo = userRepo;
    }



    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser user = userRepo.findByUsername(username);
                if(user == null){
                    log.error("User not found in the db");
                    throw new UsernameNotFoundException("User not found in the db");
                } else{
                    log.info("user with username: {} found in the database", username);
                }

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                user.getRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });

                return new User(user.getUsername(), user.getPassword(),authorities);
            }
        };
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll())
//                .httpBasic(Customizer.withDefaults());

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeHttpRequests().antMatchers(Constants.LOGIN_URL+"/**",Constants.TOKEN_REFRESH_URL + "/**").permitAll();
        http.authorizeHttpRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
        http.authorizeHttpRequests().antMatchers(POST, "api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().anyRequest().permitAll();
        http.apply(customDsl());
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}



