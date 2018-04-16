package com.qigu.readword.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Favorite.
 */
@Entity
@Table(name = "favorite")
@Document(indexName = "favorite")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "favorite_words",
               joinColumns = @JoinColumn(name="favorites_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="words_id", referencedColumnName="id"))
    private Set<Word> words = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Favorite user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Word> getWords() {
        return words;
    }

    public Favorite words(Set<Word> words) {
        this.words = words;
        return this;
    }

    public Favorite addWords(Word word) {
        this.words.add(word);
        word.getFavorites().add(this);
        return this;
    }

    public Favorite removeWords(Word word) {
        this.words.remove(word);
        word.getFavorites().remove(this);
        return this;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        if (favorite.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favorite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + getId() +
            "}";
    }
}
