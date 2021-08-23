package org.dinospring.commons.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contact implements Serializable {

  @Schema(description = "联系人名字")
  private String name;

  @Schema(description = "联系人电话")
  private String phone;
}
