package com.qigu.readword.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "total_months", nullable = false)
    private Integer totalMonths;

    @Column(name = "rank")
    private Double rank;

    @Lob
    @Column(name = "desctription")
    private String desctription;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_status")
    private LifeStatus lifeStatus;

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

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public Product price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTotalMonths() {
        return totalMonths;
    }

    public Product totalMonths(Integer totalMonths) {
        this.totalMonths = totalMonths;
        return this;
    }

    public void setTotalMonths(Integer totalMonths) {
        this.totalMonths = totalMonths;
    }

    public Double getRank() {
        return rank;
    }

    public Product rank(Double rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public String getDesctription() {
        return desctription;
    }

    public Product desctription(String desctription) {
        this.desctription = desctription;
        return this;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
    }

    public LifeStatus getLifeStatus() {
        return lifeStatus;
    }

    public Product lifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
        return this;
    }

    public void setLifeStatus(LifeStatus lifeStatus) {
        this.lifeStatus = lifeStatus;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", totalMonths=" + getTotalMonths() +
            ", rank=" + getRank() +
            ", desctription='" + getDesctription() + "'" +
            ", lifeStatus='" + getLifeStatus() + "'" +
            "}";
    }
}
