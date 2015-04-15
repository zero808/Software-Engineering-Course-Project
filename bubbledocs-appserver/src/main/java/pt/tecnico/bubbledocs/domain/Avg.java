package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
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
	public int getValue() throws BubbleDocsException {
		int total = 0;
		for (Cell cell : getCell().getSpreadsheet().getCellsInRange(getRange())) {
			if (cell.hasValidResult())
				total += cell.getContent().getValue();
			else
				throw new InvalidArgumentsException();
		}
		return total / getCell().getSpreadsheet().getCellsInRange(getRange()).size();
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element uf = new Element("unary_function");
		Element avg = new Element("avg");

		avg.addContent(getRange().exportToXML());

		uf.addContent(avg);
		f.addContent(uf);

		return f;
	}
}//End Avg class