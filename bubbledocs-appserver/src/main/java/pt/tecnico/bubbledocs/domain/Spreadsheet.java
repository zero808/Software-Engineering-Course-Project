package pt.tecnico.bubbledocs.domain;

import java.util.Iterator;

import java.util.List;
import java.util.ArrayList;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.CellAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;

/**
 * Class that describes a spreadsheet.
 * 
 * Spreadsheets are what users manipulate in this
 * application.
 * 
 * They are made of cells.
 */

public class Spreadsheet extends Spreadsheet_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Spreadsheet}
	 */
	
	public Spreadsheet() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Spreadsheet}
	 * 
	 * @param {String} name The spreadsheet's name.
	 * @param {DateTime} date The time when the spreadsheet was created.
	 * @param {number} nRows The number of rows of this spreadsheet.
	 * @param {number} nCollumns The number of columns of this spreadsheet.
	 * @throws InvalidSpreadsheetNameException, InvalidBoundsException
	 */

	public Spreadsheet(String name, DateTime date, int nRows, int nCollumns) throws InvalidSpreadsheetNameException, InvalidBoundsException {
		super();

		int _idnext;
		int _rowIterator = 1;
		int _collumnIterator = 1;

		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		setBubbledocs(FenixFramework.getDomainRoot().getBubbledocs());

		_idnext = bd.getIdGlobal();
		/** @private */
		setId(_idnext);
		bd.setIdGlobal(++_idnext);
		
		if(name.matches("(.*)[^ A-Za-z0-9_+-](.*)") || name.equals(""))
			throw new InvalidSpreadsheetNameException();
		
		/** @private */
		setName(name);
		/** @private */
		setDate(date);
		
		if(nRows < 1 || nCollumns < 1) 
			throw new InvalidBoundsException(nRows, nCollumns);
		
		/** @private */
		setNRows(nRows);
		/** @private */
		setNCols(nCollumns);
		
			while(_rowIterator <= nRows) {
				while(_collumnIterator <= nCollumns) {
					Cell c = new Cell(_rowIterator, _collumnIterator, false);
					/** @private */
					addCells(c);
					
					_collumnIterator++;
				}
				//Reset of column iterator.
				_collumnIterator = 1;
				_rowIterator++;
			}
	}
	
	/**
	 * Method that adds a cell to the set of cells
	 * that is part of this spreadsheet.
	 * 
	 * @param {Cell} c The new cell thats being added.
	 * @throws CellAlreadyExistsException
	 */
	
	@Override
	public void addCells(Cell c) throws CellAlreadyExistsException {
		
		Cell _c = getCellByCoords(c.getRow(), c.getCollumn());
		
		if (_c != null) {
			throw new CellAlreadyExistsException(c.getRow(), c.getCollumn());
		}
		
		super.addCells(c);
		c.setSpreadsheet(this);
	}
	
	/**
	 * Export this spreadsheet to a XML document.
	 * 
	 * @return {XML Element} The element describing the spreadsheet.
	 */
	
	public Element exportToXML() {
		Element element = new Element("spreadsheet");
		element.setAttribute("name", getName());
		element.setAttribute("owner", getUser().getUsername());
		element.setAttribute("date-millisecond", Integer.toString(getDate().getMillisOfSecond()));
		element.setAttribute("date-second", Integer.toString(getDate().getSecondOfMinute()));
		element.setAttribute("date-minute", Integer.toString(getDate().getMinuteOfHour()));
		element.setAttribute("date-hour", Integer.toString(getDate().getHourOfDay()));
		element.setAttribute("date-day", Integer.toString(getDate().getDayOfMonth()));
		element.setAttribute("date-month", Integer.toString(getDate().getMonthOfYear()));
		element.setAttribute("date-year", Integer.toString(getDate().getYear()));
		element.setAttribute("rows", Integer.toString(getNRows()));
		element.setAttribute("collumns", Integer.toString(getNCols()));
		
		Element cellsElement = new Element("cells");
		element.addContent(cellsElement);
		
		for (Cell c : getCellsSet()) {
			if(c.getContent() != null) {
				cellsElement.addContent(c.exportToXML());
			}
		}
		
		return element;
	}
	
	/**
	 * Import this spreadsheet from a XML document.
	 * 
	 * @param {XML Element} spreadsheetElement The element that has the spreadsheet's data.
	 */
	
	public void importFromXML(Element spreadsheetElement) {
		
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		User owner = bd.getUserByUsername(spreadsheetElement.getAttribute("owner").getValue());
		
		setName(spreadsheetElement.getAttribute("name").getValue());
		setUser(owner);
		DateTime date;
		
		try {
			date = new DateTime(spreadsheetElement.getAttribute("date-year").getIntValue(), spreadsheetElement.getAttribute("date-month").getIntValue(), spreadsheetElement.getAttribute("date-day").getIntValue(), spreadsheetElement.getAttribute("date-hour").getIntValue(), spreadsheetElement.getAttribute("date-minute").getIntValue(), spreadsheetElement.getAttribute("date-second").getIntValue(), spreadsheetElement.getAttribute("date-millisecond").getIntValue());
			setDate(date);
			setNRows(spreadsheetElement.getAttribute("rows").getIntValue());
			setNCols(spreadsheetElement.getAttribute("collumns").getIntValue());
		} catch (DataConversionException e) {
			throw new ImportDocumentException(owner.getUsername());
		}
		
		Element cells = spreadsheetElement.getChild("cells");
		
		int _rowIterator = 1;
		int _collumnIterator = 1;
		
		while(_rowIterator <= getNRows()) {
			while(_collumnIterator <= getNCols()) {
				Cell c = new Cell(_rowIterator, _collumnIterator, false);
				addCells(c);
				
				_collumnIterator++;
			}
			_collumnIterator = 1; //Reset of column iterator.
			_rowIterator++;
		}
		
		Iterator<Cell> it = getCellsSet().iterator();
		for (Element cellElement : cells.getChildren("cell")) {
			Cell c = it.next();
			c.importFromXML(cellElement);	
		}
	}
	
	/**
	 * Method that deletes a spreadsheet.
	 * 
	 * To delete a spreadsheet, given the architecture of
	 * the application, first its required to sever all the
	 * connections this spreadsheet has.
	 * More specifically, all the permissions associated with this
	 * spreadsheet, the user that created it and all its cells.
	 * 
	 * The link to BubbleDocs is also severed.
	 * 
	 * If a user is deleted, then all of its spreadsheets are also deleted.
	 * 
	 * After doing that, then the object is deleted.
	 */
	
	public void delete() {
		setUser(null);
		
		setBubbledocs(null);
		
		for(Permission p : getPermissionsSet()) {
			if(p.getSpreadsheet().getName().equals(getName())) {
				p.delete();
			}
		}
		
		for(Cell c : getCellsSet()) {
			c.delete();
		}
		
		deleteDomainObject();
	}
	
	/**
	 * Method that returns a cell given the specific coordinates.
	 * 
	 * @param {number} row The cell's row.
	 * @param {number} collumn The cell's column.
	 * @return {Cell} The cell that had the pair (row, column), null otherwise.
	 */
	
	public Cell getCellByCoords(int row, int collumn) {
		for(Cell c : getCellsSet()) {
			if(c.getRow() == row && c.getCollumn() == collumn) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Method that returns the list of all the cells
	 * between 2 cells (range).
	 * 
	 * @param {Range} r The range of which the cells are required.
	 * @return {List of Cells} The cells within that range.
	 */
	
	public List<Cell> getCellsInRange(Range r) {
		List<Cell> _requestedCells = new ArrayList<Cell>();
		Cell _startingCell = null;
		Cell _endingCell = null;
		int _rowIterator = 1, _collumnIterator = 1, count = 1;
		
		for (Cell cell : r.getCellsSet()) { 
			//Should only have 2 cells in the set.
			if (count == 1) 
				_startingCell = cell;
			if (count == 2) 
				_endingCell = cell;
			count++;
		}
		
		//Get starting row value.
		_rowIterator = _startingCell.getRow();
		//Get starting column value.
		_collumnIterator = _startingCell.getCollumn();
		
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
				//Reset of column iterator.
				_collumnIterator = _startingCell.getCollumn();
				_rowIterator++;
			}
		}
		return _requestedCells;
	}
	
	/**
	 * Method that refers this spreadsheet in a permission.
	 * 
	 * @param {Permission} p The permission.
	 */
	
	@Override
	public void addPermissions(Permission p) {
		p.setSpreadsheet(this);
		super.addPermissions(p);
	}
	
	/**
	 * Method that returns the permission a particular user
	 * has in regards to this spreadsheet.
	 * 
	 * @param {User} u The user that has the permission.
	 * @return {Permission} The user's permission if he has one, null otherwise.
	 */
	
	public Permission getPermissionOfUser(User u) {
		for(Permission p : getPermissionsSet()) {
			if(p.getUser().getName().equals(u.getName())) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * The string representation of a Spreadsheet.
	 * 
	 * @return {String} The string that represents
	 * the spreadsheet.
	 */
	
	public String toString() {
		return "Nome: " + getName() + " " + "Data Criacao: " + getDate().toString() + " " + "N-Linhas: " + getNRows() + " " + "N-Colunas: " + getNCols() + "\n";
	}
}// End Spreadsheet class
