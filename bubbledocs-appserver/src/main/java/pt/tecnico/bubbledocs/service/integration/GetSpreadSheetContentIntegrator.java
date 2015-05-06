package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContentService;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int ssId;
	private String[][] matrix;
	
	public GetSpreadSheetContentIntegrator(String tokenUser, int spreadSheetId) {
		this.tokenUser = tokenUser;
		this.ssId = spreadSheetId;	
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		GetSpreadSheetContentService getSpreadSheetContentService = new GetSpreadSheetContentService(tokenUser, ssId);
		getSpreadSheetContentService.execute();
		
		matrix = getSpreadSheetContentService.getMatrix();
	}
	
	public String[][] getMatrix() {
		return matrix;
	}
}// End GetSpreadSheetContentIntegrator class
