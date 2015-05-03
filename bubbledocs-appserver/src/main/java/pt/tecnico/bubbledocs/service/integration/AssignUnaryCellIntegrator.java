package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignUnaryCellService;

public class AssignUnaryCellIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int docId;
	private String cellId;
	private String func;
	
	public AssignUnaryCellIntegrator(String tokenUser, int docId, String cellId, String func) {
		this.tokenUser = tokenUser;
		this.docId = docId;
		this.func = func;
		this.cellId = cellId;	
	}

	@Override
	protected void dispatch() throws OutofBoundsException, CellIsProtectedException, SpreadsheetDoesNotExistException, UserNotInSessionException, InvalidTokenException, InvalidPermissionException {
		AssignUnaryCellService assignUnaryCellService = new AssignUnaryCellService(tokenUser, docId, cellId, func);
		assignUnaryCellService.execute();
	}
}// End of AssignUnaryCellIntegrator class
