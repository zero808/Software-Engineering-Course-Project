package pt.tecnico.bubbledocs.service;

//import pt.tecnico.bubbledocs.domain.Cell;
//import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class AssignReferenceCell extends BubbleDocsService {
	
	private String result;
	private int cell_row;
	private int cell_collumn;
	//private int reference_row;
	//private int reference_collumn;
	private int docId;
	//private String tokenUser;

	public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
		String cell_parts[] = cellId.split(";");
		String cell_row = cell_parts[0];
		String cell_collumn = cell_parts[1];
		int cell_row_int = Integer.parseInt(cell_row);
		int cell_collumn_int = Integer.parseInt(cell_collumn);
		
		//String reference_parts[] = reference.split(";");
		//String reference_row = reference_parts[0];
		//String reference_collumn = reference_parts[1];
		//int reference_row_int = Integer.parseInt(reference_row);
		//int reference_collumn_int = Integer.parseInt(reference_collumn);
		
		this.docId = docId;
		//this.tokenUser = tokenUser;
		this.cell_row = cell_row_int;
		this.cell_collumn = cell_collumn_int;
		//this.reference_row = reference_row_int;
		//this.reference_collumn = reference_collumn_int;
	}

	@Override
	protected void dispatch() throws InvalidReferenceException, OutofBondsException, InvalidPermissionException, CellIsProtectedException, UserNotInSessionException, SpreadsheetDoesNotExistException {
		Spreadsheet spreadsheet = getSpreadsheet(this.docId);
		//Cell referenced_cell = getCellByCoords(spreadsheet, this.reference_row, this.reference.collumn);
		//Reference reference = new Reference(referenced_cell);
		
		//TODO BubbleDocs needs a method that given a token, returns true if its valid and false otherwise.
        //TODO BubbleDocs needs a method that given a token gives the username that it belongs to for permission checking and to add the reference to the spreadsheet.
		//user.addReferencetoCell(reference, spreadsheet, this.cell_row, this.cell_collumn);
		
		result = getCellByCoords(spreadsheet, this.cell_row, this.cell_collumn).getContent().toString();
	}

	public final String getResult() {
		return result;
	}
}