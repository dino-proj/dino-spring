// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.json;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于注解的JSON类型ID解析器，用于多态类型的序列化和反序列化
 * <p>
 * 该解析器通过扫描指定包下的类，查找带有特定注解的类，
 * 并建立类型ID与类之间的映射关系，用于Jackson的多态类型处理。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 * <li>扫描包路径下的所有类文件</li>
 * <li>识别带有指定注解的类</li>
 * <li>提取注解中的类型ID信息</li>
 * <li>建立双向映射关系（类型ID ↔ 类）</li>
 * <li>支持Jackson的多态序列化/反序列化</li>
 * </ul>
 * </p>
 * <p>
 * 使用场景：
 * 当需要对继承体系中的多个子类进行JSON序列化时，
 * 可以使用此解析器根据注解自动识别类型，
 * 避免手动配置每个子类的类型信息。
 * </p>
 * <p>
 * 线程安全：
 * 该类使用ConcurrentHashMap来确保在多线程环境下的线程安全性。
 * </p>
 *
 * @author Cody Lu
 */

@Slf4j
public class AnnotionedJsonTypeIdResolver extends TypeIdResolverBase {
  private final Map<String, Class<?>> idToType = new ConcurrentHashMap<>();
  private final Map<Class<?>, String> typeToId = new ConcurrentHashMap<>();
  private static final Map<Class<?>, String> ANNOS_CACHE = new ConcurrentHashMap<>(32);

  /**
   * 注册指定注解类型的类扫描器
   * <p>
   * 扫描指定包路径下的所有类，查找带有指定注解的类，
   * 并使用提供的ID提取器从注解中提取类型ID，
   * 将类与类型ID的映射关系缓存起来。
   * </p>
   *
   * @param <T> 注解类型
   * @param annoClass 要查找的注解类
   * @param idExtractor 从注解中提取类型ID的函数
   * @param packageToScan 要扫描的包路径（如：com.example.model）
   * @throws IOException 如果读取类文件时发生IO错误
   */
  public static <T extends Annotation> void addAnnotion(Class<T> annoClass, Function<T, String> idExtractor,
      @Nonnull String packageToScan) throws IOException {

    log.info("start scan package {}, to find annotion {}", packageToScan, annoClass.getName());

    // 将包路径转换为文件路径格式
    packageToScan = StringUtils.replace(packageToScan, ".", "/");
    packageToScan = StringUtils.removeEnd(packageToScan, "/");

    // 查找类文件
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] classResources = resourcePatternResolver.getResources("classpath*:/" + packageToScan + "/**/*.class");

    // MetadataReader 的工厂类
    MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
    for (Resource resource : classResources) {
      // 读取类的元数据
      var reader = readerfactory.getMetadataReader(resource);
      var clazzMeta = reader.getClassMetadata();

      // 过滤掉不需要的类：抽象类、注解、接口、非独立类
      if (clazzMeta.isAbstract() || clazzMeta.isAnnotation() || clazzMeta.isInterface() || !clazzMeta.isIndependent()) {
        continue;
      }
      // 没有目标注解的类
      if (!reader.getAnnotationMetadata().getAnnotations().isDirectlyPresent(annoClass)) {
        continue;
      }
      // 缓存类与ID的映射
      cacheClass(clazzMeta, annoClass, idExtractor);

    }
  }

  private static <T extends Annotation> void cacheClass(ClassMetadata clazzMeta, Class<T> annoClass,
      Function<T, String> idExtractor) {
    String classname = clazzMeta.getClassName();
    try {
      Class<?> clazz = Class.forName(classname);
      // 排除成员类和合成类
      if (!(clazz.isMemberClass() || clazz.isSynthetic())) {
        T anno = AnnotationUtils.findAnnotation(clazz, annoClass);
        // 如果找到指定注解，提取ID并缓存
        if (anno != null) {
          ANNOS_CACHE.put(clazz, idExtractor.apply(anno));
        }
      }
    } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
      log.error("class:{} not found", classname);
    }
  }

  /**
   * 初始化类型解析器
   * <p>
   * 根据指定的基础类型，从注解缓存中筛选出相关的子类，
   * 建立类型ID与类之间的双向映射关系。
   * </p>
   *
   * @param bt 基础Java类型，通常是多态类型的父类或接口
   */
  @Override
  public void init(JavaType bt) {
    ANNOS_CACHE.entrySet().forEach(e -> {
      if (bt.isTypeOrSuperTypeOf(e.getKey())) {
        this.typeToId.put(e.getKey(), e.getValue());
        // 检查类型ID是否重复
        if (this.idToType.containsKey(e.getValue())) {
          throw new IllegalStateException(
              "duplicate id:" + e.getValue() + " for " + e.getKey() + " AND " + this.idToType.get(e.getValue()));
        }
        this.idToType.put(e.getValue(), e.getKey());
      }
    });
  }

  /**
   * 根据对象实例获取类型ID
   *
   * @param value 要获取类型ID的对象实例
   * @return 对应的类型ID字符串，如果未找到则返回null
   */
  @Override
  public String idFromValue(Object value) {
    return this.typeToId.get(value.getClass());
  }

  /**
   * 根据对象实例和建议类型获取类型ID
   * <p>
   * 该方法忽略建议类型，直接使用对象的实际类型。
   * </p>
   *
   * @param value 要获取类型ID的对象实例
   * @param suggestedType 建议的类型（被忽略）
   * @return 对应的类型ID字符串
   */
  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return this.idFromValue(value);
  }

  /**
   * 根据类型标识符获取对应的Java类型。
   * <p>
   * 该方法通过传入的类型ID在类型映射表中查找对应的Java类型。
   * 如果找不到对应的类型，会抛出IllegalStateException异常。
   * </p>
   *
   * @param context 数据绑定上下文
   * @param id 类型标识符
   * @return 对应的Java类型对象
   * @throws IOException 如果IO操作失败
   * @throws IllegalStateException 如果找不到对应的类型
   */
  @Override
  public JavaType typeFromId(DatabindContext context, String id) throws IOException {
    if (!this.idToType.containsKey(id)) {
      throw new IllegalStateException("no class found for key:" + id);
    }
    return context.constructType(this.idToType.get(id));
  }

  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }

}
