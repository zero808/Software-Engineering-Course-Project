package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class AssignLiteralCell extends BubbleDocsService {
	
	private String result;
	private String _tokenUser;
	private int _docId;
	private String _cellId;
	private int _literal;
	
	public AssignLiteralCell(String tokenUser, int docId, String cellId, String literal) {
		_tokenUser = tokenUser;
		_docId = docId;
		_literal = Integer.parseInt(literal);
		_cellId = cellId;	
	}

	@Override
	protected void dispatch() throws OutofBoundsException, CellIsProtectedException, SpreadsheetDoesNotExistException, UserNotInSessionException, InvalidTokenException, InvalidPermissionException {
		Spreadsheet s = getSpreadsheet(_docId);
		BubbleDocs bd = getBubbleDocs();
		String username = bd.getUsernameByToken(_tokenUser);
		
		if(_tokenUser.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(_tokenUser))) {
			throw new UserNotInSessionException(username);
		}
		
		User user = bd.getUserByUsername(username); //Not my responsibility to test if its null, it shouldn't.
		
		String cell_parts[] = _cellId.split(";");
		int cellRow = Integer.parseInt(cell_parts[0]);
		int cellCol = Integer.parseInt(cell_parts[1]);
		
		Cell c = getCellByCoords(s, cellRow, cellCol);
		
		if(c.getWProtected()) {
			throw new CellIsProtectedException(c.getRow(), c.getCollumn());
		}
		
		Literal l = new Literal(_literal);
		
		if(user.hasOwnerPermission(s)) {
			user.addLiteraltoCell(l, s, cellRow, cellCol);

			result = getCellByCoords(s, cellRow, cellCol).getContent().toString();
		}else {
			if(user.hasPermission(s)) {
				user.addLiteraltoCell(l, s, cellRow, cellCol);

				result = getCellByCoords(s, cellRow, cellCol).getContent().toString();
			}else {
				throw new InvalidPermissionException(username);
			}
		}
	}

	public String getResult() {
		return result;
	}
}// End AssignLiteralCell class