package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Prd extends Prd_Base {

	public Prd() {
		super();
	}

	public Prd(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() {
		int total = 1;
		for (Cell cell : super.getRange().getCellsSet()) {
			total *= cell.getContent().getValue();
		}
		return total;
		//TODO Needs to check for #VALUES.
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		//TODO Second Delivery.
		return f;
	}
	
	public void importFromXML(Element cellElement) {
		//TODO Second Delivery.
	}

	@Override
	public void delete() {
		// TODO Second Delivery.	
	}
}