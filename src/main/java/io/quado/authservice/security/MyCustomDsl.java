package io.quado.authservice.security;

import io.quado.authservice.filters.CustomAuthenticationFilter;
import io.quado.authservice.shared.Constants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authenticationManager);
        customFilter.setFilterProcessesUrl(Constants.LOGIN_URL);
        http.addFilter(customFilter);
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }
}
