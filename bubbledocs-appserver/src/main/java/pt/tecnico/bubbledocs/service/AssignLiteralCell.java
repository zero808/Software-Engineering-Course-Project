package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignLiteralCell extends BubbleDocsService {
	private String result;
	private String _accessUsername;
	private int _docId;
	private int _cellRow;
	private int _cellCol;
	private int _literal;
	
	public AssignLiteralCell(String accessUsername, int docId, String cellId,
			String literal) {
		_accessUsername = accessUsername;
		_docId = docId;
		_literal = Integer.parseInt(literal);
		
		String cell_parts[] = cellId.split(";");
		_cellRow = Integer.parseInt(cell_parts[0]);
		_cellCol = Integer.parseInt(cell_parts[1]);
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		//TODO
		//Validar sessão
		//Validar permissão
		Spreadsheet s = getSpreadsheet(_docId);
		Cell c = getCellByCoords(s, _cellRow, _cellCol);
		Literal l = new Literal(_literal);
		c.setContent(l);
	}

	public String getResult() {
		return result;
	}

}