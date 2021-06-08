package com.gavin.myapp.sms.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 通知内容
 *
 */
public class SmsData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 构造器
     *
     * @param params 参数列表
     */
    public SmsData(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 变量key,用于从参数列表获取变量值
     */
    private String key;

    /**
     * 参数列表
     */
    private Map<String, String> params;

    public SmsData(String key, Map<String, String> params) {
        this.key = key;
        this.params = params;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public SmsData key(String key) {
        this.key = key;
        return this;
    }

    public SmsData params(Map<String, String> params) {
        this.params = params;
        return this;
    }
}
