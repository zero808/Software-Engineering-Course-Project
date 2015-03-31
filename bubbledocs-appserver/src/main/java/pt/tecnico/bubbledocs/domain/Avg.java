package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Avg extends Avg_Base {

	public Avg() {
		super();
	}

	public Avg(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() throws InvalidArgumentsException {
		int total = 0;
		for (Cell cell : super.getRange().getCellsSet()) {
			if (cell.hasValidResult())
				total += cell.getContent().getValue();
		}
		return total / super.getRange().getCellsSet().size();
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element uf = new Element("unary_function");
		Element avg = new Element("avg");

		getRange().exportToXML();

		uf.addContent(avg);
		f.addContent(uf);

		return f;
	}

	public void importFromXML(Element cellElement) {// mover pa cima
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
		// TODO Second Delivery.
	}
}