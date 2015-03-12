package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.ArrayList;
import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.CellAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;

public class Spreadsheet extends Spreadsheet_Base {
	
	public Spreadsheet() {
		super();
	}

	public Spreadsheet(String name, DateTime date, int nRows, int nCollumns) {
		super();

		int _idnext;
		int _rowIterator = 1;
		int _collumnIterator = 1;

		setBubbledocs(FenixFramework.getDomainRoot().getBubbledocs());
		BubbleDocs bd = getBubbledocs();
		_idnext = bd.getIdGlobal();
		bd.setIdGlobal(_idnext++);

		setId(_idnext);
		setName(name);
		setDate(date);
		setNRows(nRows);
		setNCols(nCollumns);
		
		if(!(getCellsSet().isEmpty())) {
			while(_rowIterator <= nRows) {
				while(_collumnIterator <= nCollumns) {
					Cell c = new Cell(_rowIterator, _collumnIterator, false);
					addCell(c);
					c.setSpreadsheet(this);
					
					_collumnIterator++;
				}
				_collumnIterator = 1; //Reset of column iterator.
				_rowIterator++;
			}
		} else {
			Cell c = new Cell(_rowIterator, _collumnIterator, false);
			addCells(c);
			c.setSpreadsheet(this);
		}
	}
	
	public void addCell(Cell c) throws CellAlreadyExistsException {
		
		Cell _c = getCellByCoords(c.getRow(), c.getCollumn());
		
		if (_c != null) {
			throw new CellAlreadyExistsException(c.getRow(), c.getCollumn());
		}
		
		super.addCells(c);
	}
	
	public Element exportToXML() {
		Element element = new Element("spreadsheet");
		element.setAttribute("name", getName());
		element.setAttribute("date-millisecond", Integer.toString(getDate().getMillisOfSecond()));
		element.setAttribute("date-second", Integer.toString(getDate().getSecondOfMinute()));
		element.setAttribute("date-minute", Integer.toString(getDate().getMinuteOfDay()));
		element.setAttribute("date-hour", Integer.toString(getDate().getHourOfDay()));
		element.setAttribute("date-day", Integer.toString(getDate().getDayOfMonth()));
		element.setAttribute("date-month", Integer.toString(getDate().getMonthOfYear()));
		element.setAttribute("date-year", Integer.toString(getDate().getYear()));
		element.setAttribute("rows", Integer.toString(getNRows()));
		element.setAttribute("collumns", Integer.toString(getNCols()));
		
		Element cellsElement = new Element("cells");
		element.addContent(cellsElement);
		
		for (Cell c : getCellsSet()) {
			//cellsElement.addContent(c.exportToXML()); TODO (Leo, Duarte e Marco)
		}
		
		Element permissionsElement = new Element("permissions");
		element.addContent(permissionsElement);
		
		for (Permission p : getPermissionsSet()) {
			permissionsElement.addContent(p.exportToXML());
		}
		
		return element;
	}
	
	public void importFromXML(Element spreadsheetElement) {
		setName(spreadsheetElement.getAttribute("name").getValue());
		DateTime date;
		
		try {
			date = new DateTime(spreadsheetElement.getAttribute("date-year").getIntValue(), spreadsheetElement.getAttribute("date-month").getIntValue(), spreadsheetElement.getAttribute("date-day").getIntValue(), spreadsheetElement.getAttribute("date-hour").getIntValue(), spreadsheetElement.getAttribute("date-minute").getIntValue(), spreadsheetElement.getAttribute("date-second").getIntValue(), spreadsheetElement.getAttribute("date-millisecond").getIntValue());
			setDate(date);
			setNRows(spreadsheetElement.getAttribute("rows").getIntValue());
			setNCols(spreadsheetElement.getAttribute("collumns").getIntValue());
		} catch (DataConversionException e) {
			throw new ImportDocumentException();
		}
		
		Element cells = spreadsheetElement.getChild("cells");
		
		for (Element cellElement : cells.getChildren("cell")) {
			Cell c = new Cell();
			//c.importFromXML(cellElement); //TODO (Leo, Duarte e Marco)
			addCell(c);
		}
		
		Element permissions = spreadsheetElement.getChild("permissions");
		
		for (Element permissionElement : permissions.getChildren("permission")) {
			Permission p = new Permission();
			p.importFromXML(permissionElement);
			addPermissions(p);
		}
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
			//c.delete(); Cell needs to implement this all the way down to remove spreadsheet. TODO (Leo, Marco, Duarte)
		//}
		deleteDomainObject();
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
		return "Nome:" + getName() + "Data Criacao:" + getDate().toString() + "N-Linhas:" + getNRows() + "N-Colunas:" + getNCols();
	}

}// End Spreadsheet Class