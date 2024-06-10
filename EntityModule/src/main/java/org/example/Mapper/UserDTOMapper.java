package org.example.Mapper;

import org.example.DTO.UserDTO;
import org.example.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User human){
        return new UserDTO(
                human.getId(),
                human.getUsername(),
                human.getPassword(),
                human.getRole(),
                human.getName(),
                human.getBirth(),
                human.getCats()
                        .stream()
                        .map(r -> r.getCat().getId())
                        .collect(Collectors.toList())
        );
    }
}
