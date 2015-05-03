package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContentService;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private String ssName;
	private GetSpreadSheetContentService getSpreadSheetContentService;
	
	public GetSpreadSheetContentIntegrator(String tokenUser, String spreadSheetName) {
		this.tokenUser = tokenUser;
		this.ssName = spreadSheetName;	
	}

	@Override
	protected void dispatch() throws OutofBoundsException, CellIsProtectedException, SpreadsheetDoesNotExistException, UserNotInSessionException, InvalidTokenException, InvalidPermissionException {
		getSpreadSheetContentService = new GetSpreadSheetContentService(tokenUser, ssName);
		getSpreadSheetContentService.execute();
	}
}// End GetSpreadSheetContentIntegrator class