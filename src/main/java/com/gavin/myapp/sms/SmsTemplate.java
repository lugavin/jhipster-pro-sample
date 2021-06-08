package com.gavin.myapp.sms;

import com.gavin.myapp.sms.model.SmsCode;
import com.gavin.myapp.sms.model.SmsData;
import com.gavin.myapp.sms.model.SmsInfo;
import com.gavin.myapp.sms.model.SmsResponse;
import java.util.Collection;
import java.util.Collections;
import org.springframework.util.StringUtils;

/**
 * 短信通用封装
 *
 */
public interface SmsTemplate {
    /**
     * 发送短信
     *
     * @param smsInfo 短信信息
     * @return 发送返回
     */
    default boolean send(SmsInfo smsInfo) {
        return sendMulti(smsInfo.getSmsData(), smsInfo.getPhones());
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phone   手机号
     * @return 发送返回
     */
    default boolean sendSingle(SmsData smsData, String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Boolean.FALSE;
        }
        return sendMulti(smsData, Collections.singletonList(phone));
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    default boolean sendMulti(SmsData smsData, Collection<String> phones) {
        SmsResponse response = sendMessage(smsData, phones);
        return response.isSuccess();
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    SmsResponse sendMessage(SmsData smsData, Collection<String> phones);

    /**
     * 发送验证码
     *
     * @param smsData 短信内容
     * @param phone   手机号
     * @return 发送返回
     */
    SmsCode sendValidate(SmsData smsData, String phone);

    /**
     * 校验验证码
     *
     * @param smsCode 验证码内容
     * @return 是否校验成功
     */
    boolean validateMessage(SmsCode smsCode);
}
