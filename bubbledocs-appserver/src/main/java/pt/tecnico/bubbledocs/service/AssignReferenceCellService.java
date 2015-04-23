package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignReferenceCellService extends AccessService {
	
	private String result;
	private int cell_row;
	private int cell_collumn;
	private int reference_row;
	private int reference_collumn;

	public AssignReferenceCellService(String tokenUser, int docId, String cellId, String reference) {
		String cell_parts[] = cellId.split(";");
		String cell_row = cell_parts[0];
		String cell_collumn = cell_parts[1];
		int cell_row_int = Integer.parseInt(cell_row);
		int cell_collumn_int = Integer.parseInt(cell_collumn);
		
		String reference_parts[] = reference.split(";");
		String reference_row = reference_parts[0];
		String reference_collumn = reference_parts[1];
		int reference_row_int = Integer.parseInt(reference_row);
		int reference_collumn_int = Integer.parseInt(reference_collumn);
		
		this.docId = docId;
		this.token = tokenUser;
		this.cell_row = cell_row_int;
		this.cell_collumn = cell_collumn_int;
		this.reference_row = reference_row_int;
		this.reference_collumn = reference_collumn_int;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		
		Cell referenced_cell = new Cell(reference_row, reference_collumn, false);
		Reference reference = new Reference(referenced_cell);
		
		user.addReferencetoCell(reference, spreadsheet, cell_row, cell_collumn);

		result = getCellByCoords(spreadsheet, cell_row, cell_collumn).getContent().toString();
	}

	public final String getResult() {
		return result;
	}
}// End AssignReferenceCell class