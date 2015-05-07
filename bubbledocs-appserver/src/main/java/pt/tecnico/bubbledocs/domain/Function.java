package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that abstracts all functions.
 * 
 * All functions must extend this class.
 */

public abstract class Function extends Function_Base {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Function}
	 */
	
	public Function() {
		super();
	}
	
	/**
	 * Abstract method that returns the specific
	 * value of each type of function.
	 * 
	 * @return {number} The value returned by the function.
	 */
	
    public abstract int getValue();
    
    /**
	 * Abstract method that exports a specific function
	 * to a XML document.
	 * 
	 * @return {XML Element} The element describing the function.
	 */
	
	public abstract Element exportToXML();
	
	/**
	 * Abstract method that imports a specific function
	 * from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the
	 * functions data.
	 */
	
	public abstract void importFromXML(Element cellElement);
	
	/**
	 * Abstract method that deletes a function.
	 */
	
	public abstract void delete();
}// End Function class
