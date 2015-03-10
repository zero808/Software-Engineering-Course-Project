package pt.tecnico.bubbledocs.domain;

public abstract class Binary extends Binary_Base {

	public Binary() {
		super();
	}

	public Binary(Literal arg1, Literal arg2) {
		super();
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Binary(Reference arg1, Literal arg2) {
		super();
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Binary(Literal arg1, Reference arg2) {
		super();
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Binary(Reference arg1, Reference arg2) {
		super();
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	public void delete() {
		// this.arg1 = null;
		// this.arg2 = null;
		// setCell(null);
		// deleteDomainObject();
		// TODO
	}
}