package com.gavin.myapp.sms.yunpian;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.sms.SmsTemplate;
import com.gavin.myapp.sms.builder.SmsBuilder;
import com.gavin.myapp.sms.model.SmsCode;
import com.gavin.myapp.sms.model.SmsData;
import com.gavin.myapp.sms.model.SmsResponse;
import com.gavin.myapp.util.PlaceholderUtil;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.constant.Code;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsBatchSend;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import javax.cache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * 云片短信发送类
 *
 */
public class YunpianSmsTemplate implements SmsTemplate {

    private final ApplicationProperties applicationProperties;
    private final YunpianClient client;
    private final CacheManager cacheManager;

    public YunpianSmsTemplate(ApplicationProperties applicationProperties, YunpianClient client, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.client = client;
        this.cacheManager = cacheManager;
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        String templateId = applicationProperties.getSms().getTemplateId();
        // 云片短信模板内容替换, 占位符格式为官方默认的 #code#
        LinkedCaseInsensitiveMap<Object> map = new LinkedCaseInsensitiveMap<>();
        map.putAll(smsData.getParams());
        String templateText = PlaceholderUtil.getResolver("#", "#").resolveByMap(templateId, map);
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, StringUtils.join(phones, ","));
        param.put(YunpianClient.TEXT, templateText);
        Result<SmsBatchSend> result = client.sms().multi_send(param);
        return new SmsResponse(result.getCode() == Code.OK, result.getCode(), result.toString());
    }

    @Override
    public SmsCode sendValidate(SmsData smsData, String phone) {
        SmsCode smsCode = new SmsCode();
        boolean temp = sendSingle(smsData, phone);
        if (temp && StringUtils.isNotBlank(smsData.getKey())) {
            String id = UUID.randomUUID().toString();
            String value = smsData.getParams().get(smsData.getKey());
            cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).put(SmsBuilder.CAPTCHA_KEY + id, value);
            smsCode.id(id).setValue(value);
        } else {
            smsCode.setSuccess(Boolean.FALSE);
        }
        return smsCode;
    }

    @Override
    public boolean validateMessage(SmsCode smsCode) {
        String id = smsCode.getId();
        String value = smsCode.getValue();
        String cache = (String) cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).get(SmsBuilder.CAPTCHA_KEY + id);
        if (StringUtils.isNotBlank(value) && StringUtils.equalsIgnoreCase(cache, value)) {
            cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).remove(SmsBuilder.CAPTCHA_KEY + id);
            return true;
        }
        return false;
    }
}
