package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Mul extends Mul_Base {

	public Mul() {
		super();
	}

	public Mul(Literal arg1, Literal arg2) {
		super();
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Mul(Reference arg1, Literal arg2) {
		super();
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Mul(Literal arg1, Reference arg2) {
		super();
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Mul(Reference arg1, Reference arg2) {
		super();
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
		// TODO Throw exception
	}

}
