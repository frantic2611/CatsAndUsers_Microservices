package org.example.Mapper;




import org.example.Cat;
import org.example.DTO.CatDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CatDTOMapper implements Function<Cat, CatDTO> {
    @Override
    public CatDTO apply(Cat cat) {
        return new CatDTO(
                cat.getName(),
                cat.getBreed(),
                cat.getColor(),
                cat.getOwner().getId(),
                cat.getBirth(),
                cat.getId(),
                cat.getFriends()
                        .stream()
                        .map(r -> r.getCat().getId())
                        .collect(Collectors.toList())
        );
    }
}