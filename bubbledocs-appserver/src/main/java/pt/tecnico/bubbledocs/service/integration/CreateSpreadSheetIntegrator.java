package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private String name;
	private int rows;
	private int columns;
	
	public CreateSpreadSheetIntegrator(String userToken, String name, int rows, int columns) {
		this.userToken = userToken;
		this.name = name;
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	protected void dispatch() throws InvalidTokenException, UserNotInSessionException, InvalidBoundsException, InvalidSpreadsheetNameException {
		CreateSpreadSheet createSpreadSheetService = new CreateSpreadSheet(userToken, name, rows, columns);
		createSpreadSheetService.execute();
	}
}// End CreateSpreadSheetIntegrator class