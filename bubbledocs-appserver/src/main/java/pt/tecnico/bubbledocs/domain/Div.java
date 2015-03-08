package pt.tecnico.bubbledocs.domain;

public class Div extends Div_Base {

	public Div() {
		super();
	}

	public Div(Content arg1, Content arg2) {
		super();
		super.setArg1(arg1);
		super.setArg2(arg2);
	}

	@Override
	public int getValue() {

		return super.getArg1().getValue() / super.getArg2().getValue();
		// TODO Throw exception
	}

}
