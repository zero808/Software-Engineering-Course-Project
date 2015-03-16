package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Unary extends Unary_Base {

	public Unary() {
		super();
	}

	public Unary(Range range) {
		super();
		super.setRange(range);
	}
	
	public abstract int getValue();

	public abstract void delete();

	public abstract Element exportToXML();

	public abstract void importFromXML(Element cellElement);
}