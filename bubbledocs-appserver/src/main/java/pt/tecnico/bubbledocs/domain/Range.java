package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that describes the concept of a range
 * within a spreadsheet.
 * 
 * It has two arguments, the cell where the range
 * starts, and the cell where it ends.
 */

public class Range extends Range_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Range}
	 */
	public Range() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Range}
	 * 
	 * @param {Cell} startCell Where the range starts.
	 * @param {Cell} endCell Where the range ends.
	 */
	
	public Range(Cell startCell, Cell endCell) {
		super();
		/** @private */
		addCells(startCell);
		/** @private */
		addCells(endCell);
	}
	
	/**
	 * Export this range to a XML document.
	 * 
	 * @return {XML Element} The element describing the range.
	 */

	public Element exportToXML() {
		Element element = new Element("range");

		int count = 1;
		String text = "";
		for (Cell cell : getCellsSet()) {
			if (count == 1)
				text = "startCell";
			else
				text = "endCell";

			Element cellType = new Element(text);
			cellType.setAttribute("row", Integer.toString(cell.getRow()));
			cellType.setAttribute("collumn", Integer.toString(cell.getCollumn()));
			element.addContent(cellType);
			count++;
		}
		return element;
	}
	
	/**
	 * Import this range from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the range's data.
	 */

	public void importFromXML(Element cellElement) {
		for (Element element : cellElement.getChildren()) {
			if (element.getName().equals("startCell") || element.getName().equals("endCell")) {
				int row = Integer.parseInt(element.getAttribute("row").getValue());
				int col = Integer.parseInt(element.getAttribute("collumn").getValue());
				
				Spreadsheet s = getUnary().getCell().getSpreadsheet();
				Cell cell = s.getCellByCoords(row, col);
				
				super.addCells(cell);
			}
		}
	}
	
	/**
	 * Method that deletes a particular range.
	 * 
	 * To delete a particular range, given the architecture of
	 * the application, first its required to sever all the
	 * connections the range has. 
	 * More specifically, to the cell where it starts and to
	 * the cell where it ends.
	 * After doing that, then the object is deleted.
	 */

	public void delete() {
		for (Cell cell : super.getCellsSet()) {
			super.removeCells(cell);
		}
		deleteDomainObject();
	}
	
	/**
	 * The string representation of a range.
	 * 
	 * @return {String} The string that represents the range.
	 */
	
	@Override
	public String toString() {
		String returnRange = "";
		int count = 1;

		for (Cell cell : getCellsSet()) {
			if (count == 1) 
				returnRange = returnRange + cell.toString();
			if (count == 2) 
				returnRange = returnRange + cell.toString();
			count++;
		}

		return returnRange;
	}
}// End Range class
	