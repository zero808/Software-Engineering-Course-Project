package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that abstracts all binary functions.
 * 
 * All functions that are binary must extend this class.
 */

public abstract class Binary extends Binary_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Binary}
	 */

	public Binary() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Binary}
	 * 
	 * @param {Argument} arg1 The function's first argument.
	 * @param {Argument} arg2 The function's second argument.
	 */

	public Binary(Argument arg1, Argument arg2) {
		Content a1 = (Content)arg1;
		Content a2 = (Content)arg2;
		a1.setBinary1(this);
		a2.setBinary1(this);
		
		/** @private */ 
		setArg1((Content)arg1);
		/** @private */ 
		setArg2((Content)arg2);
	}
	
	/**
	 * Method that deletes a binary function.
	 * 
	 * To delete a binary function, given the architecture of
	 * the application, first its required to sever all the
	 * connections the function has. 
	 * More specifically, to each of its arguments and to the 
	 * spreadsheet cell it is in.
	 * After doing that, then the object is deleted.
	 */
	
	public void delete() {
		Content a1 = getArg1();
		Content a2 = getArg2();
		
		a1.setBinary1(null);
		a2.setBinary1(null);
		setArg1(null);
		setArg2(null);
		a1.delete();
		a2.delete();
		
		setCell(null);
		deleteDomainObject();
	}
	
	/**
	 * Abstract method that returns the specific
	 * value of each type of binary function.
	 * 
	 * @return {number} The value returned by the function.
	 */

	public abstract int getValue();
	
	/**
	 * Abstract method that exports a specific binary function
	 * to a XML document.
	 * 
	 * @return {XML Element} The element describing the binary function.
	 */

	public abstract Element exportToXML();
	
	/**
	 * Abstract method that imports a specific binary function
	 * from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the binary
	 * functions data.
	 */

	public abstract void importFromXML(Element cellElement);
}//End Binary class
