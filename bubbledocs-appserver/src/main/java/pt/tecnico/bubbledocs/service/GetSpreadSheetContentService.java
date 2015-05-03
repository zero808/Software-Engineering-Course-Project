package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

//import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContentService extends AccessService {
	
	private String ssName;
	private String[][] matrix;

	public GetSpreadSheetContentService(String userToken, String spreadSheetName) {
		this.token = userToken;
		this.ssName = spreadSheetName;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		Spreadsheet s = bd.getSpreadsheetByName(ssName);
		final int cols = s.getNCols();
		final int rows = s.getNRows();
		matrix = new String[rows][cols];
		
		for(int rowsIterator = 0; rowsIterator < rows; ++rowsIterator) {
			for(int colsIterator = 0; colsIterator < cols; ++colsIterator) {
				int ret;
				if(s.getCellByCoords(rowsIterator, colsIterator) != null) {
					if(s.getCellByCoords(rowsIterator, colsIterator).getContent() != null) {
						ret = s.getCellByCoords(rowsIterator, colsIterator).getContent().getValue();
						matrix[rowsIterator][colsIterator] = "" + ret;
					}
					else
						matrix[rowsIterator][colsIterator] = "";
				}
				else
					matrix[rowsIterator][colsIterator] = "";
			}
		}

	}
	
	public String getSpreadSheetName() {
		return ssName;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}

}
