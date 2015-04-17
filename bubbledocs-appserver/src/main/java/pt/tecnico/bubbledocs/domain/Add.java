package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Add extends Add_Base {

	public Add() {
		super();
	}

	public Add(Argument arg1, Argument arg2) {
		Content a1 = arg1.retrieveContent();
		Content a2 = arg2.retrieveContent();
		a1.setBinary1(this);
		a2.setBinary1(this);
		
		setArg1((Content)arg1);
		setArg2((Content)arg2);
	}

	@Override
	public int getValue() throws InvalidArgumentsException {
		
		if (getArg1().toString().equals("#VALUE")) 
			throw new InvalidArgumentsException();
		
		if (getArg2().toString().equals("#VALUE")) 
			throw new InvalidArgumentsException();
		
		return super.getArg1().getValue()+super.getArg2().getValue();
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element bf = new Element("binary_function");
		Element add = new Element("add");
		
		add.addContent(super.getArg1().exportToXML());
		add.addContent(super.getArg2().exportToXML());
		
		bf.addContent(add);
		f.addContent(bf);

		return f;
	}
	
	@Override
	public void importFromXML(Element AddElement) {

		int count=1;
		Content a1 = null;
		Content a2 = null;

		for (Element argElement : AddElement.getChildren()) {
			if (argElement.getName().equals("literal")) {
				Literal l = new Literal();
				l.setBinary1(this);
				l.importFromXML(argElement);
				if(count==1) a1=l.retrieveContent();
				if(count==2) a2=l.retrieveContent();
			}
			if (argElement.getName().equals("reference")) {
				Reference r = new Reference();
				r.setBinary1(this);
				r.importFromXML(argElement);
				if(count==1) a1 = r.retrieveContent();
				if(count==2) a2 = r.retrieveContent();
			}
			count++;
		}
		setArg1(a1);
		setArg2(a2);
	}
}// End Add class