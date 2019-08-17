package com.app.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "votersTokens")
public class Token {

    @Id
    @GeneratedValue
    private Long id;
    private Integer voterToken;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "voter_id", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Voter voter;
}
