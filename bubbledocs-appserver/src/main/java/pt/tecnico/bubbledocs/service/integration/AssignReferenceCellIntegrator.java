package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignReferenceCellService;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {
	
	private String cellId;
	private String reference;
	private int docId;
	private String tokenUser;

	public AssignReferenceCellIntegrator(String tokenUser, int docId, String cellId, String reference) {
		this.docId = docId;
		this.tokenUser = tokenUser;
		this.cellId = cellId;
		this.reference = reference;
	}

	@Override
	protected void dispatch() throws InvalidReferenceException, InvalidTokenException, OutofBoundsException, InvalidArgumentsException, InvalidPermissionException, CellIsProtectedException, UserNotInSessionException, SpreadsheetDoesNotExistException {
		AssignReferenceCellService assignReferenceCellService = new AssignReferenceCellService(tokenUser, docId, cellId, reference);
		assignReferenceCellService.execute();
	}
}// End AssignReferenceCellIntegrator class