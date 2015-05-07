package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes an PRD function.
 * 
 * It's a unary function, therefore it only has 1 argument.
 * 
 * Returns the product of the values of the cells in its range.
 * 
 * It's one of the many operations that can be made on a
 * spreadsheet.
 */

public class Prd extends Prd_Base {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Prd}
	 */
	
	public Prd() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Prd}
	 * 
	 * @param {Range} range The function's only argument.
	 */

	public Prd(Range range) {
		super();
		super.setRange(range);
	}
	
	/**
	 * Returns the product of the values of the cells in
	 * the range given.
	 * 
	 * @throws InvalidArgumentsException
	 * @return {number} The product of the values.
	 */

	@Override
	public int getValue() throws InvalidArgumentsException {
		int total = 1;
		for (Cell cell : getCell().getSpreadsheet().getCellsInRange(getRange())) {
			if (cell.hasValidResult())
				total *= cell.getContent().getValue();
			else
				throw new InvalidArgumentsException();
		}
		return total;
	}
	
	/**
	 * Export this function to a XML document.
	 * 
	 * @return {XML Element} The element describing the function.
	 */

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element uf = new Element("unary_function");
		Element prd = new Element("prd");

		prd.addContent(getRange().exportToXML());

		uf.addContent(prd);
		f.addContent(uf);

		return f;
	}
	
	/**
	 * The string representation of an PRD function.
	 * 
	 * @return {String} The string that represents the function.
	 */
	
	@Override
	public String toString() {
		return "PRD(" + getRange().toString() + ")";
	}
}// End Prd class
