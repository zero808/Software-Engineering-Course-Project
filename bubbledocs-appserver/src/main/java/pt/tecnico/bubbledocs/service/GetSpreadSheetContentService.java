package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContentService extends AccessService {
	
	private int ssId;
	private String[][] matrix;

	public GetSpreadSheetContentService(String userToken, int spreadSheetId) {
		this.token = userToken;
		this.ssId = spreadSheetId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Spreadsheet s = getSpreadsheet(ssId);
		final int cols = s.getNCols();
		final int rows = s.getNRows();
		matrix = new String[rows][cols];
		
		for(int rowsIterator = 0; rowsIterator < rows; ++rowsIterator) {
			for(int colsIterator = 0; colsIterator < cols; ++colsIterator) {
				String ret;
				if(s.getCellByCoords(rowsIterator, colsIterator) != null) {
					if(s.getCellByCoords(rowsIterator, colsIterator).getContent() != null) {
						ret = s.getCellByCoords(rowsIterator, colsIterator).getContent().toString();
						matrix[rowsIterator][colsIterator] = "" + ret;
					}
					else
						matrix[rowsIterator][colsIterator] = "#VALUE";
				}
				else
					matrix[rowsIterator][colsIterator] = "#VALUE";
			}
		}
	}
	
	public int getSpreadSheetId() {
		return ssId;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}
}// End GetSpreadSheetContentService class
