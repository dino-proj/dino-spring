// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.importandexport.dataimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.dinospring.commons.utils.LambdaUtils;

import lombok.SneakyThrows;

/**
 * @author JL
 * @Date: 2021/9/30
 */
public class DataImport {

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
        in.close();
      }
    }).sheet().doRead();
  }

  public static <T> void doRead(String pathName, Class<T> cls, Consumer<List<T>> cb) throws IOException {
    InputStream in = null;
    try {
      in = new FileInputStream(new File(pathName));
      doRead(in, cls, cb);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static <T> void repeatedRead(InputStream in, List<Class<T>> list, Consumer<List<T>> cb)
      throws IOException {
    try {
      ExcelReader excelReader = EasyExcel.read(in).build();
      analysis(excelReader, list, cb);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      in.close();
    }

  }

  public static <T> void repeatedRead(String pathName, List<Class<T>> list, Consumer<List<T>> cb) throws IOException {
    InputStream in = null;
    try {
      in = new FileInputStream(new File(pathName));
      repeatedRead(in, list, cb);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      in.close();
    }
  }

  private static <T> void analysis(ExcelReader excelReader, List<Class<T>> list, Consumer<List<T>> cb) {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (excelReader != null) {
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
      }
    }

  }

}
