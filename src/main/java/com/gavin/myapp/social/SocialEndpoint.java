package com.gavin.myapp.social;

import com.gavin.myapp.config.ApplicationProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方登陆端点
 *
 */
@RestController
@RequestMapping("/social")
@ConditionalOnProperty(value = "social.enabled", havingValue = "true")
@Api(value = "第三方登陆", tags = "第三方登陆端点")
public class SocialEndpoint {

    private final ApplicationProperties applicationProperties;

    /**
     * 授权完毕跳转
     */
    @ApiOperation(value = "授权完毕跳转")
    @RequestMapping("/oauth/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = SocialUtil.getAuthRequest(source, applicationProperties);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 获取认证信息
     */
    @ApiOperation(value = "获取认证信息")
    @RequestMapping("/oauth/callback/{source}")
    public Object login(@PathVariable("source") String source, AuthCallback callback) {
        AuthRequest authRequest = SocialUtil.getAuthRequest(source, applicationProperties);
        return authRequest.login(callback);
    }

    /**
     * 撤销授权
     */
    @ApiOperation(value = "撤销授权")
    @RequestMapping("/oauth/revoke/{source}/{token}")
    public Object revokeAuth(@PathVariable("source") String source, @PathVariable("token") String token) {
        AuthRequest authRequest = SocialUtil.getAuthRequest(source, applicationProperties);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    /**
     * 续期accessToken
     */
    @ApiOperation(value = "续期令牌")
    @RequestMapping("/oauth/refresh/{source}")
    public Object refreshAuth(@PathVariable("source") String source, String token) {
        AuthRequest authRequest = SocialUtil.getAuthRequest(source, applicationProperties);
        return authRequest.refresh(AuthToken.builder().refreshToken(token).build());
    }

    public SocialEndpoint(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
}
