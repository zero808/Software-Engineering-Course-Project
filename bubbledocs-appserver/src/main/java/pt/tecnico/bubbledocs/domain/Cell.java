package pt.tecnico.bubbledocs.domain;

public class Cell extends Cell_Base {
    
	public Cell() {
		super();
	}

	public Cell(int _row, int _col, boolean _wProtected) {
		super();
		super.setRow(_row);
		super.setCollumn(_col);
		super.setWProtected(_wProtected);
		
		//TODO throw an exception when cell is out of bonds
	}

	@Override
	public String toString() {
		String _cont;
		if (this.getContent() == null)
			_cont = null;
		else
			_cont = this.getContent().toString();
		return "row: " + super.getRow() + "\ncol: " + super.getCollumn()
				+ "\ncont: " + _cont;

	}
    
}
