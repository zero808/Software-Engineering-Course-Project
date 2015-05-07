package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes an AVG function.
 * 
 * It's a unary function, therefore it only has 1 argument.
 * 
 * Returns the average of the values of the cells in its range.
 * 
 * It's one of the many operations that can be made on a
 * spreadsheet.
 */

public class Avg extends Avg_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Avg}
	 */

	public Avg() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Avg}
	 * 
	 * @param {Range} range The function's only argument.
	 */

	public Avg(Range range) {
		super();
		/** @private */ 
		super.setRange(range);
	}
	
	/**
	 * Returns the average of the values of the cells in
	 * the range given.
	 * 
	 * @throws InvalidArgumentsException
	 * @return {number} The average of the values.
	 */
	
	@Override
	public int getValue() throws InvalidArgumentsException {
		int total = 0;
		for (Cell cell : getCell().getSpreadsheet().getCellsInRange(getRange())) {
			if (cell.hasValidResult())
				total += cell.getContent().getValue();
			else
				throw new InvalidArgumentsException();
		}
		return total / getCell().getSpreadsheet().getCellsInRange(getRange()).size();
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
		Element avg = new Element("avg");

		avg.addContent(getRange().exportToXML());

		uf.addContent(avg);
		f.addContent(uf);

		return f;
	}
	
	/**
	 * The string representation of an AVG function.
	 * 
	 * @return {String} The string that represents the function.
	 */
	
	@Override
	public String toString() {
		return "AVG(" + getRange().toString() + ")";
	}
}//End Avg class
