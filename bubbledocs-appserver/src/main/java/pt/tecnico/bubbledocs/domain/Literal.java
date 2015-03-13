package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
    public Literal() {
        super();
    }
    
    public Literal(int value) {
    	super();
    	super.setVal(value);
    }
    
    @Override
    public String toString() {
    	return "Literal value: " + super.getVal();
    }

	@Override
	public int getValue() {
		return super.getVal();
	}

	@Override
	public void delete() {
		deleteDomainObject();
	}

	@Override
	public Element exportToXML() {
		Element element = new Element ("literal");
		
		return element;	}

	@Override
	public void importFromXML(Element cellElement) {
		// TODO Auto-generated method stub
		
	}
    
}