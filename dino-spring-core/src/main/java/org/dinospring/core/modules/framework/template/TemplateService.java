// Copyright 2021 dinospring.cn
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

package org.dinospring.core.modules.framework.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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
 * @author tuuboo
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

  public Map<String, Template> getTemplatesMap(){
    return this.templatesMap;
  }
}
