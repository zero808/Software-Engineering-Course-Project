package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Reference extends Reference_Base {

	
	public Reference() {
		super();
	}

	public Reference(Cell c) {
		super();
		super.setReferencedCell(c);
		c.setReference(this);
	}

	public String toString() {
		if(super.getReferencedCell().getContent() == null) 
			return "\n#VALUE";
		else
			return super.getReferencedCell().getContent().toString();
	}
	
	@Override
	public int getValue() {
		return super.getReferencedCell().getContent().getValue();
	}
	
	public void delete() {
		//deletes the connection from the cell to this reference
		super.getReferencedCell().setReference(null);
		//and deletes the connection between this reference and the cell
		super.setCell(null);
		deleteDomainObject();
	}

	@Override
	public Element exportToXML() {
 		Element element = new Element ("ref");
		
		return element;
	}

	@Override
	public void importFromXML(Element cellElement) {
		// TODO Auto-generated method stub
		
	}
}