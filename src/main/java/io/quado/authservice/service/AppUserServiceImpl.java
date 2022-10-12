package io.quado.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quado.authservice.domain.AppUser;
import io.quado.authservice.domain.Role;
import io.quado.authservice.repo.AppUserRepo;
import io.quado.authservice.repo.RoleRepo;
import io.quado.authservice.shared.Constants;
import io.quado.authservice.shared.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements  AppUserService {

    private final AppUserRepo userRepo;
    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;


    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving a user {}", appUser.getName());
       appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepo.save(appUser) ;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role {}", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void saveRoleToUser(String username, String roleName) {
        log.info("fetching user with username {} and role with name {} in order to update the user role",username, roleName);
        AppUser user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("getting user with username {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("getting all the users");
        return userRepo.findAll();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = TokenUtils.verifyToken(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = getUser(username);
                List<String> roles =  user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
                String issuer = request.getRequestURL().toString();
                String access_token = TokenUtils.generateAccessToken(username,issuer, roles);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }


        }
    }


}
