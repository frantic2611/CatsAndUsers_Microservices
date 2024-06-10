package org.example.Service;

import lombok.AllArgsConstructor;
import org.example.Cat;
import org.example.DAO.CatDao;
import org.example.DTO.CatDTO;
import org.example.Mapper.CatDTOMapper;
import org.example.Span.CatSpan;
import org.example.DAO.UserDAO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CatService {

    private final CatDao catDAO;
    private final UserDAO userDAO;
    private final CatDTOMapper catDTOMapper;

    @KafkaListener(topics = "saveCatTopic", groupId = "group1", containerFactory = "catKafkaListenerContainerFactory")
    public Cat save(CatSpan catSpan){
        Cat cat = new Cat();
        cat.setBreed(catSpan.getBreed());
        cat.setBirth(LocalDate.now());
        cat.setName(catSpan.getName());
        cat.setColor(catSpan.getColor());
        cat.setOwner(userDAO.findById(catSpan.getOwnerId()).get());
        if(catSpan.getFriends() != null){

            for(int friendId : catSpan.getFriends()) {
                Cat friend = catDAO.findById(friendId).get();
                cat.add(friend);
            }
        }
        catDAO.save(cat);
        if(cat.getFriends() != null){
            for(Cat friendId : cat.getFriends()) {
                friendId.add(cat);
            }
            catDAO.saveAll(cat.getFriends());
        }
        return catDAO.save(cat);
    }

    @KafkaListener(topics = "updateCatTopic", groupId = "group2", containerFactory = "catKafkaListenerContainerFactory")
    public Cat update(CatSpan catSpan){
        Cat cat = catDAO.findById(catSpan.getId()).get();
        cat.setBreed(catSpan.getBreed());
        cat.setName(catSpan.getName());
        cat.setColor(catSpan.getColor());
        cat.setOwner(userDAO.findById(catSpan.getOwnerId()).get());
        if(catSpan.getBirth() != null) cat.setBirth(catSpan.getBirth());
        if(catSpan.getFriends() != null){
            for(int friendId : catSpan.getFriends()) {
                Cat friend = catDAO.findById(friendId).get();
                cat.add(friend);
            }
        }
        catDAO.save(cat);
        if(cat.getFriends() != null){
            for(Cat friend : cat.getFriends())
                if(!friend.getFriends().contains(cat)) friend.add(cat);
            catDAO.saveAll(cat.getFriends());
        }
        return catDAO.save(cat);
    }

    @KafkaListener(topics = "deleteCatTopic", groupId = "group3", containerFactory = "cat1KafkaListenerContainerFactory")
    public void delete(int id){
        if(catDAO.findById(id).get().getFriends() != null){
            for( Cat friend : catDAO.findById(id).get().getFriends()){
                friend.getCat().deleteFriend(id);
            }
        }
        catDAO.deleteById(id);
    }

    public List<CatDTO> getAll(){
        return catDAO.findAll()
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public CatDTO getById(int id){
        return catDAO.findById(id)
                .map(catDTOMapper)
                .orElseThrow();
    }
    public List<CatDTO> findByColor(String color){
        return catDAO.findByColor(color)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByBreed(String breed){
        return catDAO.findByBreed(breed)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByName(String name){
        return catDAO.findByName(name)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByOwnerId(Integer id){
        return catDAO.findByOwnerId(id)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }

    public Integer getOwnerId(int id){
        return catDAO.findById(id).get().getOwner().getId();
    }
}
