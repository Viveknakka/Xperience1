package com.experience.Xperience.services;


import com.experience.Xperience.models.User;
import com.experience.Xperience.models.UserPrincipal;
import com.experience.Xperience.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo repo;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    public void saveUser(User user){
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        user.setPassword(encoder.encode(user.getPassword())); // Check this line
        repo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= repo.findByUsername(username);

        if (user==null) {
            System.out.println("User 404");
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipal(user);
    }

    public User getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    public boolean hasAdminRole(User currentUser) {
        return  currentUser.getRole().equalsIgnoreCase("Admin");
    }
}