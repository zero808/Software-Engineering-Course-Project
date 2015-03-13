package pt.tecnico.bubbledocs.domain;

public abstract class Prd extends Prd_Base {

	public Prd() {
		super();
	}

	public Prd(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() {
		int total = 1;
		for (Cell cell : super.getRange().getCellsSet()) {
			total *= cell.getContent().getValue();
		}
		return total;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}