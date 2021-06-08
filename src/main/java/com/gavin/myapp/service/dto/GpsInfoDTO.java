package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.GpsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.GpsInfo}的DTO。
 */
@ApiModel(description = "GPS信息")
public class GpsInfoDTO implements Serializable {

    private Long id;

    /**
     * gps坐标类型
     */
    @ApiModelProperty(value = "gps坐标类型")
    private GpsType type;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double latitude;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double longitude;

    /**
     * 地址描述
     */
    @ApiModelProperty(value = "地址描述")
    private String address;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GpsType getType() {
        return type;
    }

    public void setType(GpsType type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GpsInfoDTO)) {
            return false;
        }

        GpsInfoDTO gpsInfoDTO = (GpsInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gpsInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GpsInfoDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
