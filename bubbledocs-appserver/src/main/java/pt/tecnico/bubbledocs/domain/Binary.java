package pt.tecnico.bubbledocs.domain;

public abstract class Binary extends Binary_Base {

	public Binary() {
		super();
	}

	public Binary(Literal arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addLiterals(arg1);
		super.addLiterals(arg2);
	}

	public Binary(Reference arg1, Literal arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addLiterals(arg2);
	}

	public Binary(Literal arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg2);
		super.addLiterals(arg1);
	}

	public Binary(Reference arg1, Reference arg2) {
		super();
		arg1.setBinary(this);
		arg2.setBinary(this);
		super.addReferences(arg1);
		super.addReferences(arg2);
	}

	public void delete() {
		for(Literal l : super.getLiteralsSet()) {
			l.setBinary(null);
			super.removeLiterals(l);
		}
		for(Reference r : super.getReferencesSet()) {
			r.setBinary(null);
			super.removeReferences(r);
		}
		deleteDomainObject();
	}
}