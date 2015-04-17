package pt.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class Permission extends Permission_Base {
	
	public Permission() {
		super();
	}

	public Permission(Spreadsheet spred, User user, boolean b) throws SpreadsheetDoesNotExistException, LoginBubbleDocsException {

		if (spred == null)
			throw new SpreadsheetDoesNotExistException();
		if (user == null)
			throw new LoginBubbleDocsException("username");

		setRw(b);
		setUser(user);
		setSpreadsheet(spred);

		spred.addPermissions(this);
	}

	public void edit(boolean b) {
		setRw(b);
	}

	public void delete() {
		setUser(null);
		setSpreadsheet(null);
		deleteDomainObject();
	}
	
	public Element exportToXML() {
		Element element = new Element("permission");

		element.setAttribute("spreadsheet", getSpreadsheet().getName());
		element.setAttribute("user", getUser().getUsername());
		element.setAttribute("type", Boolean.toString(getRw()));

		Element spreadsheetsElement = new Element("spreadsheets");
		element.addContent(spreadsheetsElement);

		return element;
	}
	
	public void importFromXML(Element permissionElement) {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		Spreadsheet s = bd.getSpreadsheetByName(permissionElement.getAttribute("spreadsheet").getValue());
		setSpreadsheet(s);
		
		User u = bd.getUserByUsername(permissionElement.getAttribute("user").getValue());
		setUser(u);
		
		try {
			setRw(permissionElement.getAttribute("type").getBooleanValue());
		} catch (DataConversionException e) {
			throw new ImportDocumentException("permission");
		}
	}
}// End Permission class