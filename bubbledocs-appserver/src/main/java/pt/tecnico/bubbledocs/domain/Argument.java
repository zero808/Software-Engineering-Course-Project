package pt.tecnico.bubbledocs.domain;

/**
 * Interface that hides the multiple combinations of content
 * that functions can receive as arguments, and how that's
 * implemented.
 */

public interface Argument {

	public abstract Content retrieveContent();
	
}// End Argument class
