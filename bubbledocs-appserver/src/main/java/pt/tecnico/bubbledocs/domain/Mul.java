package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Mul extends Mul_Base {

	public Mul() {
		super();
	}

	public Mul(Literal arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Mul(Reference arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Mul(Literal arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Mul(Reference arg1, Reference arg2) {
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
		return values.get(0) * values.get(1);
		// TODO Needs to check for #VALUES
	}

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element bf = new Element("binary_function");
		Element mul = new Element("mul");

		for (Reference r : super.getReferencesSet())
			mul.addContent(r.exportToXML());
		for (Literal l : super.getLiteralsSet())
			mul.addContent(l.exportToXML());
		bf.addContent(mul);
		f.addContent(bf);

		return f;
	}

	@Override
	public void importFromXML(Element MulElement) {
		for (Element argElement : MulElement.getChildren("literal")) {
			Literal l = new Literal();
			l.setBinary(this);
			l.importFromXML(argElement);
			addLiterals(l);
		}

		for (Element argElement : MulElement.getChildren("reference")) {
			Reference r = new Reference();
			r.setBinary(this);
			r.importFromXML(argElement);
			addReferences(r);
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
		return "Funcao Mul";
		// TODO Make it print the arguments and the value also.
	}
}