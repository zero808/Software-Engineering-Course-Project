package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignReferenceCell extends BubbleDocsService {
	private String result;

	public AssignReferenceCell(String tokenUser, int docId, String cellId,
			String reference) {
		// add code here
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// add code here
	}

	public final String getResult() {
		return result;
	}
}