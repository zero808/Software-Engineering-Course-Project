package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

//import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Sub extends Sub_Base {

	public Sub() {
		super();
	}

	public Sub(Argument arg1, Argument arg2){
		Content a1 = arg1.retrieveContent();
		Content a2 = arg2.retrieveContent();
		a1.setBinary1(this);
		a2.setBinary1(this);
		
		setArg1((Content)arg1);
		setArg2((Content)arg2);
	}

	@Override
	public int getValue() throws BubbleDocsException{
		if (getArg1().toString().equals("#VALUE")) throw new InvalidArgumentsException();
		if (getArg2().toString().equals("#VALUE")) throw new InvalidArgumentsException();
		return super.getArg1().getValue()-super.getArg2().getValue();
		// TODO Needs to check for #VALUES and the order of the arguments.
	}

	@Override
	public Element exportToXML() {
		Element f = new Element ("function");
		Element bf = new Element ("binary_function");
		Element sub = new Element ("sub");
		
		sub.addContent(super.getArg1().exportToXML());
		sub.addContent(super.getArg2().exportToXML());

		bf.addContent(sub);
		f.addContent(bf);
		
		return f;
	}

	@Override
	public void importFromXML(Element SubElement) {
		
		int count=1;
		
		for (Element argElement : SubElement.getChildren()){
			if (argElement.getName().equals("literal")){
				Literal l = new Literal();
				l.setBinary1(this);
				l.importFromXML(argElement);
				if(count==1) this.setArg1(l);
				if(count==2) this.setArg2(l);
			}
			if (argElement.getName().equals("reference")){
				Reference r = new Reference();
				r.setBinary1(this);
				r.importFromXML(argElement);
				if(count==1) this.setArg1(r);
				if(count==2) this.setArg2(r);
			}
			count++;
		}
		
	}
	
	@Override
	public String toString() {
		return "Funcao Sub";
		//TODO Make it print the arguments and the value also.
	}
}