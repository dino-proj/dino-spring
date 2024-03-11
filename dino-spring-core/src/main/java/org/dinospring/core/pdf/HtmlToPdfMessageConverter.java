// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.pdf;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.dinospring.commons.function.Resolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.openhtmltopdf.extend.FSUriResolver;
import com.openhtmltopdf.latexsupport.LaTeXDOMMutator;
import com.openhtmltopdf.mathmlsupport.MathMLDrawer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.openhtmltopdf.swing.NaiveUserAgent.DefaultUriResolver;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2024-02-02 05:38:40
 */

@Component
@ConditionalOnClass(PdfRendererBuilder.class)
@Slf4j
public class HtmlToPdfMessageConverter extends AbstractHttpMessageConverter<PdfFromHtmlModel> {
  private static final String CLASS_SVG = "com.openhtmltopdf.svgsupport.BatikSVGDrawer";
  private static final String CLASS_MATH = "com.openhtmltopdf.mathmlsupport.MathMLDrawer";
  private static final String CLASS_LATEX = "com.openhtmltopdf.latexsupport.LaTeXDOMMutator";

  public HtmlToPdfMessageConverter() {
    super(MediaType.APPLICATION_PDF);
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return PdfFromHtmlModel.class.isAssignableFrom(clazz);
  }

  @Override
  protected boolean canRead(MediaType mediaType) {
    return false;
  }

  @Override
  protected PdfFromHtmlModel readInternal(Class<? extends PdfFromHtmlModel> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    throw new UnsupportedOperationException("Unimplemented method 'readInternal'");
  }

  @Override
  protected void writeInternal(PdfFromHtmlModel model, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {

    String fileName = model.getFileName();
    if (StringUtils.isNotBlank(fileName)) {
      if (log.isDebugEnabled()) {
        log.debug("to view pdf in browser, set Content-Disposition to inline");
      }
      outputMessage.getHeaders().add("Content-Disposition", "inline; filename=" + fileName + ".pdf");
    }

    // 写出pdf
    var pdfBuilder = this.createBuilder(model);

    pdfBuilder.toStream(outputMessage.getBody());

    pdfBuilder.run();
  }

  private PdfRendererBuilder createBuilder(PdfFromHtmlModel model) throws IOException {
    PdfRendererBuilder builder = new PdfRendererBuilder();
    builder.useFastMode();

    builder.withHtmlContent(model.getHtml(), model.getWorkDir());

    var pdDocument = new PDDocument(MemoryUsageSetting.setupMixed(1024 * 1024L));
    pdDocument.setAllSecurityToBeRemoved(false);

    // add password protection
    if (model.isProtect() || model.isReadOnly() || StringUtils.isNotBlank(model.getUserPassword())) {
      AccessPermission ap = new AccessPermission();
      if (model.isReadOnly()) {
        ap.setCanModify(false);
      }
      if (model.isProtect()) {
        ap.setCanAssembleDocument(false);
        ap.setCanExtractForAccessibility(false);
      }
      pdDocument.protect(new StandardProtectionPolicy(null, model.getUserPassword(), ap));
    }

    // 添加创建者
    pdDocument.getDocumentInformation().setCreator(model.getCreator());
    builder.withProducer(model.getProducer());

    builder.usePDDocument(pdDocument);

    // 如果找到BatikSVGDrawer.class，这添加svg支持
    if (ClassUtils.isPresent(CLASS_SVG, this.getClass().getClassLoader())) {
      builder.useSVGDrawer(new BatikSVGDrawer());
    }
    // 如果找到MathMLDrawer.class，这添加MathML支持
    if (ClassUtils.isPresent(CLASS_MATH, this.getClass().getClassLoader())) {
      builder.useMathMLDrawer(new MathMLDrawer());
    }
    // 如果找到LaTeXDOMMutator.class，这添加LaTeX支持
    if (ClassUtils.isPresent(CLASS_LATEX, this.getClass().getClassLoader())) {
      builder.addDOMMutator(LaTeXDOMMutator.INSTANCE);
    }

    // 添加自定义的uri解析器
    if (model.getUriResolver() != null) {
      builder.useUriResolver(new UriResolverWrraper(model.getUriResolver()));
    }

    return builder;
  }

  private static class UriResolverWrraper implements FSUriResolver {

    private static final DefaultUriResolver DEFAULT_URI_RESOLVER = new DefaultUriResolver();

    private final Resolver<String> uriResolver;

    public UriResolverWrraper(Resolver<String> uriResolver) {
      this.uriResolver = uriResolver;
    }

    @Override
    public String resolveURI(String baseUri, String uri) {
      if (this.uriResolver.isSupported(uri)) {
        return this.uriResolver.resolve(uri);
      } else {
        return DEFAULT_URI_RESOLVER.resolveURI(baseUri, uri);
      }
    }
  }
}
