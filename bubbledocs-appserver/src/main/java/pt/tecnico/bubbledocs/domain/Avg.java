package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Avg extends Avg_Base {

	public Avg() {
		super();
	}

	public Avg(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() {
		int total = 0;
		//int flag = 0;
		for (Cell cell : super.getRange().getCellsSet()) {
			if (cell.hasValidResult())
			total += cell.getContent().getValue();
		}
		return total / super.getRange().getCellsSet().size();
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