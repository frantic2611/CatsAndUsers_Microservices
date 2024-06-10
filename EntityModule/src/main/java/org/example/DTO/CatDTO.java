package org.example.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatDTO {
    private String name;
    private String breed;
    private String color;
    private Integer owner;
    private LocalDate birth;
    private int id;
    private List<Integer> friends;
}
