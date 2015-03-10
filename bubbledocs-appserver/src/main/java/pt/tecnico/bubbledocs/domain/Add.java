package pt.tecnico.bubbledocs.domain;

public class Add extends Add_Base {

	public Add() {
		super();
	}

	public Add(Literal arg1, Literal arg2) {
		super();
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Add(Reference arg1, Literal arg2) {
		super();
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Add(Literal arg1, Reference arg2) {
		super();
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Add(Reference arg1, Reference arg2) {
		super();
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	@Override
	public int getValue() {
		int value = 0;
		if (super.getReferencesSet().size() + super.getLiteralsSet().size() != 2) {
			// TODO throw exception, number of args invalid
		}
		for (Reference r : super.getReferencesSet())
			value += r.getValue();
		for (Literal l : super.getLiteralsSet())
			value += l.getValue();
		return value;
		// TODO Throw exception
	}

}