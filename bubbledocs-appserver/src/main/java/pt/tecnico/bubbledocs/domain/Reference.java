package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Reference extends Reference_Base implements Argument {

	public Reference() {
		super();
	}

	public Reference(Cell c) {
		super();
		super.setReferencedCell(c);
		c.addReference(this);
	}
	
	@Override
	public String toString() {
		if(super.getReferencedCell().getContent() == null) 
			return "#VALUE";
		else
			return super.getReferencedCell().getContent().toString();
	}
	
	@Override
	public int getValue() {
		return super.getReferencedCell().getContent().getValue();
	}
	
	@Override
	public void delete() {
		
		if(super.getReferencedCell() != null) {
			super.getReferencedCell().removeReference(this);
		}
		
		super.setCell(null);
		deleteDomainObject();
	}

	@Override
	public Element exportToXML() {
 		Element element = new Element("reference");
 		
 		if(getReferencedCell() != null) {
 			element.setAttribute("row", Integer.toString(super.getReferencedCell().getRow()));
	 	 	element.setAttribute("collumn", Integer.toString(super.getReferencedCell().getCollumn()));
	 	 	
 			if(getCell() != null) {
 				element.setAttribute("desc", "Reference not saved in any cell of spreadsheet");
 			}
 		} else {
 			element.setAttribute("cont", "#VALUE");
 		}
 	
		return element;
	}

	@Override
	public void importFromXML(Element element) {
		
		int row = Integer.parseInt(element.getAttribute("row").getValue());
		int collumn = Integer.parseInt(element.getAttribute("collumn").getValue());
		
		Spreadsheet ss;
		
		if(getBinary1()!=null) {
			ss = getBinary1().getCell().getSpreadsheet();
		} else {
			ss = getCell().getSpreadsheet();
		} 
		
		setReferencedCell(ss.getCellByCoords(row, collumn));	
	}

	@Override
	public Content retrieveContent() {
		return this;
	}
}// End Reference class