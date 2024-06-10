package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "friends")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_cat_id")
    private Cat firstCat;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_cat_id")
    private Cat secondCat;

}
