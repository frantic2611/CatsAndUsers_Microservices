package org.example.Span;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserSpan {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String name;
    private LocalDate birth;
}