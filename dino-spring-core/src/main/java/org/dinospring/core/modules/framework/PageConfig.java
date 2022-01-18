package org.dinospring.core.modules.framework;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.dinospring.commons.annotion.AnnotionedJsonTypeIdResolver;

/**
 *
 * @author tuuboo
 */

@JsonTypeInfo(use = Id.CUSTOM, include = As.PROPERTY, visible = true, property = "@t")
@JsonTypeIdResolver(AnnotionedJsonTypeIdResolver.class)
public interface PageConfig extends Component {

}
