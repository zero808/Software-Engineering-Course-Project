package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

/**
 * Class that describes a users permission in 
 * relation to a specific spreadsheet.
 */

public class Permission extends Permission_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Permission}
	 */
	
	public Permission() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Permission}
	 * 
	 * @param {Spreadsheet} spred The spreadsheet to which the permission refers.
	 * @param {User} user The user to which the permission belongs to.
	 * @param {Boolean} b The type of permission. True is write, false read only.
	 * @throws SpreadsheetDoesNotExistException, LoginBubbleDocsException
	 */
	
	public Permission(Spreadsheet spred, User user, boolean b) throws SpreadsheetDoesNotExistException, LoginBubbleDocsException {

		if (spred == null)
			throw new SpreadsheetDoesNotExistException();
		if (user == null)
			throw new LoginBubbleDocsException("username");
		
		/** @private */
		setRw(b);
		/** @private */
		setUser(user);
		/** @private */
		setSpreadsheet(spred);

		spred.addPermissions(this);
	}
	
	/**
	 * Method that allows for the type of permission
	 * to be changed.
	 * 
	 * @param {Boolean} b The new type of permission.
	 */

	public void edit(boolean b) {
		setRw(b);
	}
	
	/**
	 * Method that deletes a permission.
	 * 
	 * To delete a permission, given the architecture of
	 * the application, first its required to sever all the
	 * connections the permission has. 
	 * More specifically, to the spreadsheet that the permission
	 * refers and to the user the permission belongs to.
	 * After doing that, then the object is deleted.
	 */

	public void delete() {
		setUser(null);
		setSpreadsheet(null);
		deleteDomainObject();
	}
}// End Permission class
