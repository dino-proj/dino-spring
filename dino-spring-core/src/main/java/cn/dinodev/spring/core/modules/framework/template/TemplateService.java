// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import cn.dinodev.spring.core.modules.framework.annotion.PageTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Service
@Slf4j
public class TemplateService {
  private final Map<String, Template> templatesMap = new HashMap<>();

  /**
   * 初始化模板，扫描并加载所有带有 @PageTemplate 注解的类
   *
   * @throws IOException 如果读取资源时发生错误
   */
  @PostConstruct
  public void init() throws IOException {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    var clzsss = resourcePatternResolver.getResources("classpath*:/com/botbrain/botfish/**/*.class");
    //MetadataReader 的工厂类
    MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
    for (Resource resource : clzsss) {

      MetadataReader reader = readerfactory.getMetadataReader(resource);
      //扫描到的class
      String classname = reader.getClassMetadata().getClassName();
      try {
        Class<?> clazz = Class.forName(classname);

        //判断是否有指定主解
        PageTemplate anno = clazz.getAnnotation(PageTemplate.class);
        if (anno != null) {
          var template = new Template();
          template.setName(anno.name());
          template.setTitle(anno.title());
          template.setType(anno.type());
          template.setIcon(anno.icon());
          template.setAppPath(anno.appPath());
          template.setPcPath(anno.pcPath());
          template.setDescription(anno.description());
          template.setConfClass(clazz);
          templatesMap.put(anno.name(), template);
        }
      } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
        log.error("class:{} not found", classname);
      }
    }
  }

  /**
   * 根据模板名称获取模板
   *
   * @param templateName 模板名称
   * @return 模板对象，如果不存在则返回 null
   */
  public Template getByName(String templateName) {
    return templatesMap.get(templateName);
  }

}
