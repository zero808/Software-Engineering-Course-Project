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
	private String _cellId;
	private int _literal;
	
	public AssignLiteralCell(String accessUsername, int docId, String cellId,
			String literal) {
		_accessUsername = accessUsername;
		_docId = docId;
		//testa o inteiro antes
		_literal = Integer.parseInt(literal);
		_cellId = cellId;
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		//TODO
		//Validar sessão
		//Validar permissão
		
		String cell_parts[] = _cellId.split(";");
		int cellRow = Integer.parseInt(cell_parts[0]);
		int cellCol = Integer.parseInt(cell_parts[1]);
		Spreadsheet s = getSpreadsheet(_docId);
		Cell c = getCellByCoords(s, cellRow, cellCol);
		Literal l = new Literal(_literal);
		c.setContent(l);
	}

	public String getResult() {
		return result;
	}

}