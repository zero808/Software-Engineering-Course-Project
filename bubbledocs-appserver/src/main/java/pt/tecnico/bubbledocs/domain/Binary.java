package pt.tecnico.bubbledocs.domain;

public abstract class Binary extends Binary_Base {

	private Content arg1;
	private Content arg2;

	public Binary() {
		super();
	}

	public Binary(Content arg1, Content arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public Content getArg1() {
		return arg1;
	}

	public void setArg1(Content arg1) {
		this.arg1 = arg1;
	}

	public Content getArg2() {
		return arg2;
	}

	public void setArg2(Content arg2) {
		this.arg2 = arg2;
	}

	@Override
	public void delete() {
		this.arg1 = null;
		this.arg2 = null;
		setCell(null);
		deleteDomainObject();
	}
}