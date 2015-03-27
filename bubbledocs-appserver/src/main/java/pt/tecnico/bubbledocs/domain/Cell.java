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
				r.setCell(this);
				r.importFromXML(subcontent);
			} else {
				if ((subcontent = content.getChild("literal")) != null) {
					Literal l = new Literal();
					l.setCell(this);
					l.importFromXML(subcontent);
				} else {
					if ((subcontent = content.getChild("function")) != null) {
						Element functionType;
						if ((functionType = subcontent.getChild("binary_function")) != null) {
							Element functionName;
							if ((functionName = functionType.getChild("add")) != null) {
								Add add = new Add();
								add.setCell(this);
								add.importFromXML(functionName);
							} else {
								if ((functionName = functionType.getChild("sub")) != null) {
									Sub sub = new Sub();
									sub.setCell(this);
									sub.importFromXML(functionName);
								} else {
									if ((functionName = functionType.getChild("div")) != null) {
										Div div = new Div();
										div.setCell(this);
										div.importFromXML(functionName);
									} else {
										if ((functionName = functionType.getChild("mul")) != null) {
											Mul mul = new Mul();
											mul.setCell(this);
											mul.importFromXML(functionName);
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

	public boolean hasValidResult(){
		if (getContent() == null) return false;
		if (getContent().toString().equals("#VALUE")) return false;
		return true;
	}
	
	public void delete() {
		if(super.getContent() != null) {
			super.getContent().delete();
		}
		setSpreadsheet(null);
		
		if(!getReferenceSet().isEmpty()) {
			for (Reference r : getReferenceSet()) {
				getReferenceSet().remove(r);
			}
		}
		
		deleteDomainObject();
	}
}