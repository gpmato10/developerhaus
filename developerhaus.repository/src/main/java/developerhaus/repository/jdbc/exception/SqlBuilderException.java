package developerhaus.repository.jdbc.exception;

public class SqlBuilderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2448733327977455894L;

	public SqlBuilderException(String msg) {
		super(msg);
	}

	public SqlBuilderException(String msg, Exception e) {
		super(msg, e);
	}

}
