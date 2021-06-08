package com.gavin.myapp.sms.tencent;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.sms.SmsTemplate;
import com.gavin.myapp.sms.builder.SmsBuilder;
import com.gavin.myapp.sms.model.SmsCode;
import com.gavin.myapp.sms.model.SmsData;
import com.gavin.myapp.sms.model.SmsResponse;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import javax.cache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * 腾讯云短信发送类
 *
 */
public class TencentSmsTemplate implements SmsTemplate {

    private static final int SUCCESS = 0;
    private static final String NATION_CODE = "86";

    private final ApplicationProperties applicationProperties;
    private final SmsMultiSender smsSender;
    private final CacheManager cacheManager;

    public TencentSmsTemplate(ApplicationProperties applicationProperties, SmsMultiSender smsSender, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.smsSender = smsSender;
        this.cacheManager = cacheManager;
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        try {
            Collection<String> values = smsData.getParams().values();
            String[] params = org.springframework.util.StringUtils.toStringArray(values);
            SmsMultiSenderResult senderResult = smsSender.sendWithParam(
                NATION_CODE,
                org.springframework.util.StringUtils.toStringArray(phones),
                Integer.parseInt(applicationProperties.getSms().getTemplateId()),
                params,
                applicationProperties.getSms().getSignName(),
                "",
                ""
            );
            return new SmsResponse(senderResult.result == SUCCESS, senderResult.result, senderResult.toString());
        } catch (HTTPException | IOException e) {
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
