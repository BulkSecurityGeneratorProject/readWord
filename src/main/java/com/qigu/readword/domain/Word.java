package com.qigu.readword.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Word.
 */
@Entity
@Table(name = "word")
@Document(indexName = "word")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank")
    private Double rank;

    @Lob
    @Column(name = "desctription")
    private String desctription;

    @OneToOne
    @JoinColumn(unique = true)
    private Image img;

    @OneToOne
    @JoinColumn(unique = true)
    private Audio audio;

    @ManyToOne
    private User user;

    @ManyToOne
    private WordGroup wordGroup;

    @ManyToMany(mappedBy = "words")
    @JsonIgnore
    private Set<Favorite> favorites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Word name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRank() {
        return rank;
    }

    public Word rank(Double rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public String getDesctription() {
        return desctription;
    }

    public Word desctription(String desctription) {
        this.desctription = desctription;
        return this;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
    }

    public Image getImg() {
        return img;
    }

    public Word img(Image image) {
        this.img = image;
        return this;
    }

    public void setImg(Image image) {
        this.img = image;
    }

    public Audio getAudio() {
        return audio;
    }

    public Word audio(Audio audio) {
        this.audio = audio;
        return this;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public User getUser() {
        return user;
    }

    public Word user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WordGroup getWordGroup() {
        return wordGroup;
    }

    public Word wordGroup(WordGroup wordGroup) {
        this.wordGroup = wordGroup;
        return this;
    }

    public void setWordGroup(WordGroup wordGroup) {
        this.wordGroup = wordGroup;
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public Word favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
        return this;
    }

    public Word addFavorites(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.getWords().add(this);
        return this;
    }

    public Word removeFavorites(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.getWords().remove(this);
        return this;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
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
        Word word = (Word) o;
        if (word.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), word.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Word{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", desctription='" + getDesctription() + "'" +
            "}";
    }
}
