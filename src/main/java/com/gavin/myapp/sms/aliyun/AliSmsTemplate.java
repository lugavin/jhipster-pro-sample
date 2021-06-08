package com.gavin.myapp.sms.aliyun;

import static com.gavin.myapp.sms.builder.SmsBuilder.CAPTCHA_KEY;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.sms.SmsTemplate;
import com.gavin.myapp.sms.builder.SmsBuilder;
import com.gavin.myapp.sms.model.SmsCode;
import com.gavin.myapp.sms.model.SmsData;
import com.gavin.myapp.sms.model.SmsResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import javax.cache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * 阿里云短信发送类
 *
 */
public class AliSmsTemplate implements SmsTemplate {

    private static final int SUCCESS = 200;
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2017-05-25";
    private static final String ACTION = "SendSms";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ApplicationProperties applicationProperties;
    private final IAcsClient acsClient;
    private final CacheManager cacheManager;

    public AliSmsTemplate(ApplicationProperties applicationProperties, IAcsClient acsClient, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.acsClient = acsClient;
        this.cacheManager = cacheManager;
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(ACTION);
        request.putQueryParameter("PhoneNumbers", StringUtils.join(phones, ","));
        request.putQueryParameter("TemplateCode", applicationProperties.getSms().getTemplateId());
        request.putQueryParameter("SignName", applicationProperties.getSms().getSignName());
        try {
            request.putQueryParameter("TemplateParam", objectMapper.writeValueAsString(smsData.getParams()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            return new SmsResponse(response.getHttpStatus() == SUCCESS, response.getHttpStatus(), response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Override
    public SmsCode sendValidate(SmsData smsData, String phone) {
        SmsCode smsCode = new SmsCode();
        boolean temp = sendSingle(smsData, phone);
        if (temp && StringUtils.isNotBlank(smsData.getKey())) {
            String id = UUID.randomUUID().toString();
            String value = smsData.getParams().get(smsData.getKey());
            cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).put(CAPTCHA_KEY + id, value);
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
        String cache = (String) cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).get(CAPTCHA_KEY + id);
        if (StringUtils.isNotBlank(value) && StringUtils.equalsIgnoreCase(cache, value)) {
            cacheManager.getCache(SmsBuilder.CAPTCHA_KEY).remove(CAPTCHA_KEY + id);
            return true;
        }
        return false;
    }
}
