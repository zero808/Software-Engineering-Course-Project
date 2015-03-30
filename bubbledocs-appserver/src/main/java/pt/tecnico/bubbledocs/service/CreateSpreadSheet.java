package pt.tecnico.bubbledocs.service;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class CreateSpreadSheet extends BubbleDocsService {
	private int sheetId;  // id of the new sheet
	private String userToken;
	private String name;
	private int nrow;
	private int ncol;
	private Spreadsheet s;
	
	public int getSheetId() {
		return sheetId;
	}
	
	public Spreadsheet getSheet(){
		return s;
	}
	
	public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
		this.userToken = userToken;
		this.name = name;
		nrow = rows;
		ncol = columns;
	}

	@Override
	protected void dispatch() throws InvalidTokenException, UserNotInSessionException, InvalidBoundsException, InvalidSpreadsheetNameException {
		BubbleDocs bd = getBubbleDocs();
		DateTime date = new DateTime();

		String username = bd.getUsernameByToken(userToken);
		
		if(userToken.equals(""))
			throw new InvalidTokenException();
		
		if(!(bd.isInSession(userToken))) 
			throw new UserNotInSessionException(username);
		
		User user = bd.getUserByUsername(username);
		
		if(nrow < 1 || ncol < 1) 
			throw new InvalidBoundsException(nrow, ncol);
		
		//only accept spreadsheet name in Alfa numeric format plus
		//symbols + - _ and spaces
		if(name.matches("(.*)[^ A-Za-z0-9_+-](.*)") || name.equals(""))
			throw new InvalidSpreadsheetNameException();
		
		Spreadsheet s = new Spreadsheet(name,date,nrow,ncol);
		user.addSpreadsheets(s);
		this.s = s;
	}
}// End CreateSpreadSheet class