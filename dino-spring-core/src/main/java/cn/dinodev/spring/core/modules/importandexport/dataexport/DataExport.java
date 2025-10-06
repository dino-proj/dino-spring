// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.importandexport.dataexport;

import static com.alibaba.excel.EasyExcelFactory.write;
import static com.alibaba.excel.EasyExcelFactory.writerSheet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import cn.dinodev.spring.commons.utils.LambdaUtils;
import cn.dinodev.spring.core.modules.importandexport.handler.DataExportHandler;

/**
 * @author JL
 * @Date: 2021/10/12
 */
public final class DataExport {

  private DataExport() {
    // Utility class
  }

  /**
   * 简单写入 Excel 文件
   *
   * @param fileName 文件名
   * @param cls 数据类型的 Class 对象
   * @param cb 数据导出处理器
   * @param <T> 数据类型
   * @throws IOException 如果写入文件时发生错误
   */
  public static <T> void simpleWrite(String fileName, Class<T> cls, DataExportHandler<T> cb)
      throws IOException {
    try (OutputStream out = java.nio.file.Files.newOutputStream(new File(fileName).toPath())) {
      simpleWrite(out, cls, cb);
    }
  }

  /**
   * 简单写入 Excel 到输出流
   *
   * @param outputStream 输出流
   * @param cls 数据类型的 Class 对象
   * @param cb 数据导出处理器
   * @param <T> 数据类型
   * @throws IOException 如果写入时发生错误
   */
  public static <T> void simpleWrite(OutputStream outputStream, Class<T> cls, DataExportHandler<T> cb)
      throws IOException {
    // 这里 需要指定写用哪个class去写
    try (ExcelWriter excelWriter = write(outputStream, cls).build()) {
      WriteSheet writeSheet = writerSheet(0).build();
      excelWriter.write(cb::importData, writeSheet);
    }
  }

  /**
   * 排除指定列写入 Excel 文件
   *
   * @param fileName 文件名
   * @param cls 数据类型的 Class 对象
   * @param list 数据列表
   * @param excludeColumnFiledNames 要排除的列字段名集合
   * @param <T> 数据类型
   */
  public static <T> void excludeColumnWrite(String fileName, Class<T> cls, List<T> list,
      Set<String> excludeColumnFiledNames) {
    // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    write(fileName, cls).excludeColumnFieldNames(excludeColumnFiledNames).sheet(0)
        .doWrite(list);
  }

  /**
   * 仅包含指定列写入 Excel 文件
   *
   * @param fileName 文件名
   * @param cls 数据类型的 Class 对象
   * @param list 数据列表
   * @param includeColumnFiledNames 要包含的列字段名集合
   * @param <T> 数据类型
   */
  public static <T> void includeColumnWrite(String fileName, Class<T> cls, List<T> list,
      Set<String> includeColumnFiledNames) {
    // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    write(fileName, cls).includeColumnFieldNames(includeColumnFiledNames).sheet(0)
        .doWrite(list);
  }

  /**
   * 重复写入多个 Sheet 到 Excel 文件
   *
   * @param fileName 文件名
   * @param clsList 数据类型的 Class 对象列表
   * @param list 数据列表
   * @param <T> 数据类型
   */
  public static <T> void repeatedWrite(String fileName, List<Class<T>> clsList, List<T> list) {
    // 这里 指定文件
    try (ExcelWriter excelWriter = write(fileName).build()) {
      // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
      LambdaUtils.forEach(clsList, (index, item) -> {
        // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
        WriteSheet writeSheet = writerSheet(index, "sheet" + index).head(item).build();
        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        excelWriter.write(list, writeSheet);
      });
    }
  }
}
