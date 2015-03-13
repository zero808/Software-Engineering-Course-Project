package pt.tecnico.bubbledocs.domain;

public class Avg extends Avg_Base {

	public Avg() {
		super();
	}

	public Avg(Range range) {
		super();
		super.setRange(range);
	}

	@Override
	public int getValue() {
		int total = 0;
		for (Cell cell : super.getRange().getCellsSet()) {
			total += cell.getContent().getValue();
		}
		return total / super.getRange().getCellsSet().size();
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}