package com.formcrafter.auth.user;

import com.formcrafter.auth.security.SecurityUtils;
import com.formcrafter.auth.user.documentation.apis.GetLoggedInUserDocumentation;
import com.formcrafter.auth.user.documentation.controllers.UserControllerDocumentation;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@UserControllerDocumentation
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetLoggedInUserDocumentation
    @GetMapping("/me")
    public UserDTO getLoggedInUser() {
        return userService.getById(SecurityUtils.requireCurrentUser().getId());
    }
}
