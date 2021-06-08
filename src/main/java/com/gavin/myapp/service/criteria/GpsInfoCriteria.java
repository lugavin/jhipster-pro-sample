package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.GpsType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gavin.myapp.domain.GpsInfo} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.GpsInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gps-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GpsInfoCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering GpsType
     */
    public static class GpsTypeFilter extends Filter<GpsType> {

        public GpsTypeFilter() {}

        public GpsTypeFilter(GpsTypeFilter filter) {
            super(filter);
        }

        @Override
        public GpsTypeFilter copy() {
            return new GpsTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private GpsTypeFilter type;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private StringFilter address;

    public GpsInfoCriteria() {}

    public GpsInfoCriteria(GpsInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.address = other.address == null ? null : other.address.copy();
    }

    @Override
    public GpsInfoCriteria copy() {
        return new GpsInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public GpsTypeFilter getType() {
        return type;
    }

    public GpsTypeFilter type() {
        if (type == null) {
            type = new GpsTypeFilter();
        }
        return type;
    }

    public void setType(GpsTypeFilter type) {
        this.type = type;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            latitude = new DoubleFilter();
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            longitude = new DoubleFilter();
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GpsInfoCriteria that = (GpsInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(address, that.address)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, latitude, longitude, address);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GpsInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
            "}";
    }
}
