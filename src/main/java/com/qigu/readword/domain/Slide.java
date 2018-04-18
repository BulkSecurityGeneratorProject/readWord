package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * A Slide.
 */
@Entity
@Table(name = "slide")
@Document(indexName = "slide")
public class Slide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank")
    private Double rank;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_status")
    private LifeStatus lifeStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private Image img;

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

    public Slide name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRank() {
        return rank;
    }

    public Slide rank(Double rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public LifeStatus getLifeStatus() {
        return lifeStatus;
    }

    public Slide lifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
        return this;
    }

    public void setLifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public Image getImg() {
        return img;
    }

    public Slide img(Image image) {
        this.img = image;
        return this;
    }

    public void setImg(Image image) {
        this.img = image;
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
        Slide slide = (Slide) o;
        if (slide.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slide.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Slide{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", lifeStatus='" + getLifeStatus() + "'" +
            "}";
    }
}
