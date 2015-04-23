package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignLiteralCellService extends AccessService {
	
	private String result;
	private int docId;
	private String cellId;
	private int literal;
	
	public AssignLiteralCellService(String tokenUser, int docId, String cellId, String literal) {
		token = tokenUser;
		this.docId = docId;
		this.literal = Integer.parseInt(literal);
		this.cellId = cellId;	
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Spreadsheet s = getSpreadsheet(docId);
		
		String cell_parts[] = cellId.split(";");
		int cellRow = Integer.parseInt(cell_parts[0]);
		int cellCol = Integer.parseInt(cell_parts[1]);
		
		Literal l = new Literal(literal);
		
		user.addLiteraltoCell(l, s, cellRow, cellCol);

		result = getCellByCoords(s, cellRow, cellCol).getContent().toString();
	}

	public String getResult() {
		return result;
	}
}// End AssignLiteralCell class