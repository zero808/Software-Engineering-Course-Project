package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base implements Argument {

	public Literal() {
		super();
	}

	public Literal(int value) {
		super();
		super.setVal(value);
	}

	@Override
	public String toString() {
		return Integer.toString(super.getVal());
	}

	@Override
	public int getValue() {
		return super.getVal();
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("literal");
		element.setAttribute("value", Integer.toString(super.getVal()));
		return element;
	}

	@Override
	public void importFromXML(Element element) {
		setVal(Integer.parseInt(element.getAttribute("value").getValue()));
	}

	@Override
	public Content retrieveContent() {
		return this;
	}
}// End Literal class
