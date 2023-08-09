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

package org.dinospring.core.modules.oss.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.dinospring.core.modules.oss.BucketMeta;
import org.dinospring.core.modules.oss.ObjectMeta;
import org.dinospring.core.modules.oss.OssService;
import org.dinospring.core.modules.oss.config.LocalOssProperties;

/**
 *
 * @author Cody LU
 * @author JL
 */

public class LocalOssService implements OssService {

  private Path basePath;

  public LocalOssService(@Nonnull LocalOssProperties properties) throws IOException {
    basePath = Path.of(properties.getBaseDir());
    PathUtils.createParentDirectories(basePath);
  }

  @Override
  public boolean hasBucket(String bucketName) throws IOException {
    var f = FileUtils.getFile(basePath.toFile(), bucketName);
    return f.exists() && f.isDirectory();
  }

  @Override
  public void createBucket(String bucketName) throws IOException {
    var f = FileUtils.getFile(basePath.toFile(), bucketName);
    FileUtils.forceMkdir(f);
  }

  @Override
  public void deleteBucket(String bucketName) throws IOException {
    var f = FileUtils.getFile(basePath.toFile(), bucketName);
    FileUtils.forceDelete(f);

  }

  @Override
  public List<BucketMeta> listBuckets() throws IOException {
    var dirs = FileUtils.listFiles(basePath.toFile(), FileFilterUtils.directoryFileFilter(),
        FileFilterUtils.falseFileFilter());
    return dirs.stream().map(f -> BucketMeta.of(f.getName(), f.lastModified())).collect(Collectors.toList());
  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName) throws IOException {
    return listObjects(bucketName, null);
  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName, String dir) throws IOException {
    var dirFile = FileUtils.getFile(basePath.toFile(), bucketName);
    if (dir != null) {
      dirFile = FileUtils.getFile(dirFile, dir);
    }
    var it = FileUtils.iterateFiles(dirFile, FileFilterUtils.trueFileFilter(), FileFilterUtils.falseFileFilter());

    return IterableUtils.transformedIterable(IteratorUtils.asIterable(it),
        f -> f.isDirectory() ? ObjectMeta.ofDir(f.getName(), f.lastModified())
            : ObjectMeta.ofFile(f.getName(), f.length(), f.lastModified()));
  }

  @Override
  public ObjectMeta statObject(String bucket, String objectName) throws IOException {
    var file = FileUtils.getFile(basePath.toFile(), bucket, objectName);
    if (!file.exists()) {
      throw new FileNotFoundException(bucket + ": " + objectName);
    }
    return ObjectMeta.ofFile(file.getName(), file.length(), file.lastModified());
  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName) throws IOException {
    var file = FileUtils.getFile(basePath.toFile(), bucket, objectName);

    if (!file.createNewFile()) {
      throw new FileAlreadyExistsException(bucket + ": " + objectName);
    }
    try (var out = new FileOutputStream(file, false)) {
      IOUtils.copy(stream, out);
    }
  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName, String contentType) throws IOException {
    var file = FileUtils.getFile(basePath.toFile(), bucket, objectName);

    if (!file.createNewFile()) {
      throw new FileAlreadyExistsException(bucket + ": " + objectName);
    }
    try (var out = new FileOutputStream(file, false)) {
      IOUtils.copy(stream, out);
    }
  }

  @Override
  public InputStream getObject(String bucket, String objectName) throws IOException {
    var file = FileUtils.getFile(basePath.toFile(), bucket, objectName);
    if (!file.exists()) {
      throw new FileNotFoundException(bucket + ": " + objectName);
    }
    return new FileInputStream(file);
  }

  @Override
  public int trasferObject(String bucket, String objectName, OutputStream out) throws IOException {
    var file = FileUtils.getFile(basePath.toFile(), bucket, objectName);
    if (!file.exists()) {
      throw new FileNotFoundException(bucket + ": " + objectName);
    }
    try (var in = new FileInputStream(file)) {
      return IOUtils.copy(in, out);
    }
  }

  @Override
  public void deleteObject(String bucket, String objectName) throws IOException {
    var objPath = basePath.resolve(bucket).resolve(objectName);
    if (Files.isRegularFile(objPath)) {
      Files.deleteIfExists(objPath);
    }

  }

  @Override
  public void moveObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
      throws IOException {
    var srcFile = FileUtils.getFile(basePath.toFile(), srcBucket, srcObjectName);
    if (!srcFile.exists()) {
      throw new FileNotFoundException(srcBucket + ": " + srcObjectName);
    }
    var destFile = FileUtils.getFile(basePath.toFile(), destBucket, destObjectName);
    if (destFile.exists()) {
      throw new FileAlreadyExistsException(destBucket + ": " + destObjectName);
    }
    FileUtils.moveFile(srcFile, destFile);

  }

  @Override
  public void copyObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
      throws IOException {
    var srcFile = FileUtils.getFile(basePath.toFile(), srcBucket, srcObjectName);
    if (!srcFile.exists()) {
      throw new FileNotFoundException(srcBucket + ": " + srcObjectName);
    }
    var destFile = FileUtils.getFile(basePath.toFile(), destBucket, destObjectName);
    if (destFile.exists()) {
      throw new FileAlreadyExistsException(destBucket + ": " + destObjectName);
    }
    FileUtils.copyFile(srcFile, destFile);

  }

  @Override
  public String getPresignedObjectUrl(String bucket, String objectName) {
    return null;
  }

  @Override
  public String getPresignedObjectUrl(String bucket, String objectName, Integer timeout, TimeUnit unit) {
    return null;
  }

}
