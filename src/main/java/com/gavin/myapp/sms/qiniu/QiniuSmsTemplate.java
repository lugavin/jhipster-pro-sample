package com.gavin.myapp.sms.qiniu;

import static com.gavin.myapp.sms.builder.SmsBuilder.CAPTCHA_KEY;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.sms.SmsTemplate;
import com.gavin.myapp.sms.builder.SmsBuilder;
import com.gavin.myapp.sms.model.SmsCode;
import com.gavin.myapp.sms.model.SmsData;
import com.gavin.myapp.sms.model.SmsResponse;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import javax.cache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * 七牛云短信发送类
 *
 */
public class QiniuSmsTemplate implements SmsTemplate {

    private final ApplicationProperties applicationProperties;
    private final SmsManager smsManager;
    private final CacheManager cacheManager;

    public QiniuSmsTemplate(ApplicationProperties applicationProperties, SmsManager smsManager, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.smsManager = smsManager;
        this.cacheManager = cacheManager;
    }

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        try {
            Response response = smsManager.sendMessage(
                applicationProperties.getSms().getTemplateId(),
                org.springframework.util.StringUtils.toStringArray(phones),
                smsData.getParams()
            );
            return new SmsResponse(response.isOK(), response.statusCode, response.toString());
        } catch (QiniuException e) {
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
