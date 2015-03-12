package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.exception.CellAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;

public class Spreadsheet extends Spreadsheet_Base {

	public Spreadsheet(String name, DateTime date, int nRows, int nCollumns) {
		super();

		int _idnext;
		int _rowIterator = 1;
		int _collumnIterator = 1;

		BubbleDocs bd = getBubbledocs();
		_idnext = bd.getIdGlobal();
		_idnext++;

		setId(_idnext);
		setName(name);
		setDate(date);
		setNRows(nRows);
		setNCols(nCollumns);
		
		while(_rowIterator <= nRows) {
			while(_collumnIterator <= nCollumns) {
				addCell(new Cell(_rowIterator, _collumnIterator, false));
				_collumnIterator++;
			}
			_collumnIterator = 1; //Reset of column iterator.
			_rowIterator++;
		}
	}
	
	public void addCell(Cell c) throws CellAlreadyExistsException {
		Cell _c = getCellByCoords(c.getRow(), c.getCollumn());
		
		if (_c != null) {
			throw new CellAlreadyExistsException(c.getRow(), c.getCollumn());
		}
		
		super.addCells(c);
	}

	public void delete() { //Used to remove connection to user that is being removed.

		setUser(null);
	}
	
	public void deleteSpreadsheetContent() { //Used to remove a spreadsheet.
		
		setBubbledocs(null);
		setUser(null);
		for(Permission p : getPermissionsSet()) {
			if(p.getSpreadsheet().getName().equals(getName())) {
				p.delete();
			}
		}
		//for(Cell c : getCellsSet()) {
			//c.delete(); Cell needs to implement this all the way down to remove spreadsheet.
		//}
		deleteDomainObject();
		//TODO (Leo, Marco, Duarte)
	}
	
	public Cell getCellByCoords(int row, int collumn) throws OutofBondsException {
		Cell _requestedCell = null;
		
		for(Cell c : getCellsSet()) {
			if(c.getRow() == row && c.getCollumn() == collumn) {
				_requestedCell = c;
			}
		}
		
		if(_requestedCell == null) { //Should never happen, just to be safe.
			throw new OutofBondsException(getName());
		}
		return _requestedCell;
	}
	
	public List<Cell> getCellsInRange(Range r) {
		List<Cell> _requestedCells = new ArrayList<Cell>();
		Cell _startingCell = null;
		Cell _endingCell = null;
		int _rowIterator = 1;
		int _collumnIterator = 1;
		
		for(Cell c : r.getCellsSet()) { //Should only have 2 cells in the set.
			if(_startingCell == null) {
				_startingCell = c; //Get the first cell.
			}
			if(_endingCell == null) {
				_endingCell = c; //Get the last cell.
			}
		}
		
		_rowIterator = _startingCell.getRow(); //Get starting row value.
		_collumnIterator = _startingCell.getCollumn(); //Get starting column value.
		
		if(_startingCell.getRow() == _endingCell.getRow()) {
			while(_collumnIterator <= _endingCell.getCollumn()) {
				_requestedCells.add(getCellByCoords(_startingCell.getRow(), _collumnIterator));
				_collumnIterator++;
			}
		} else if(_startingCell.getCollumn() == _endingCell.getCollumn()) {
			while(_rowIterator <= _endingCell.getRow()) {
				_requestedCells.add(getCellByCoords(_rowIterator, _startingCell.getCollumn()));
				_rowIterator++;
			}
		} else {
			while(_rowIterator <= _endingCell.getRow()) {
				while(_collumnIterator <= _endingCell.getCollumn()) {
					_requestedCells.add(getCellByCoords(_rowIterator, _collumnIterator));
					_collumnIterator++;
				}
				_collumnIterator = _startingCell.getCollumn(); //Reset of column iterator.
				_rowIterator++;
			}
		}
		return _requestedCells;
	}
	
	public Permission getPermissionOfUser(User u) {
		for(Permission p : getPermissionsSet()) {
			if(p.getUser().getName().equals(u.getName())) {
				return p;
			}
		}
		return null;
	}
	
	public void printCells() {

		for (Cell cell : getCellsSet()) {
			cell.toString();
		}
	}

	public String toString() {
		return "Nome:" + getName() + "Data Criacao:" + getDate() + "N-Linhas:" + getNRows() + "N-Colunas:" + getNCols();
	}

}// End Spreadsheet Class