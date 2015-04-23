package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignLiteralCellService;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int docId;
	private String cellId;
	private String literal;
	
	public AssignLiteralCellIntegrator(String tokenUser, int docId, String cellId, String literal) {
		this.tokenUser = tokenUser;
		this.docId = docId;
		this.literal = literal;
		this.cellId = cellId;	
	}

	@Override
	protected void dispatch() throws OutofBoundsException, CellIsProtectedException, SpreadsheetDoesNotExistException, UserNotInSessionException, InvalidTokenException, InvalidPermissionException {
		AssignLiteralCellService assignLiteralCellService = new AssignLiteralCellService(tokenUser, docId, cellId, literal);
		assignLiteralCellService.execute();
	}
}// End AssignLiteralCellIntegrator class