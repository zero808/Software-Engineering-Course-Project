package pt.tecnico.bubbledocs.domain;

public class Reference extends Reference_Base {

	private Cell cell;
	
	public Reference() {
		super();
	}

	public Reference(Cell c) {
		super();
		this.cell = c;
	}

	public String toString() {
		return this.cell.toString();
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	@Override
	public int getValue() {
		return cell.getContent().getValue();
	}
}