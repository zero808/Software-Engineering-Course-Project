package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheetService;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private String name;
	private int rows;
	private int columns;
	private int docId;
	
	public CreateSpreadSheetIntegrator(String userToken, String name, int rows, int columns) {
		this.userToken = userToken;
		this.name = name;
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	protected void dispatch() throws InvalidTokenException, UserNotInSessionException, InvalidBoundsException, InvalidSpreadsheetNameException {
		CreateSpreadSheetService createSpreadSheetService = new CreateSpreadSheetService(userToken, name, rows, columns);
		createSpreadSheetService.execute();
		
		docId = createSpreadSheetService.getSheetId();
	}
	
	public int getDocId() {
		return docId;
	}
}// End CreateSpreadSheetIntegrator class
