package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Content extends Content_Base {
    
    public Content() {
        super();
    }
    
    public Content(Cell c){
    	super();
    	super.setCell(c);
    }
    
    public void delete() {
    	setCell(null);
    	deleteDomainObject();
    }

    public abstract int getValue();

	public abstract Element exportToXML();
	
	public abstract void importFromXML(Element cellElement);
}// End Content class
