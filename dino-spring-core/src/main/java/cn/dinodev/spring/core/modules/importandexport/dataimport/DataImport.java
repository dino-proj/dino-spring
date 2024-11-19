// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.importandexport.dataimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;

import cn.dinodev.spring.commons.utils.LambdaUtils;
import lombok.SneakyThrows;

/**
 * @author JL
 * @Date: 2021/9/30
 */
public class DataImport {

  /**
   * 读取Excel文件并处理数据
   *
   * @param <T> 数据类型
   * @param in 输入流
   * @param cls 数据类型的Class对象
   * @param cb 处理数据的回调函数
   * @throws IOException 如果读取文件时发生错��
   *
   * 示例：
   * InputStream in = new FileInputStream("example.xlsx");
   * DataImport.doRead(in, ExampleData.class, dataList -> {
   *     // 处理数据
   * });
   */
  public static <T> void doRead(InputStream in, Class<T> cls, Consumer<List<T>> cb) throws IOException {
    EasyExcel.read(in, cls, new ReadListener<T>() {
      private List<T> list = new LinkedList<>();

      @Override
      public void invoke(T data, AnalysisContext context) {
        list.add(data);
      }

      @SneakyThrows
      @Override
      public void doAfterAllAnalysed(AnalysisContext context) {
        cb.accept(list);
      }
    }).sheet().doRead();
  }

  /**
   * 读取Excel文件并处理数据
   *
   * @param <T> 数据类型
   * @param pathName 文件路径
   * @param cls 数据类型的Class对象
   * @param cb 处理数据的回调函数
   * @throws IOException 如果读取文件时发生错误
   *
   * 示例：
   * DataImport.doRead("example.xlsx", ExampleData.class, dataList -> {
   *     // 处理数据
   * });
   */
  public static <T> void doRead(String pathName, Class<T> cls, Consumer<List<T>> cb) throws IOException {

    try (InputStream in = new FileInputStream(new File(pathName))) {
      doRead(in, cls, cb);
    }

  }

  /**
   * 读取多个Sheet并处理数据
   *
   * @param <T> 数据类型
   * @param in 输入流
   * @param list 数据类型的Class对象列表
   * @param cb 处理数据的回调函数
   * @throws IOException 如果读取文件时发生错误
   *
   * 示例：
   * InputStream in = new FileInputStream("example.xlsx");
   * List<Class<ExampleData>> list = Arrays.asList(ExampleData.class);
   * DataImport.repeatedRead(in, list, dataList -> {
   *     // 处理数据
   * });
   */
  public static <T> void repeatedRead(InputStream in, List<Class<T>> list, Consumer<List<T>> cb)
      throws IOException {

    ExcelReader excelReader = EasyExcel.read(in).build();
    analysis(excelReader, list, cb);

  }

  /**
   * 读取多个Sheet并处理数据
   *
   * @param <T> 数据类型
   * @param pathName 文件路径
   * @param list 数据类型的Class对象列表
   * @param cb 处理数据的回调函数
   * @throws IOException 如果读取文件时发生错误
   *
   * 示例：
   * List<Class<ExampleData>> list = Arrays.asList(ExampleData.class);
   * DataImport.repeatedRead("example.xlsx", list, dataList -> {
   *     // 处理数据
   * });
   */
  public static <T> void repeatedRead(String pathName, List<Class<T>> list, Consumer<List<T>> cb) throws IOException {

    try (InputStream in = new FileInputStream(new File(pathName))) {
      repeatedRead(in, list, cb);
    }

  }

  /**
   * 分析Excel文件并处理数据
   *
   * @param <T> 数据类型
   * @param excelReader Excel读取器
   * @param list 数据类型的Class对象列表
   * @param cb 处理数据的回调函数
   *
   * 示例：
   * ExcelReader excelReader = EasyExcel.read(new FileInputStream("example.xlsx")).build();
   * List<Class<ExampleData>> list = Arrays.asList(ExampleData.class);
   * DataImport.analysis(excelReader, list, dataList -> {
   *     // 处理数据
   * });
   */
  private static <T> void analysis(ExcelReader excelReader, List<Class<T>> list, Consumer<List<T>> cb) {

    List<ReadSheet> readSheets = new ArrayList<>();
    LambdaUtils.forEach(list, (index, item) -> {
      ReadSheet readSheet = EasyExcel.readSheet(index).head(item).registerReadListener(new ReadListener<T>() {
        List<T> list = new LinkedList<>();

        @Override
        public void invoke(T data, AnalysisContext context) {
          list.add(data);
        }

        @SneakyThrows
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
          cb.accept(list);
        }
      }).build();
      readSheets.add(readSheet);
    });
    excelReader.read(readSheets);

  }

}
