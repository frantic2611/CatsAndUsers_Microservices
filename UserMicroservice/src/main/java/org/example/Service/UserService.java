package org.example.Service;


import lombok.AllArgsConstructor;
import org.example.DTO.UserDTO;
import org.example.Mapper.UserDTOMapper;
import org.example.Span.UserSpan;
import org.example.User;
import org.example.DAO.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;

    private UserDTOMapper userDTOMapper;


    @KafkaListener(topics = "saveUserTopic", groupId = "group1",
                containerFactory = "userKafkaListenerContainerFactory")
    public void save(UserSpan userSpan){
        LOGGER.info(String.format("Message received -> %s", userSpan.getUsername()));
        User user = User.builder()
                .username(userSpan.getUsername())
                .role(userSpan.getRole())
                .birth(userSpan.getBirth())
                .password(userSpan.getPassword())
                .name(userSpan.getName())
                .build();
        if(user.getBirth() == null)user.setBirth(LocalDate.now());
        user.setCats(new ArrayList<>());
        userDAO.save(user);
    }

    @KafkaListener(topics = "updateUserTopic", groupId = "group2",
        containerFactory = "userKafkaListenerContainerFactory")
    public void update(UserSpan userSpan){
        User user = User.builder()
                .role(userSpan.getRole())
                .password(userSpan.getPassword())
                .username(userSpan.getUsername())
                .birth(userSpan.getBirth())
                .name(userSpan.getName()).build();
        if(user.getBirth() == null) user.setBirth(LocalDate.now());
        user.setId(userSpan.getId());
        userDAO.save(user);
    }

    @KafkaListener(topics = "deleteUserTopic1", groupId = "group3",
    containerFactory = "user1KafkaListenerContainerFactory")
    public void update(Integer id){
        /*if(userDAO.findById(id).get().getCats() != null){
            for( Cat cat : userDAO.findById(id).get().getCats()){
                catService.delete(cat.getId());
            }
        }*/
        userDAO.deleteById(id);
    }

    public UserDTO getById(int id) {
        return userDAO.findById(id)
                .map(userDTOMapper)
                .orElseThrow();
    }

    public List<UserDTO> getAll() {
        return userDAO.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username){
        return userDAO.findByUsername(username);
    }


}