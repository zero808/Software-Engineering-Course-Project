package pt.tecnico.bubbledocs.service;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CreateSpreadSheetService extends BubbleDocsService {
	
	private int sheetId;
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
	
	public CreateSpreadSheetService(String userToken, String name, int rows, int columns) {
		this.token = userToken;
		this.name = name;
		nrow = rows;
		ncol = columns;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		DateTime date = new DateTime();

		String username = bd.getUsernameByToken(token);
		
		User user = bd.getUserByUsername(username);	
		
		Spreadsheet s = new Spreadsheet(name,date,nrow,ncol);
		
		user.addSpreadsheets(s);
		this.s = s;
	}

	@Override
	protected void checkAccess() {
		return;
	}
}// End CreateSpreadSheetService class