package com.botbrain.dino.utils.flan.function.imp;

import com.botbrain.dino.utils.flan.AbandonedException;
import com.botbrain.dino.utils.flan.function.Context;
import com.botbrain.dino.utils.flan.function.Description;

@Description(value = "${name} is used to stop processing the following expresions. throw AbandonedException", usage = "example1: '${name}()'. example2: 'expressionA() or ${name}()'. the rest expression will not excuted. ")
public class FNBreak extends Base {

	@Override
	public String eval(Context context, String val) throws AbandonedException {
		throw new AbandonedException("break");
	}

}
