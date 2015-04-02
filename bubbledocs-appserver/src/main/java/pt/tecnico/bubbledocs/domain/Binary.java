package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Binary extends Binary_Base {

	public Binary() {
		super();
	}

	public Binary(Argument arg1, Argument arg2){
		Content a1 = (Content)arg1;
		Content a2 = (Content)arg2;
		a1.setBinary1(this);
		a2.setBinary1(this);
		
		setArg1((Content)arg1);
		setArg2((Content)arg2);
	}
	
	public void delete() {
		Content a1 = getArg1();
		Content a2 = getArg2();
		
		a1.setBinary1(null);
		a2.setBinary1(null);
		setArg1(null);
		setArg2(null);
		a1.delete();
		a2.delete();
		
		setCell(null);
		deleteDomainObject();
	}

	public abstract int getValue();

	public abstract Element exportToXML();

	public abstract void importFromXML(Element cellElement);
}//End Binary class