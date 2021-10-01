package com.botbrain.dino.utils.flan.function.imp;

import javax.servlet.http.HttpServletRequest;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;
import com.botbrain.dino.utils.ip.IpUtils;

import org.apache.commons.lang3.StringUtils;

@Description(value = "${name}(), get the real ip", usage = "${name}()")
public class FNIP extends Base {

  @Override
  public String eval(Context context, String val) throws AbandonedException {
    HttpServletRequest req = context.arg(0, HttpServletRequest.class);
    return getIp(req);
  }

  private final String getIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    ip = getOuterIp(ip);
    if (ip == null || ip.length() == 0) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  private final String getOuterIp(String xfIp) {
    if (xfIp == null) {
      return null;
    }

    if (xfIp.indexOf(',') > -1) {
      String[] ips = StringUtils.split(xfIp, ",");
      for (String ip : ips) {
        ip = StringUtils.trim(ip);
        if (StringUtils.isNotBlank(ip) && IpUtils.isOuter(ip)) {
          return ip;
        }
      }
    }

    return xfIp;
  }

}
