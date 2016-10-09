package net.hakugyokurou.fds.node;

public class InvalidExpressionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6489993342427081122L;

	public InvalidExpressionException() {
		super();
	}
	
	public InvalidExpressionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidExpressionException(String message) {
		super(message);
	}

	public InvalidExpressionException(Throwable cause) {
		super(cause);
	}
}
