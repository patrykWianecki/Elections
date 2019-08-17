package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String photo;
    private Integer votes;
    private Boolean isValid;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "constituency_id")
    private Constituency constituency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(id, candidate.id) &&
            Objects.equals(name, candidate.name) &&
            Objects.equals(surname, candidate.surname) &&
            Objects.equals(age, candidate.age) &&
            gender == candidate.gender &&
            Objects.equals(photo, candidate.photo) &&
            Objects.equals(votes, candidate.votes) &&
            Objects.equals(isValid, candidate.isValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, age, gender, photo, votes, isValid);
    }
}
