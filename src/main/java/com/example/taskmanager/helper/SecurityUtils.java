package com.example.taskmanager.helper;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtils {
    public static User getCurrentUser() {
        MyUserDetails myUserDetails = (MyUserDetails)Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal();
        return myUserDetails.user();
    }
}
