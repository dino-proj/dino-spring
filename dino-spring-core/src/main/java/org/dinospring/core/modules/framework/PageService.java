// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.modules.framework.template.Template;
import org.dinospring.core.modules.framework.template.TemplateService;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
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

  public Page<PageConfig> getPageByTemplateName(String templateName) {
    return getPageByTemplateName(templateName, PageConfig.class);
  }

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

  public Page<PageConfig> getPageById(Long id) {
    return getPageById(id, PageConfig.class);
  }

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
