package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that describes a reference content.
 * 
 * It has a cell as its argument, to where 
 * it refers to.
 * 
 * It's one of the many types of content
 * that can be put in cells.
 */

public class Reference extends Reference_Base implements Argument {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Reference}
	 */
	
	public Reference() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Reference}
	 * 
	 * @param {Cell} c The cell that the reference refers.
	 */

	public Reference(Cell c) {
		super();
		/** @private */
		super.setReferencedCell(c);
		c.addReference(this);
	}
	
	/**
	 * The string representation of a Reference.
	 * 
	 * @return {String} The string that represents the value of
	 * the reference, which is the value of the cell that the
	 * reference points to.
	 */
	
	@Override
	public String toString() {
		if(super.getReferencedCell().getContent() == null) 
			return "#VALUE";
		else
			return super.getReferencedCell().getContent().toString();
	}
	
	/**
	 * Returns the value of this reference.
	 * 
	 * @return {number} The value of the cell that the
	 * reference points to.
	 */
	
	@Override
	public int getValue() {
		return super.getReferencedCell().getContent().getValue();
	}
	
	/**
	 * Method that deletes a reference.
	 * 
	 * To delete a reference, given the architecture of
	 * the application, first its required to sever all the
	 * connections this reference has.
	 * More specifically, to the cell which the reference points
	 * to, and to the cell of which the reference is in.
	 * 
	 * After doing that, then the object is deleted.
	 */
	
	@Override
	public void delete() {
		
		if(super.getReferencedCell() != null) {
			super.getReferencedCell().removeReference(this);
		}
		
		super.setCell(null);
		deleteDomainObject();
	}
	
	/**
	 * Export this reference to a XML document.
	 * 
	 * @return {XML Element} The element describing the reference.
	 */

	@Override
	public Element exportToXML() {
 		Element element = new Element("reference");
 		
 		if(getReferencedCell() != null) {
 			element.setAttribute("row", Integer.toString(super.getReferencedCell().getRow()));
	 	 	element.setAttribute("collumn", Integer.toString(super.getReferencedCell().getCollumn()));
	 	 	
 			if(getCell() != null) {
 				element.setAttribute("desc", "Reference not saved in any cell of spreadsheet");
 			}
 		} else {
 			element.setAttribute("cont", "#VALUE");
 		}
 	
		return element;
	}
	
	/**
	 * Import this reference from a XML document.
	 * 
	 * @param {XML Element} element The element that has the reference's data.
	 */

	@Override
	public void importFromXML(Element element) {
		
		int row = Integer.parseInt(element.getAttribute("row").getValue());
		int collumn = Integer.parseInt(element.getAttribute("collumn").getValue());
		
		Spreadsheet ss;
		
		if(getBinary1()!=null) {
			ss = getBinary1().getCell().getSpreadsheet();
		} else {
			ss = getCell().getSpreadsheet();
		} 
		
		setReferencedCell(ss.getCellByCoords(row, collumn));	
	}
	
	/**
	 * Method that returns this as a type of content.
	 * Used in binary functions.
	 * 
	 * @return {Content} Returns this reference as a content,
	 * part of one of the arguments in a binary function.
	 */

	@Override
	public Content retrieveContent() {
		return this;
	}
}// End Reference class
