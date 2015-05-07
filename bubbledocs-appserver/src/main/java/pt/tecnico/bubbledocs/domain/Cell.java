package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes a Cell.
 * 
 * Spreadsheets are made of many cells. Each of them
 * can contain a particular type of content.
 */

public class Cell extends Cell_Base {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Cell}
	 */
	
	public Cell() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Cell}
	 * 
	 * @param {number} row The row of the cell.
	 * @param {number} col The column of the cell.
	 * @param {Boolean} wProtected If the cell is protected or not.
	 * @throws InvalidArgumentsException
	 */

	public Cell(int row, int col, boolean wProtected) throws InvalidArgumentsException {
		super();
		if (row > 0 && col > 0) {
			/** @private */ 
			super.setRow(row);
			/** @private */ 
			super.setCollumn(col);
		} else
			throw new InvalidArgumentsException();
		
		/** @private */ 
		super.setWProtected(wProtected);
	}
	
	/**
	 * Export this cell to a XML document.
	 * 
	 * @return {XML Element} The element describing the cell.
	 */

	public Element exportToXML() {
		Element element = new Element("cell");

		element.setAttribute("row", Integer.toString(super.getRow()));
		element.setAttribute("collumn", Integer.toString(super.getCollumn()));
		element.setAttribute("wprotected", Boolean.toString(super.getWProtected()));

		Element cont = new Element("content");
		element.addContent(cont);

		if (super.getContent() != null)
			cont.addContent(super.getContent().exportToXML());

		return element;
	}
	
	/**
	 * Import this cell from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the cell's data.
	 */

	public void importFromXML(Element cellElement) {
		setRow(Integer.parseInt(cellElement.getAttribute("row").getValue()));
		setCollumn(Integer.parseInt(cellElement.getAttribute("collumn").getValue()));
		setWProtected(Boolean.parseBoolean(cellElement.getAttribute("wprotected").getValue()));

		Element content = cellElement.getChild("content");

		if (content != null) {

			Element subcontent;
			if ((subcontent = content.getChild("reference")) != null) {
				Reference r = new Reference();
				r.setCell(this);
				r.importFromXML(subcontent);
			} else {
				if ((subcontent = content.getChild("literal")) != null) {
					Literal l = new Literal();
					l.setCell(this);
					l.importFromXML(subcontent);
				} else {
					if ((subcontent = content.getChild("function")) != null) {
						Element functionType;
						if ((functionType = subcontent.getChild("binary_function")) != null) {
							Element functionName;
							if ((functionName = functionType.getChild("add")) != null) {
								Add add = new Add();
								add.setCell(this);
								add.importFromXML(functionName);
							} else {
								if ((functionName = functionType.getChild("sub")) != null) {
									Sub sub = new Sub();
									sub.setCell(this);
									sub.importFromXML(functionName);
								} else {
									if ((functionName = functionType.getChild("div")) != null) {
										Div div = new Div();
										div.setCell(this);
										div.importFromXML(functionName);
									} else {
										if ((functionName = functionType.getChild("mul")) != null) {
											Mul mul = new Mul();
											mul.setCell(this);
											mul.importFromXML(functionName);
										}
									}
								}
							}

						} else {
							if ((functionType = subcontent.getChild("unary_function")) != null) {
								Element functionName;
								if ((functionName = functionType.getChild("avg")) != null) {
									Avg avg = new Avg();
									avg.setCell(this);
									avg.importFromXML(functionName);
								} else {
									if ((functionName = functionType.getChild("prd")) != null) {
										Prd prd = new Prd();
										prd.setCell(this);
										prd.importFromXML(functionName);
									} 
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method that checks if a cell has a valid value.
	 * 
	 * @throws InvalidArgumentsException
	 * @return {Boolean} True if yes, false otherwise.
	 */

	public boolean hasValidResult() throws InvalidArgumentsException {
		if (getContent() == null) 
			throw new InvalidArgumentsException();
		if (getContent().toString().equals("#VALUE")) 
			throw new InvalidArgumentsException();
		
		return true;
	}
	
	/**
	 * Method that deletes a cell.
	 * 
	 * To delete a cell, given the architecture of
	 * the application, first its required to sever all the
	 * connections this cell has.
	 * More specifically, to its content,the spreadsheet
	 * it belongs to and any references that might point
	 * to it.
	 * 
	 * After doing that, then the object is deleted.
	 */
	
	public void delete() {
		if(super.getContent() != null) {
			super.getContent().delete();
		}
		setSpreadsheet(null);
		
		if(!getReferenceSet().isEmpty()) {
			for (Reference r : getReferenceSet()) {
				getReferenceSet().remove(r);
			}
		}
		
		deleteDomainObject();
	}
	
	/**
	 * The string representation of a cell.
	 * 
	 * @return {String} The string that represents the cell.
	 */
	
	@Override
	public String toString() {
		return "(" + getRow() + "," + getCollumn() + ")";
	}
}// End Cell class
