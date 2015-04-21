package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public abstract class BubbleDocsIntegrator {

	public final void execute() throws BubbleDocsException {
		dispatch();
	}

	protected abstract void dispatch() throws BubbleDocsException;
}// End BubbleDocsIntegrator class