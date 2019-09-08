package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constituencies")
public class Constituency {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constituency")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Voter> voters;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "constituency")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Candidate> candidates;
}
