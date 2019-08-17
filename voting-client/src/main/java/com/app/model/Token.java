package com.app.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Voter voter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id) &&
                Objects.equals(voterToken, token.voterToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, voterToken);
    }
}
