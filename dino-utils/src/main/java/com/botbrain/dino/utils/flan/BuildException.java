package com.botbrain.dino.utils.flan;

public class BuildException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4455205082406118094L;

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

}
