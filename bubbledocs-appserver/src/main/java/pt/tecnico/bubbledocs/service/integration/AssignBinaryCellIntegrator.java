package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignBinaryCellService;

public class AssignBinaryCellIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int docId;
	private String cellId;
	private String func;
	
	public AssignBinaryCellIntegrator(String tokenUser, int docId, String cellId, String func) {
		this.tokenUser = tokenUser;
		this.docId = docId;
		this.func = func;
		this.cellId = cellId;	
	}

	@Override
	protected void dispatch() throws OutofBoundsException, CellIsProtectedException, SpreadsheetDoesNotExistException, UserNotInSessionException, InvalidTokenException, InvalidPermissionException {
		AssignBinaryCellService assignBinaryCellService = new AssignBinaryCellService(tokenUser, docId, cellId, func);
		assignBinaryCellService.execute();
	}
}// End of AssignBinaryCellIntegrator class
