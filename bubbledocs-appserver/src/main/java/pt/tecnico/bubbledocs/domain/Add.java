package pt.tecnico.bubbledocs.domain;

public class Add extends Add_Base {

	public Add() {
		super();
	}

	public Add(Content arg1, Content arg2) {
		super();
		super.setContent1(arg1);
		super.setContent2(arg2);
	}

	@Override
	public int getValue() {

		return super.getContent1().getValue() + super.getContent2().getValue();
		// TODO Throw exception
	}

}