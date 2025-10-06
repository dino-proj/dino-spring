// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.core.modules.framework.template.Template;
import cn.dinodev.spring.core.modules.framework.template.TemplateService;
import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;

/**
 *
 * @author Cody Lu
 * @author JL
 */

@Service
public class PageService extends ServiceBase<PageEntity, Long> {

  @Autowired
  private PageRepository pageRepository;

  @Autowired
  private TemplateService templateService;

  @Autowired
  ObjectMapper objectMapper;

  @Override
  public CrudRepositoryBase<PageEntity, Long> repository() {
    return pageRepository;
  }

  /**
   * 根据模板名称获取页面配置
   *
   * @param templateName 模板名称
   * @return 页面配置对象
   */
  public Page<PageConfig> getPageByTemplateName(String templateName) {
    return getPageByTemplateName(templateName, PageConfig.class);
  }

  /**
   * 根据模板名称获取指定类型的页面配置
   *
   * @param templateName 模板名称
   * @param cls 配置类型的 Class 对象
   * @param <T> 配置类型
   * @return 页面配置对象
   */
  @SuppressWarnings("unchecked")
  public <T extends PageConfig> Page<T> getPageByTemplateName(String templateName, Class<T> cls) {
    Template template = templateService.getByName(templateName);
    Assert.notNull(template, "模板不存在");

    var entity = pageRepository.getOneByTemplateName(ContextHelper.currentTenantId(), templateName);
    Page<T> page;
    if (entity.isPresent()) {
      page = projection(Page.class, entity);
      page.setId(entity.get().getId());
      page.setProperties(cls.cast(entity.get().getConfig()));
    } else {
      page = new Page<>();
      page.setTitle(template.getTitle());
    }
    page.setTemplate(template);
    return page;
  }

  /**
   * 根据页面 ID 获取指定类型的页面配置
   *
   * @param id 页面 ID
   * @param cls 配置类型的 Class 对象
   * @param <T> 配置类型
   * @return 页面配置对象，如果不存在则返回 null
   */
  @SuppressWarnings("unchecked")
  public <T extends PageConfig> Page<T> getPageById(Long id, Class<T> cls) {
    var entity = pageRepository.getOneById(ContextHelper.currentTenantId(), id);
    if (entity.isEmpty()) {
      return null;
    }

    Template template = templateService.getByName(entity.get().getTemplateName());
    Assert.notNull(template, "模板不存在");

    Page<T> page;
    page = projection(Page.class, entity);
    page.setProperties(cls.cast(entity.get().getConfig()));
    page.setId(entity.get().getId());
    page.setTemplate(template);
    return page;
  }

  /**
   * 根据页面 ID 获取页面配置
   *
   * @param id 页面 ID
   * @return 页面配置对象
   */
  public Page<PageConfig> getPageById(Long id) {
    return getPageById(id, PageConfig.class);
  }

  /**
   * 根据 ID 更新页面配置
   *
   * @param id 页面 ID
   * @param title 页面标题
   * @param config 页面配置
   * @return 更新后的页面配置对象
   */
  public Page<PageConfig> updatePageConfigById(Long id, String title, PageConfig config) {
    var entity = pageRepository.getOneById(ContextHelper.currentTenantId(), id);
    Assert.isTrue(entity.isPresent(), Status.CODE.FAIL_NOT_FOUND);
    entity.ifPresent(e -> {
      e.setConfig(config);
      e.setTitle(title);
      this.save(e);
    });
    return this.getPageById(id);
  }

  /**
   * 根据模板更新页面配置
   *
   * @param template 模板对象
   * @param title 页面标题
   * @param config 页面配置
   * @return 更新后的页面配置对象
   */
  @SuppressWarnings("unchecked")
  public Page<PageConfig> updatePageConfigByTemplate(Template template, String title, PageConfig config) {
    PageEntity pageEntity;
    if (template.getType() == PageType.CUSTOM) {
      //自定义类型的需要新建
      pageEntity = new PageEntity().setTemplateName(template.getName());
    } else {
      var entity = pageRepository.getOneByTemplateName(ContextHelper.currentTenantId(), template.getName());
      pageEntity = entity.orElseGet(() -> new PageEntity().setTemplateName(template.getName()));
    }
    pageEntity.setConfig(config);
    pageEntity.setTitle(title);
    pageEntity = this.save(pageEntity);
    Page<PageConfig> page = projection(Page.class, pageEntity);
    page.setTemplate(template);
    page.setProperties(pageEntity.getConfig());
    page.setId(pageEntity.getId());
    return page;
  }

}
