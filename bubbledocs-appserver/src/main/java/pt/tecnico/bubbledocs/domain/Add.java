package pt.tecnico.bubbledocs.domain;

public class Add extends Add_Base {

	public Add() {
		super();
	}

	public Add(Content arg1, Content arg2) {
		super();
		super.setArg1(arg1);
		super.setArg2(arg2);
	}

	@Override
	public int getValue() {
		return super.getArg1().getValue() + super.getArg2().getValue();
		// TODO Throw exception
	}

}