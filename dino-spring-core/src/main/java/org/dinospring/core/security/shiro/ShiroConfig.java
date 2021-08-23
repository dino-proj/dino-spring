package org.dinospring.core.security.shiro;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ShiroConfig {

  @Bean("securityManager")
  public DefaultWebSecurityManager securityManager(ShiroRealm realm) {
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    // 使用自己的realm
    manager.setRealm(realm);

    /*
     * 关闭shiro自带的session，详情见文档
     * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
     */
    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
    DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
    defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
    subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
    manager.setSubjectDAO(subjectDAO);

    return manager;
  }

  @Bean
  public ShiroRealm shiroRealm() {
    return new ShiroRealm();
  }

  @Bean
  public ShiroAuthFilter shiroAuthFilter() {
    return new ShiroAuthFilter();
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
    var factoryBean = new ShiroFilterFactoryBean();

    // 添加自己的过滤器并且取名为jwt
    Map<String, Filter> filterMap = new HashMap<>();
    filterMap.put("authToken", shiroAuthFilter());
    factoryBean.setFilters(filterMap);

    factoryBean.setSecurityManager(securityManager);

    /*
     * 自定义url规则
     * http://shiro.apache.org/web.html#urls-
     */
    Map<String, String> filterRuleMap = new HashMap<>();
    // 所有请求通过我们自己的authToken Filter
    filterRuleMap.put("/**", "anon");

    filterRuleMap.put("/actuator", "anon");
    factoryBean.setFilterChainDefinitionMap(filterRuleMap);
    return factoryBean;
  }

  /**
   * 下面的代码是添加注解支持
   */
  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    var defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    // 强制使用cglib，防止重复代理和可能引起代理出错的问题
    // https://zhuanlan.zhihu.com/p/29161098
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }

  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      DefaultWebSecurityManager securityManager) {
    var advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }
}