package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

public class Sub extends Sub_Base {

	public Sub() {
		super();
	}

	public Sub(Literal arg1, Literal arg2) {
		super();
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Sub(Reference arg1, Literal arg2) {
		super();
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Sub(Literal arg1, Reference arg2) {
		super();
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Sub(Reference arg1, Reference arg2) {
		super();
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	@Override
	public int getValue() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		if (super.getReferencesSet().size() + super.getLiteralsSet().size() != 2) {
			// TODO throw exception, number of args invalid
		}
		for (Reference r : super.getReferencesSet())
			values.add(r.getValue());
		for (Literal l : super.getLiteralsSet())
			values.add(l.getValue());
		return values.get(0) - values.get(1);
		// TODO Throw exception
	}

}
