package io.quado.authservice.service;

import io.quado.authservice.domain.AppUser;
import io.quado.authservice.domain.Role;
import io.quado.authservice.repo.AppUserRepo;
import io.quado.authservice.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserSeriveImpl implements  AppUserService{

    private final AppUserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving a user {}", appUser.getName());
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
