package io.quado.authservice.service;

import io.quado.authservice.domain.AppUser;
import io.quado.authservice.domain.Role;
import io.quado.authservice.repo.AppUserRepo;
import io.quado.authservice.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements  AppUserService, UserDetailsService {

    private final AppUserRepo userRepo;
    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if(user == null){
            log.error("User notdound in the db");
            throw new UsernameNotFoundException("User not found in the db");
        } else{
            log.info("user with username: {} found in the database", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        // The User class is obtained from org.springframework.security.core.userdetails.User
        return new User(user.getUsername(), user.getPassword(),authorities);
    }

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


}
