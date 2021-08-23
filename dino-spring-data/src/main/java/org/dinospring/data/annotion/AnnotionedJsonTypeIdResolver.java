package org.dinospring.data.annotion;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotionedJsonTypeIdResolver extends TypeIdResolverBase {
  private final Map<String, Class<?>> idToType = new HashMap<>();
  private final Map<Class<?>, String> typeToId = new HashMap<>();
  private static final Map<Class<?>, String> annos = new HashMap<>();

  public static <T extends Annotation> void addAnnotion(Class<T> annoClass, Function<T, String> idExtractor,
      @Nonnull String packageToScan) throws IOException {

    log.info("start scan package {}, to find annotion {}", packageToScan, annoClass.getName());

    packageToScan = StringUtils.replace(packageToScan, ".", "/");
    packageToScan = StringUtils.removeEnd(packageToScan, "/");
    // find classes
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    var clzsss = resourcePatternResolver.getResources("classpath*:/" + packageToScan + "/**/*.class");
    //MetadataReader 的工厂类
    MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
    for (Resource resource : clzsss) {

      var reader = readerfactory.getMetadataReader(resource);
      var clazzMeta = reader.getClassMetadata();
      //扫描到的class
      if (clazzMeta.isAbstract() || clazzMeta.isAnnotation() || clazzMeta.isInterface() || !clazzMeta.isIndependent()) {
        continue;
      }
      if (!reader.getAnnotationMetadata().getAnnotations().isDirectlyPresent(annoClass)) {
        continue;
      }
      String classname = clazzMeta.getClassName();
      try {
        Class<?> clazz = Class.forName(classname);
        if (clazz.isMemberClass() || clazz.isSynthetic()) {
          continue;
        }

        var anno = AnnotationUtils.findAnnotation(clazz, annoClass);
        //判断是否有指定注解
        if (anno != null) {
          annos.put(clazz, idExtractor.apply(anno));
        }
      } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
        log.error("class:{} not found", classname);
      }
    }
  }

  @Override
  public void init(JavaType bt) {
    annos.entrySet().forEach(e -> {
      if (bt.isTypeOrSuperTypeOf(e.getKey())) {
        typeToId.put(e.getKey(), e.getValue());
        if (idToType.containsKey(e.getValue())) {
          throw new IllegalStateException(
              "duplicate id:" + e.getValue() + " for " + e.getKey() + " AND " + idToType.get(e.getValue()));
        }
        idToType.put(e.getValue(), e.getKey());
      }
    });
  }

  @Override
  public String idFromValue(Object value) {
    return typeToId.get(value.getClass());
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return idFromValue(value);
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) throws IOException {
    if (!idToType.containsKey(id)) {
      throw new IllegalStateException("no class found for key:" + id);
    }
    return context.constructType(idToType.get(id));
  }

  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }

}
