package com.app.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voters")
public class Voter {

    @Id
    @GeneratedValue
    private Long id;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Education education;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "constituency_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Constituency constituency;

    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "voter")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Token token;
}
