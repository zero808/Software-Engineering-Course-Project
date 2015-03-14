package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

//import org.jdom2.Element;

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

	public void importFromXML(Element AddElement) {
		for (Element argElement : AddElement.getChildren("literal")) {
			Literal l = new Literal();
			l.importFromXML(argElement);
			addLiterals(l);
			l.setBinary(this);
		}

		for (Element argElement : AddElement.getChildren("reference")) {
			Reference r = new Reference();
			r.importFromXML(argElement);
			addReferences(r);
			r.setBinary(this);
		}

	}

	// public abstract Element exportToXML();
}