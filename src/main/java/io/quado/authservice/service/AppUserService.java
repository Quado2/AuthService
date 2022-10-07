package io.quado.authservice.service;

import io.quado.authservice.domain.AppUser;
import io.quado.authservice.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AppUserService {

    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void saveRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
