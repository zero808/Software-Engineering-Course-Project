package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Function extends Function_Base {

	public Function() {
		super();
	}
	
    public abstract int getValue();
	
	public abstract Element exportToXML();
	
	public abstract void importFromXML(Element cellElement);
	
	public abstract void delete();
}