package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WordGroup.
 */
@Entity
@Table(name = "word_group")
@Document(indexName = "wordgroup")
public class WordGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank")
    private Double rank;

    @OneToOne
    @JoinColumn(unique = true)
    private Image img;

    @ManyToOne
    private User user;

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

    public WordGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRank() {
        return rank;
    }

    public WordGroup rank(Double rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Image getImg() {
        return img;
    }

    public WordGroup img(Image image) {
        this.img = image;
        return this;
    }

    public void setImg(Image image) {
        this.img = image;
    }

    public User getUser() {
        return user;
    }

    public WordGroup user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        WordGroup wordGroup = (WordGroup) o;
        if (wordGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wordGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WordGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            "}";
    }
}
