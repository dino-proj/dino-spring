package com.botbrain.dino.utils.flan.function;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description the function.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
	String value() default "${name} is undocumented";

	String usage();
}
