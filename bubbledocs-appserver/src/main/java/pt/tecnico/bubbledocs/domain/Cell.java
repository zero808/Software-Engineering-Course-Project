package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

public class Cell extends Cell_Base {
    
	public Cell() {
		super();
	}

	public Cell(int _row, int _col, boolean _wProtected) throws InvalidArgumentsException {
		super();
		if(_row > 0 && _col > 0) {
			super.setRow(_row);
			super.setCollumn(_col);
		}
		else
			throw new InvalidArgumentsException();
		super.setWProtected(_wProtected);
	}

	@Override
	public String toString() {
		String _cont;
		if (this.getContent() == null) {
			_cont = null;
			return "row: " + this.getRow() + "\ncol: " + this.getCollumn() + "\n#VALUE\n";
		}
		else {
			_cont = this.getContent().toString();
			return "row: " + super.getRow() + "\ncol: " + super.getCollumn() + "\n" + _cont; //OCD beware.
		}
	}
    
}
