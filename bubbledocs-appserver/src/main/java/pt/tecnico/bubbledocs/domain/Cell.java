package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Cell extends Cell_Base {
    
	public Cell() {
		super();
	}

	public Cell(int _row, int _col, boolean _wProtected) throws InvalidArgumentsException {
		super();
		if(_row > 0 && _col > 0) {
			super.setRow(_row);
			super.setCollumn(_col);
		}
		else
			throw new InvalidArgumentsException();
		super.setWProtected(_wProtected);
	}

	public Element exportToXML() {
		Element element = new Element("cell");

		element.setAttribute("row", Integer.toString(super.getRow()));
		element.setAttribute("collumn", Integer.toString(super.getCollumn()));
		element.setAttribute("wprotected", Boolean.toString(super.getWProtected()));
		
		Element cont = new Element("contents");
		element.addContent(cont);
		
		if (super.getContent() != null)
		cont.addContent(super.getContent().exportToXML());
		
		return element;
	}
	
	@Override
	public String toString() {
		String _cont;
		if (this.getContent() == null) {
			_cont = null;
			return "row: " + this.getRow() + "\ncol: " + this.getCollumn() + "\n#VALUE\n";
		}
		else {
			_cont = this.getContent().toString();
			return "row: " + super.getRow() + "\ncol: " + super.getCollumn() + "\n" + _cont; //OCD beware.
		}
	}
	
	public void delete() {
		super.getContent().delete();
		deleteDomainObject();
	}
    
}
