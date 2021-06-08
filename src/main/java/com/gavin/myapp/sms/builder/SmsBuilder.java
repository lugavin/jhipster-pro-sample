package com.gavin.myapp.sms.builder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.domain.SmsConfig;
import com.gavin.myapp.domain.enumeration.SmsProvider;
import com.gavin.myapp.service.SmsConfigService;
import com.gavin.myapp.sms.SmsTemplate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.cache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * Sms短信服务统一构建类
 *
 */
public class SmsBuilder {

    public static final String SMS_CODE = "sms:code:";
    public static final String SMS_PARAM_KEY = "code";
    public static final String CAPTCHA_KEY = "jhipster:sms::captcha:";

    private final ApplicationProperties applicationProperties;
    private final SmsConfigService smsConfigService;
    private final CacheManager cacheManager;

    public SmsBuilder(ApplicationProperties applicationProperties, SmsConfigService smsConfigService, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.smsConfigService = smsConfigService;
        this.cacheManager = cacheManager;
    }

    /**
     * SmsTemplate配置缓存池
     */
    private final Map<String, SmsTemplate> templatePool = new ConcurrentHashMap<>();

    /**
     * Sms配置缓存池
     */
    private final Map<String, SmsConfig> smsPool = new ConcurrentHashMap<>();

    /**
     * 获取template
     *
     * @return SmsTemplate
     */
    public SmsTemplate template() {
        return template("");
    }

    /**
     * 获取template
     *
     * @param code 资源编号
     * @return SmsTemplate
     */
    public SmsTemplate template(String code) {
        SmsConfig sms = getSms(code);
        SmsConfig smsCached = smsPool.get("admin-sms");
        SmsTemplate template = templatePool.get("admin-smsTemplate");
        // 若为空或者不一致，则重新加载
        if (
            ObjectUtils.isEmpty(template) ||
            ObjectUtils.isEmpty(smsCached) ||
            !sms.getTemplateId().equals(smsCached.getTemplateId()) ||
            !sms.getAccessKey().equals(smsCached.getAccessKey())
        ) {
            synchronized (SmsBuilder.class) {
                template = templatePool.get("admin-smsTemplate");
                if (
                    ObjectUtils.isEmpty(template) ||
                    ObjectUtils.isEmpty(smsCached) ||
                    !sms.getTemplateId().equals(smsCached.getTemplateId()) ||
                    !sms.getAccessKey().equals(smsCached.getAccessKey())
                ) {
                    if (sms.getProvider() == SmsProvider.YUNPIAN) {
                        template = YunpianSmsBuilder.template(sms, cacheManager);
                    } else if (sms.getProvider() == SmsProvider.QINIU) {
                        template = QiniuSmsBuilder.template(sms, cacheManager);
                    } else if (sms.getProvider() == SmsProvider.ALI) {
                        template = AliSmsBuilder.template(sms, cacheManager);
                    } else if (sms.getProvider() == SmsProvider.TENCENT) {
                        template = TencentSmsBuilder.template(sms, cacheManager);
                    }
                    templatePool.put("admin-smsTemplate", template);
                    smsPool.put("admin-sms", sms);
                }
            }
        }
        return template;
    }

    /**
     * 获取短信实体
     *
     * @return Sms
     */
    public SmsConfig getSms(String smsCode) {
        LambdaQueryWrapper<SmsConfig> lqw = Wrappers.<SmsConfig>query().lambda();
        // 获取传参的资源编号并查询，若有则返回，若没有则调启用的配置
        String key = "";
        // String smsCode = StringUtils.isBlank(code) ? WebUtil.getParameter(SMS_PARAM_KEY) : code;
        if (StringUtils.isNotBlank(smsCode)) {
            key = key.concat("-").concat(smsCode);
            lqw.eq(SmsConfig::getSmsCode, smsCode);
        } else {
            lqw.eq(SmsConfig::getEnabled, true);
        }
        SmsConfig s = smsConfigService.getOne(lqw);
        SmsConfig result = new SmsConfig();
        // 若为空则调用默认配置
        if ((ObjectUtils.isEmpty(s))) {
            SmsConfig defaultSms = new SmsConfig();
            result.setId(0L);
            result.setTemplateId(applicationProperties.getSms().getTemplateId());
            result.setRegionId(applicationProperties.getSms().getRegionId());
            result.setProvider(SmsProvider.valueOf(applicationProperties.getSms().getName()));
            result.setAccessKey(applicationProperties.getSms().getAccessKey());
            result.setSecretKey(applicationProperties.getSms().getSecretKey());
            result.setSignName(applicationProperties.getSms().getSignName());
        } else {
            result = s;
        }
        return result;
    }
}
