package pt.tecnico.bubbledocs.exception;

/**
 * Class that abstracts all of the possible
 * exceptions that could occur within the
 * BubbleDocs.
 * 
 * All specific exceptions must extend this class.
 */

public abstract class BubbleDocsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {BubbleDocsException}
	 */
	
	public BubbleDocsException() {
		//Just needs to exist.
	}
}// End BubbleDocsException class
