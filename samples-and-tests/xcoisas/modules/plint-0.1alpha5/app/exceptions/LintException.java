package exceptions;

public class LintException extends Exception{

	private static final long serialVersionUID = 1L;

	public LintException() {
	}

	public LintException(String message) {
		super(message);
	}

	public LintException(Throwable cause) {
		super(cause);
	}

	public LintException(String message, Throwable cause) {
		super(message, cause);
	}
}
