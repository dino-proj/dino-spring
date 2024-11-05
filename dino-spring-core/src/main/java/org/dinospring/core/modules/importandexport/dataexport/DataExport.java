// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.importandexport.dataexport;

import static com.alibaba.excel.EasyExcelFactory.write;
import static com.alibaba.excel.EasyExcelFactory.writerSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import org.dinospring.commons.utils.LambdaUtils;
import org.dinospring.core.modules.importandexport.handler.DataExportHandler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

/**
 * @author JL
 * @Date: 2021/10/12
 */
public class DataExport {

  /**
   *
   * @param fileName
   * @param cls
   * @param collection
   * @param <T>
   * @throws IOException
   */
  public static <T> void simpleWrite(String fileName, Class<T> cls, DataExportHandler<T> cb)
      throws IOException {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(new File(fileName));
      simpleWrite(out, cls, cb);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param outputStream
   * @param cls
   * @param list
   * @param <T>
   * @throws IOException
   */
  public static <T> void simpleWrite(OutputStream outputStream, Class<T> cls, DataExportHandler<T> cb)
      throws IOException {
    // 这里 需要指定写用哪个class去写
    ExcelWriter excelWriter = null;
    try {
      excelWriter = write(outputStream, cls).build();
      WriteSheet writeSheet = writerSheet(0).build();
      excelWriter.write(cb::importData, writeSheet);
    } finally {
      // 千万别忘记finish 会帮忙关闭流
      if (excelWriter != null) {
        excelWriter.finish();
      }
      outputStream.close();
    }
  }

  public static <T> void excludeColumnWrite(String fileName, Class<T> cls, List<T> list,
      Set<String> excludeColumnFiledNames) {
    // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    write(fileName, cls).excludeColumnFieldNames(excludeColumnFiledNames).sheet(0)
        .doWrite(list);
  }

  public static <T> void includeColumnWrite(String fileName, Class<T> cls, List<T> list,
      Set<String> includeColumnFiledNames) {
    // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    write(fileName, cls).includeColumnFieldNames(includeColumnFiledNames).sheet(0)
        .doWrite(list);
  }

  public static <T> void repeatedWrite(String fileName, List<Class<T>> clsList, List<T> list) {
    ExcelWriter excelWriter = null;
    try {
      // 这里 指定文件
      excelWriter = write(fileName).build();
      // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
      ExcelWriter finalExcelWriter = excelWriter;
      LambdaUtils.forEach(clsList, (index, item) -> {
        // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
        WriteSheet writeSheet = writerSheet(index, "sheet" + index).head(item).build();
        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        finalExcelWriter.write(list, writeSheet);
      });
    } finally {
      // 千万别忘记finish 会帮忙关闭流
      if (excelWriter != null) {
        excelWriter.finish();
      }
    }
  }
}
