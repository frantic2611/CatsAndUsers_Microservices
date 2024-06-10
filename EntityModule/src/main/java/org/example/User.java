package org.example;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String name;
    private LocalDate birth;
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private List<Cat> cats;
}