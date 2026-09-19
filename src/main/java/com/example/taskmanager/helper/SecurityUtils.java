package com.example.taskmanager.helper;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
