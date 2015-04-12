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

	public abstract Element exportToXML();
	
	public void importFromXML(Element cellElement) {
		// create range(void)
		Range r = new Range();
		// set unary
		r.setUnary(this);
		this.setRange(r);
		// range.import
		Element element;
		if ((element = cellElement.getChild("range")) != null)
			r.importFromXML(element);
	}

	@Override
	public void delete() {
		Range r = getRange();
		setRange(null);
		r.setUnary(null);
		r.delete();
		setCell(null);
		deleteDomainObject();
	}
}// End Unary class