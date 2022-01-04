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

package org.dinospring.core.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.png.PngChunkType;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.avi.AviDirectory;
import com.drew.metadata.bmp.BmpHeaderDirectory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mov.media.QuickTimeVideoDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import com.drew.metadata.mp4.media.Mp4VideoDirectory;
import com.drew.metadata.png.PngDirectory;
import com.drew.metadata.wav.WavDirectory;
import com.drew.metadata.webp.WebpDirectory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

/**
 *
 * @author tuuboo
 */

@UtilityClass
@Slf4j
public class MultiMediaUtils {

  /**
   * 获取视频、图片、音频的元信息
   * @param input
   * @return
   */
  public static MediaInfo extractMediaInfo(BufferedInputStream input) {

    try {
      var fileType = FileTypeDetector.detectFileType(input);
      if (fileType.getMimeType() == null || fileType == FileType.Zip) {
        return null;
      }
      var media = new MediaInfo(fileType.getName(), fileType.getMimeType());

      if (!isMultiMediaMime(fileType.getMimeType())) {
        return media;
      }

      if (media.isAudio()) {
        media.setDuration(getAudioDuration(input) / 1000L);
        return media;
      }

      var meta = ImageMetadataReader.readMetadata(input);

      fillMediaInfo(meta, media);
      return media;

    } catch (IOException | ImageProcessingException | EncoderException | MetadataException e) {
      log.error("error occurred", e);
      return null;
    }
  }

  private static void fillMediaInfo(Metadata meta, MediaInfo media) throws MetadataException {
    for (var dir : meta.getDirectories()) {
      if (dir instanceof JpegDirectory) {
        var jpegDir = (JpegDirectory) dir;
        media.setWidth(jpegDir.getImageWidth());
        media.setHeight(jpegDir.getImageHeight());
        return;
      } else if (dir instanceof PngDirectory) {
        var pngDir = (PngDirectory) dir;
        if (pngDir.getPngChunkType().equals(PngChunkType.IHDR)) {
          media.setWidth(dir.getInt(PngDirectory.TAG_IMAGE_WIDTH));
          media.setHeight(dir.getInt(PngDirectory.TAG_IMAGE_HEIGHT));
        }
        return;
      } else if (dir instanceof GifHeaderDirectory) {
        media.setWidth(dir.getInt(GifHeaderDirectory.TAG_IMAGE_WIDTH));
        media.setHeight(dir.getInt(GifHeaderDirectory.TAG_IMAGE_HEIGHT));
        return;
      } else if (dir instanceof BmpHeaderDirectory) {
        media.setWidth(dir.getInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH));
        media.setHeight(dir.getInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT));
        return;
      } else if (dir instanceof WebpDirectory) {
        media.setWidth(dir.getInt(WebpDirectory.TAG_IMAGE_WIDTH));
        media.setHeight(dir.getInt(WebpDirectory.TAG_IMAGE_HEIGHT));
        return;
      } else if (dir instanceof AviDirectory) {
        media.setWidth(dir.getInt(AviDirectory.TAG_WIDTH));
        media.setHeight(dir.getInt(AviDirectory.TAG_HEIGHT));
        media.setDuration(dir.getLong(AviDirectory.TAG_DURATION));
        media.setResolution(calResolution(media.getWidth(), media.getHeight()));
        return;
      } else if (dir instanceof QuickTimeDirectory) {
        if (dir instanceof QuickTimeVideoDirectory) {
          media.setWidth(dir.getInt(QuickTimeVideoDirectory.TAG_WIDTH));
          media.setHeight(dir.getInt(QuickTimeVideoDirectory.TAG_HEIGHT));
          media.setResolution(calResolution(media.getWidth(), media.getHeight()));
        } else if (dir.hasTagName(QuickTimeDirectory.TAG_DURATION)) {
          media.setDuration(dir.getLong(QuickTimeDirectory.TAG_DURATION) / 1000L);
        }
      } else if (dir instanceof Mp4Directory) {
        if (dir instanceof Mp4VideoDirectory) {
          media.setWidth(dir.getInt(Mp4VideoDirectory.TAG_WIDTH));
          media.setHeight(dir.getInt(Mp4VideoDirectory.TAG_HEIGHT));
          media.setResolution(calResolution(media.getWidth(), media.getHeight()));
        } else if (dir.hasTagName(Mp4Directory.TAG_DURATION)) {
          media.setDuration(dir.getLong(Mp4Directory.TAG_DURATION) / 1000L);
        }
      } else if (dir instanceof WavDirectory) {
        media.setDuration(dir.getLong(WavDirectory.TAG_DURATION) / 1000L);
        return;
      }
    }
  }

  /**
   * 计算视频文件的长度
   * @param input
   * @return
   * @throws IOException
   * @throws EncoderException
   */
  public static long getAudioDuration(InputStream input) throws IOException, EncoderException {
    var tempFile = FileUtils.getFile(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
    FileUtils.copyToFile(input, tempFile);
    var mediaInfo = new MultimediaObject(tempFile).getInfo();
    return mediaInfo.getDuration();
  }

  /**
   * 计算视频的清晰度
   *
   * @param width
   * @param height
   * @return ["240p", "360p", "480p", "720p", "1080p", "2k", "4k", "8k", "16k", "32k", "64k", "128k"]
   */
  public String calResolution(int width, int height) {
    var min = Math.min(width, height);
    if (min >= 71820) {
      return "128k";
    } else if (min >= 35640) {
      return "64k";
    } else if (min >= 17820) {
      return "32k";
    } else if (min >= 8640) {
      return "16k";
    } else if (min >= 4320) {
      return "8k";
    } else if (min >= 2160) {
      return "4k";
    } else if (min >= 1080) {
      return "1080";
    } else if (min >= 720) {
      return "720";
    } else if (min >= 480) {
      return "480";
    } else if (min >= 360) {
      return "360";
    } else {
      return "240";
    }
  }

  /**
   * 判断MimeType是否是图片
   * @param mime
   * @return
   */
  public static boolean isImageMime(String mime) {
    return StringUtils.startsWith(mime, "image/");
  }

  /**
   * 判断MimeType是否是视频
   * @param mime
   * @return
   */
  public static boolean isVideoMime(String mime) {
    return StringUtils.startsWith(mime, "video/");
  }

  /**
   * 判断MimeType是否是音频
   * @param mime
   * @return
   */
  public static boolean isAudioMime(String mime) {
    return StringUtils.startsWith(mime, "audio/");
  }

  /**
   * 判断MemeType是否是多媒体，即是否是图片、音频、视频中的一种
   * @param mime
   * @return
   */
  public static boolean isMultiMediaMime(String mime) {
    return StringUtils.startsWithAny(mime, "audio/", "video/", "image/");
  }

  @Data
  public static class MediaInfo {
    private final String typeName;
    private final String mime;
    //宽度
    private Integer width;
    //高度
    private Integer height;
    //时长(秒)
    private Long duration;
    //清晰度["240", "360", "480", "720", "1080", "2k", "4k", "8k"]
    private String resolution;

    public boolean isImage() {
      return isImageMime(mime);
    }

    public boolean isAudio() {
      return isAudioMime(mime);
    }

    public boolean isVideo() {
      return isVideoMime(mime);
    }

    public boolean isMultiMedia() {
      return isMultiMediaMime(mime);
    }
  }
}