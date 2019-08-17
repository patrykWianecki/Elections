package com.app.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "constituency_id")
    private Constituency constituency;
    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "voter")
    private Token token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voter voter = (Voter) o;
        return Objects.equals(id, voter.id) &&
                Objects.equals(age, voter.age) &&
                gender == voter.gender &&
                education == voter.education;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, education);
    }
}
