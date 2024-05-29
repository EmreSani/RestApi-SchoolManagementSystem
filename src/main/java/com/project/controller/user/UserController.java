package com.project.controller.user;

import com.project.payload.request.user.UserRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save/{userRole}") // http://localhost:8080/user/save/Admin  + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(@RequestBody @Valid UserRequest userRequest,
                                                                  @PathVariable String userRole){
        return ResponseEntity.ok(userService.saveUser(userRequest, userRole));
    }

    // Not: getAllAdminOrDeanOrViceDeanByPage() ******************************************

    // Not :  getUserById() *********************************************************

    // Not : deleteUser() **********************************************************

    // Not: updateAdminOrDeanOrViceDean() ********************************************

    // Not: updateUserForUser() **********************************************************

    // Not : getByName() ***************************************************************
}
