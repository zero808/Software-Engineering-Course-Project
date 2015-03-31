package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Prd extends Prd_Base {

	public Prd() {
		super();
	}

	public Prd(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() throws InvalidArgumentsException {
		int total = 1;
		for (Cell cell : super.getRange().getCellsSet()) {
			if (cell.hasValidResult())
				total *= cell.getContent().getValue();
		}
		return total;
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element uf = new Element("unary_function");
		Element prd = new Element("prd");

		getRange().exportToXML();

		uf.addContent(prd);
		f.addContent(uf);

		return f;
	}

}