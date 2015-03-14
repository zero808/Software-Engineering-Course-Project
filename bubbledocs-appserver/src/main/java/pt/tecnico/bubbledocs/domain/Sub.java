package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Sub extends Sub_Base {

	public Sub() {
		super();
	}

	public Sub(Literal arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Sub(Reference arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Sub(Literal arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Sub(Reference arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	@Override
	public int getValue() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		if (super.getReferencesSet().size() + super.getLiteralsSet().size() != 2) {
			throw new InvalidArgumentsException();
		}
		for (Reference r : super.getReferencesSet())
			values.add(r.getValue());
		for (Literal l : super.getLiteralsSet())
			values.add(l.getValue());
		return values.get(0) - values.get(1);
		// TODO Throw exception
	}

	@Override
	public Element exportToXML() {
		Element f = new Element ("function");
		Element bf = new Element ("binary_function");
		Element div = new Element ("sub");
		for (Reference r : super.getReferencesSet())
			div.addContent(r.exportToXML());
		for (Literal l : super.getLiteralsSet())
			div.addContent(l.exportToXML());
		bf.addContent(div);
		f.addContent(bf);
		
		return f;
	}

	@Override
	public void importFromXML(Element cellElement) {
		// TODO Auto-generated method stub
		
	}

}
