package com.gavin.myapp.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;

/**
 * 校验信息
 *
 */
public class SmsCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean success = Boolean.TRUE;

    /**
     * 变量id,用于redis进行比对
     */
    private String id;

    /**
     * 变量值,用于redis进行比对
     */
    @JsonIgnore
    private String value;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SmsCode success(boolean success) {
        this.success = success;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SmsCode id(String id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SmsCode value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsCode smsCode = (SmsCode) o;
        return success == smsCode.success && Objects.equals(id, smsCode.id) && Objects.equals(value, smsCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, id, value);
    }

    @Override
    public String toString() {
        return "SmsCode{" + "success=" + success + ", id='" + id + '\'' + ", value='" + value + '\'' + '}';
    }
}
