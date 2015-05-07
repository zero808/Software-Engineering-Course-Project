package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that describes a literal content.
 * 
 * It has a single number as its value.
 * 
 * It's one of the many types of content
 * that can be put in cells.
 */

public class Literal extends Literal_Base implements Argument {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Literal}
	 */
	
	public Literal() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Literal}
	 * 
	 * @param {number} value The number that is the value of 
	 * this literal.
	 */

	public Literal(int value) {
		super();
		/** @private */
		super.setVal(value);
	}
	
	/**
	 * The string representation of a Literal.
	 * 
	 * @return {String} The string that represents the value of
	 * the literal.
	 */

	@Override
	public String toString() {
		return Integer.toString(super.getVal());
	}
	
	/**
	 * Returns the value of this literal.
	 * 
	 * @return {number} The value of the literal.
	 */

	@Override
	public int getValue() {
		return super.getVal();
	}
	
	/**
	 * Export this literal to a XML document.
	 * 
	 * @return {XML Element} The element describing the literal.
	 */

	@Override
	public Element exportToXML() {
		Element element = new Element("literal");
		element.setAttribute("value", Integer.toString(super.getVal()));
		return element;
	}
	
	/**
	 * Import this literal from a XML document.
	 * 
	 * @param {XML Element} element The element that has the literal's data.
	 */

	@Override
	public void importFromXML(Element element) {
		setVal(Integer.parseInt(element.getAttribute("value").getValue()));
	}
	
	/**
	 * Method that returns this as a type of content.
	 * Used in binary functions.
	 * 
	 * @return {Content} Returns this literal as a content,
	 * part of one of the arguments in a binary function.
	 */
	
	@Override
	public Content retrieveContent() {
		return this;
	}
}// End Literal class
