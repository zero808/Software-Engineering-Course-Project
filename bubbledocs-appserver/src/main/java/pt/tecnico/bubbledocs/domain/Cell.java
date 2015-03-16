package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Cell extends Cell_Base {

	public Cell() {
		super();
	}

	public Cell(int _row, int _col, boolean _wProtected) throws InvalidArgumentsException {
		super();
		if (_row > 0 && _col > 0) {
			super.setRow(_row);
			super.setCollumn(_col);
		} else
			throw new InvalidArgumentsException();
		
		super.setWProtected(_wProtected);
	}

	public Element exportToXML() {
		Element element = new Element("cell");

		element.setAttribute("row", Integer.toString(super.getRow()));
		element.setAttribute("collumn", Integer.toString(super.getCollumn()));
		element.setAttribute("wprotected", Boolean.toString(super.getWProtected()));

		Element cont = new Element("content");
		element.addContent(cont);

		if (super.getContent() != null)
			cont.addContent(super.getContent().exportToXML());

		return element;
	}

	public void importFromXML(Element cellElement) {
		setRow(Integer.parseInt(cellElement.getAttribute("row").getValue()));
		setCollumn(Integer.parseInt(cellElement.getAttribute("collumn").getValue()));
		setWProtected(Boolean.parseBoolean(cellElement.getAttribute("wprotected").getValue()));

		Element content = cellElement.getChild("content");

		if (content != null) {

			Element subcontent;
			if ((subcontent = content.getChild("reference")) != null) {
				Reference r = new Reference();
				r.importFromXML(subcontent);
				r.setCell(this);
			} else {
				if ((subcontent = content.getChild("literal")) != null) {
					Literal l = new Literal();
					l.importFromXML(subcontent);
					l.setCell(this);
				} else {
					if ((subcontent = content.getChild("function")) != null) {
						Element functionType;
						if ((functionType = subcontent.getChild("binary_function")) != null) {
							Element functionName;
							if ((functionName = functionType.getChild("add")) != null) {
								Add add = new Add();
								add.importFromXML(functionName);
								add.setCell(this);
							} else {
								if ((functionName = functionType.getChild("sub")) != null) {
									Sub sub = new Sub();
									sub.importFromXML(functionName);
									sub.setCell(this);
								} else {
									if ((functionName = functionType.getChild("div")) != null) {
										Div div = new Div();
										div.importFromXML(functionName);
										div.setCell(this);
									} else {
										if ((functionName = functionType.getChild("mul")) != null) {
											Mul mul = new Mul();
											mul.importFromXML(functionName);
											mul.setCell(this);
										}
									}
								}
							}

						} else {
							if ((functionType = subcontent.getChild("unary_function")) != null) {
								// TODO Second Delivery
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		String _cont;
		if (this.getContent() == null) {
			_cont = null;
			return "row: " + this.getRow() + "\ncol: " + this.getCollumn() + "\n#VALUE\n";
		} else {
			_cont = this.getContent().toString();
			return "row: " + super.getRow() + "\ncol: " + super.getCollumn() + "\n" + _cont; // OCD beware.
		}
	}

	public void delete() {
		if(super.getContent() != null) {
			super.getContent().delete();
		}
		setSpreadsheet(null);
		
		if(getReference() != null) {
			setReference(null);
		}
		
		deleteDomainObject();
	}

}
