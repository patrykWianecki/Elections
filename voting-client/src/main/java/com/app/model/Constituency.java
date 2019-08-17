package com.app.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "constituencies")
public class Constituency {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "constituency")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Voter> voters;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "constituency")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Candidate> candidates;
}
