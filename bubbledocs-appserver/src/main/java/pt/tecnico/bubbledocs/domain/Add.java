package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Add extends Add_Base {

	public Add() {
		super();
	}

	public Add(Literal arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Add(Reference arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Add(Literal arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Add(Reference arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	@Override
	public int getValue() {
		int value = 0;
		if (super.getReferencesSet().size() + super.getLiteralsSet().size() != 2) {
			throw new InvalidArgumentsException();
		}
		for (Reference r : super.getReferencesSet())
			value += r.getValue();
		for (Literal l : super.getLiteralsSet())
			value += l.getValue();
		return value;
		// TODO Needs to check for #VALUE.
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element bf = new Element("binary_function");
		Element add = new Element("add");
		
		for (Reference r : super.getReferencesSet())
			add.addContent(r.exportToXML());
		for (Literal l : super.getLiteralsSet())
			add.addContent(l.exportToXML());
		
		bf.addContent(add);
		f.addContent(bf);

		return f;
	}
	
	@Override
	public void importFromXML(Element AddElement) {
		for (Element argElement : AddElement.getChildren("literal")) {
			Literal l = new Literal();
			l.importFromXML(argElement);
			addLiterals(l);
			l.setBinary(this);
		}

		for (Element argElement : AddElement.getChildren("reference")) {
			Reference r = new Reference();
			r.importFromXML(argElement);
			addReferences(r);
			r.setBinary(this);
		}
	}
	
	@Override
	public void delete() {
		for (Literal l : super.getLiteralsSet()) {
			l.setBinary(null);
			super.removeLiterals(l);
			l.delete();
		}
		for (Reference r : super.getReferencesSet()) {
			r.setBinary(null);
			super.removeReferences(r);
			r.delete();

		}
		setCell(null);
		deleteDomainObject();
	}
	
	@Override
	public String toString() {
		return "Funcao Add";
		//TODO Make it print the arguments and the value also.
	}
}