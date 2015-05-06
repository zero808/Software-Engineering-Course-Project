package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Range extends Range_Base {

	public Range() {
		super();
	}
	
	public Range(Cell startCell, Cell endCell) {
		super();
		addCells(startCell);
		addCells(endCell);
	}

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

	public void delete() {
		for (Cell cell : super.getCellsSet()) {
			super.removeCells(cell);
		}
		deleteDomainObject();
	}
	
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
	