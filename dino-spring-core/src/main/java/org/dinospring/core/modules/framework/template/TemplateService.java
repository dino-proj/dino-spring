// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.dinospring.core.modules.framework.annotion.PageTemplate;
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
 * @author Cody LU
 */

@Service
@Slf4j
public class TemplateService {
  private Map<String, Template> templatesMap = new HashMap<>();

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
          var t = new Template();
          t.setName(anno.name());
          t.setTitle(anno.title());
          t.setType(anno.type());
          t.setIcon(anno.icon());
          t.setAppPath(anno.appPath());
          t.setPcPath(anno.pcPath());
          t.setDescription(anno.description());
          t.setConfClass(clazz);
          templatesMap.put(anno.name(), t);
        }
      } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
        log.error("class:{} not found", classname);
      }
    }
  }

  public Template getByName(String templateName) {
    return templatesMap.get(templateName);
  }

}
