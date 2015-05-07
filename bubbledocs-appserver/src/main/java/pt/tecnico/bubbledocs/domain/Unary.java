package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that abstracts all unary functions.
 * 
 * All functions that are unary must extend this class.
 */

public abstract class Unary extends Unary_Base {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Unary}
	 */
	
	public Unary() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Unary}
	 * 
	 * @param {Range} range The function's only argument.
	 */

	public Unary(Range range) {
		super();
		/** @private */
		super.setRange(range);
	}
	
	/**
	 * Abstract method that returns the specific
	 * value of each type of unary function.
	 * 
	 * @return {number} The value returned by the function.
	 */
	
	public abstract int getValue();
	
	/**
	 * Abstract method that exports a specific unary function
	 * to a XML document.
	 * 
	 * @return {XML Element} The element describing the unary function.
	 */

	public abstract Element exportToXML();
	
	/**
	 * Import this function from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the function's data.
	 */
	
	public void importFromXML(Element cellElement) {
		Range r = new Range();
		
		r.setUnary(this);
		this.setRange(r);
		
		Element element;
		if ((element = cellElement.getChild("range")) != null)
			r.importFromXML(element);
	}
	
	/**
	 * Method that deletes a unary function.
	 * 
	 * To delete a unary function, given the architecture of
	 * the application, first its required to sever all the
	 * connections the function has. 
	 * More specifically, to the range argument that it has and
	 * to the cell it belongs in.
	 * After doing that, then the object is deleted.
	 */

	@Override
	public void delete() {
		Range r = getRange();
		setRange(null);
		
		r.setUnary(null);
		r.delete();
		
		setCell(null);
		
		deleteDomainObject();
	}
}// End Unary class
