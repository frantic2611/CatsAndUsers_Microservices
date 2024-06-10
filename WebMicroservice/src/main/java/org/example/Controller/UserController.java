package org.example.Controller;

import lombok.AllArgsConstructor;
import org.example.DTO.UserDTO;
import org.example.Security.MyUserDetails;
import org.example.Span.UserSpan;
import org.example.Service.UserService;
import org.example.kafka.UserProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserProducer userProducer;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserSpan> save(@RequestBody UserSpan userSpan){
        userSpan.setPassword(passwordEncoder.encode(userSpan.getPassword()));
        userProducer.push(userSpan);
        return new ResponseEntity<>(userSpan, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpStatus deleteHuman(@PathVariable(name = "id") int id){
        userProducer.delete(id);
        return HttpStatus.OK;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserSpan> delete(@PathVariable(name = "id") Integer id, @RequestBody UserSpan userSpan){
        userSpan.setId(id);
        userProducer.update(userSpan);
        return new ResponseEntity<>(userSpan, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') ")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<UserDTO> users = null;
        if("ROLE_ADMIN".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            users = userService.getAll();
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getHuman(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }
}
