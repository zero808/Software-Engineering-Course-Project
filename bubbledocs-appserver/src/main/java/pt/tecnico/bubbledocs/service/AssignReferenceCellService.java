package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class AssignReferenceCell extends BubbleDocsService {
	
	private String result;
	private int cell_row;
	private int cell_collumn;
	private int reference_row;
	private int reference_collumn;
	private int docId;
	private String tokenUser;

	public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
		String cell_parts[] = cellId.split(";");
		String cell_row = cell_parts[0];
		String cell_collumn = cell_parts[1];
		int cell_row_int = Integer.parseInt(cell_row);
		int cell_collumn_int = Integer.parseInt(cell_collumn);
		
		String reference_parts[] = reference.split(";");
		String reference_row = reference_parts[0];
		String reference_collumn = reference_parts[1];
		int reference_row_int = Integer.parseInt(reference_row);
		int reference_collumn_int = Integer.parseInt(reference_collumn);
		
		this.docId = docId;
		this.tokenUser = tokenUser;
		this.cell_row = cell_row_int;
		this.cell_collumn = cell_collumn_int;
		this.reference_row = reference_row_int;
		this.reference_collumn = reference_collumn_int;
	}

	@Override
	protected void dispatch() throws InvalidReferenceException, InvalidTokenException, OutofBoundsException, InvalidArgumentsException, InvalidPermissionException, CellIsProtectedException, UserNotInSessionException, SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		String username = bd.getUsernameByToken(tokenUser);
		
		if(tokenUser.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(tokenUser))) {
			throw new UserNotInSessionException(username);
		}
		
		User user = bd.getUserByUsername(username); //Not my responsibility to test if its null, it shouldn't.
		
		Cell referenced_cell = new Cell(reference_row, reference_collumn, false);
		Reference reference = new Reference(referenced_cell);

		if(user.hasOwnerPermission(spreadsheet)) {
			user.addReferencetoCell(reference, spreadsheet, cell_row, cell_collumn);

			result = getCellByCoords(spreadsheet, cell_row, cell_collumn).getContent().toString();
		}else {
			if(user.hasPermission(spreadsheet)) {
				user.addReferencetoCell(reference, spreadsheet, cell_row, cell_collumn);

				result = getCellByCoords(spreadsheet, cell_row, cell_collumn).getContent().toString();
			}else {
				throw new InvalidPermissionException(username);
			}
		}
	}

	public final String getResult() {
		return result;
	}
}// End AssignReferenceCell class