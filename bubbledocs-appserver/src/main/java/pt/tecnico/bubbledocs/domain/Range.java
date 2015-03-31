package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import org.jdom2.Element;

public class Range extends Range_Base {

	public Range() {
		super();
	}

	private void generateRange(int row1, int col1, int row2, int col2) {
		Spreadsheet s = getUnary().getCell().getSpreadsheet();
		Cell initCell = s.getCellByCoords(row1, col1);
		Cell endCell = s.getCellByCoords(row2, col2);
		super.addCells(initCell);
		super.addCells(endCell);

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
			cellType.setAttribute("collumn",
					Integer.toString(cell.getCollumn()));
			element.addContent(cellType);
		}
		return element;
	}

	public void importFromXML(Element cellElement) {
		// parse values <range start:"3;4" end: "4;5" />
		// unary.generateRange(values)
		for (Element element : cellElement.getChildren()) {
			if (element.getName().equals("startCell")
					|| element.getName().equals("endCell")) {
				int row = Integer.parseInt(element.getAttribute("row")
						.getValue());
				int col = Integer.parseInt(element.getAttribute("collumn")
						.getValue());
				Spreadsheet s = getUnary().getCell().getSpreadsheet();
				Cell cell = s.getCellByCoords(row, col);
				super.addCells(cell);
			}
		}
	}
}
