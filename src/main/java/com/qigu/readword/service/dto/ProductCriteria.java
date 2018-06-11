package com.qigu.readword.service.dto;

import java.io.Serializable;
import com.qigu.readword.domain.enumeration.LifeStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Product entity. This class is used in ProductResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /products?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable {
    /**
     * Class for filtering LifeStatus
     */
    public static class LifeStatusFilter extends Filter<LifeStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private DoubleFilter price;

    private IntegerFilter totalMonths;

    private DoubleFilter rank;

    private LifeStatusFilter lifeStatus;

    public ProductCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths(IntegerFilter totalMonths) {
        this.totalMonths = totalMonths;
    }

    public DoubleFilter getRank() {
        return rank;
    }

    public void setRank(DoubleFilter rank) {
        this.rank = rank;
    }

    public LifeStatusFilter getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(LifeStatusFilter lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (totalMonths != null ? "totalMonths=" + totalMonths + ", " : "") +
                (rank != null ? "rank=" + rank + ", " : "") +
                (lifeStatus != null ? "lifeStatus=" + lifeStatus + ", " : "") +
            "}";
    }

}
