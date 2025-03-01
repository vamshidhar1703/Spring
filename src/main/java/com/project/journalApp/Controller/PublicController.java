package com.project.journalApp.Controller;

import com.project.journalApp.Entity.User;
import com.project.journalApp.Filter.JwtFilter;
import com.project.journalApp.Service.UserDetailsServiceImplementation;
import com.project.journalApp.Service.UserService;
import com.project.journalApp.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public Controller API", description = "Access to all Users")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/signup")
    @Operation(summary = "new registration")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        boolean b = userService.saveNewUser(user);
        if (b) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    @Operation(summary = "existing user login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {

            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

            UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(user.getUserName());
            String jwt=jwtUtil.generateToken(userDetails.getUsername());
            return  new ResponseEntity<>(jwt,HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception while CreateAuthenticationUser "+e);
            return new ResponseEntity<>("Incorrect Username or password",HttpStatus.BAD_REQUEST);
        }

    }

}
