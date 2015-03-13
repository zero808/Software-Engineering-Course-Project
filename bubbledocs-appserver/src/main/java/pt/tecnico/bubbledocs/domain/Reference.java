package pt.tecnico.bubbledocs.domain;

public class Reference extends Reference_Base {

	
	public Reference() {
		super();
	}

	public Reference(Cell c) {
		super();
		super.setCell(c);
		c.setReference(this);
	}

	public String toString() {
		return super.getCell().toString();
	}
	
	@Override
	public int getValue() {
		return super.getCell().getContent().getValue();
	}
	
	public void delete() {
		//deletes the connection from the cell to this reference
		super.getCell().setReference(null);
		//and deletes the connection between this reference and the cell
		super.setCell(null);
		deleteDomainObject();
	}
}