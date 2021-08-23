package org.dinospring.core.modules.appclient;

import java.util.ArrayList;
import java.util.List;

import org.dinospring.commons.sys.Tenant;

import lombok.Data;

@Data
public class AppClient {
  String id;
  String name;
  List<Tenant> bindTenants = new ArrayList<>();
}
