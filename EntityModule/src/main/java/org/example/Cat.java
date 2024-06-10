package org.example;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cats")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String breed;
    private String color;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;
    private LocalDate birth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "first_cat_id"),
            inverseJoinColumns = @JoinColumn(name = "second_cat_id"))
    private List<Cat> friends;
    public Cat getCat(){return this;}
    public void add(Cat cat){
        if(friends == null) friends = new ArrayList<>();
        this.friends.add(cat);
    }
    public void deleteFriend(int id){
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getId() == id){friends.remove(friends.get(i).getCat());}
        }
    }
    public void deleteAllFriends(){
        for(int i = 0; i < friends.size(); i++){
            friends.remove(friends.get(i).getCat());
        }
    }
}