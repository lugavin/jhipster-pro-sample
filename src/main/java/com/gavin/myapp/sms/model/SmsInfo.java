package com.gavin.myapp.sms.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 通知信息
 *
 */
public class SmsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知内容
     */
    private SmsData smsData;

    /**
     * 号码列表
     */
    private Collection<String> phones;

    public SmsInfo smsData(SmsData smsData) {
        this.smsData = smsData;
        return this;
    }

    public SmsInfo phones(Collection<String> phones) {
        this.phones = phones;
        return this;
    }

    public SmsData getSmsData() {
        return smsData;
    }

    public void setSmsData(SmsData smsData) {
        this.smsData = smsData;
    }

    public Collection<String> getPhones() {
        return phones;
    }

    public void setPhones(Collection<String> phones) {
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsInfo smsInfo = (SmsInfo) o;
        return Objects.equals(smsData, smsInfo.smsData) && Objects.equals(phones, smsInfo.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsData, phones);
    }

    @Override
    public String toString() {
        return "SmsInfo{" + "smsData=" + smsData + ", phones=" + phones + '}';
    }
}
