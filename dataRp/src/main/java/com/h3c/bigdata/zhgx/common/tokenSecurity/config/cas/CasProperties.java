package com.h3c.bigdata.zhgx.common.tokenSecurity.config.cas;

import com.h3c.bigdata.zhgx.common.tokenSecurity.util.LocalIpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * cas 对应所有参数的配置
 * @author w17193
 */
@Component
public class CasProperties {

    @Value("${cas.server.host.url}")
    private String casServerPrefix;

    @Value("${cas.server.host.login_url}")
    private String casServerLoginUrl;

    @Value("${cas.server.host.logout_url}")
    private String casServerLogoutUrl;

    @Value("${app.server.host.url}")
    private String appServicePrefix;

    @Value("${app.login.url}")
    private String appServiceLoginUrl;

    @Value("${app.logout.url}")
    private String appServiceLogoutUrl;

    @Value("${web.url}")
    private String webUrl;

    public String getCasServerPrefix() {
        return LocalIpUtil.replaceTrueIpIfLocalhost(casServerPrefix);
    }

    public void setCasServerPrefix(String casServerPrefix) {
        this.casServerPrefix = casServerPrefix;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }

    public void setCasServerLogoutUrl(String casServerLogoutUrl) {
        this.casServerLogoutUrl = casServerLogoutUrl;
    }

    public String getAppServicePrefix() {
        return LocalIpUtil.replaceTrueIpIfLocalhost(appServicePrefix);
    }

    public void setAppServicePrefix(String appServicePrefix) {
        this.appServicePrefix = appServicePrefix;
    }

    public String getAppServiceLoginUrl() {
        return appServiceLoginUrl;
    }

    public void setAppServiceLoginUrl(String appServiceLoginUrl) {
        this.appServiceLoginUrl = appServiceLoginUrl;
    }

    public String getAppServiceLogoutUrl() {
        return appServiceLogoutUrl;
    }

    public void setAppServiceLogoutUrl(String appServiceLogoutUrl) {
        this.appServiceLogoutUrl = appServiceLogoutUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
