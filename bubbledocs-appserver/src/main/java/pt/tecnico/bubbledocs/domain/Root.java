package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;

/**
 * Class that describes the singleton Root.
 * 
 * It's a special type of user, which has more power
 * then the average user, in the sense that it can
 * create and delete other users.
 */

public class Root extends Root_Base {
	
	/**
	 * Since this is a singleton, this method
	 * is used to return the only instance of Root.
	 * 
	 * @return {Root} The single instance of Root.
	 */
	
	public static Root getInstance() {
		Root instance = null;
		if (instance == null) {
			instance = new Root();
		}
		return instance;
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Root}
	 */

	private Root() {
		super();
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		setBubbledocs(bd);
		
		/** @private */
		setUsername("root");
		/** @private */
		setName("Super User");
		/** @private */
		setPassword("rootroot");
		/** @private */
		setEmail("root@root.pt");
		
		bd.addUsers(this);
	}
	
	/**
	 * Method that removes a user.
	 * 
	 * @param {String} username The username of the user to remove.
	 * @throws LoginBubbleDocsException
	 */
	
	public void removeUser(String username) throws LoginBubbleDocsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		User toRemove = bd.getUserByUsername(username);

		if (toRemove == null)
			throw new LoginBubbleDocsException("username");

		bd.removeUsers(toRemove);
	}
	
	/**
	 * Method that adds a new user.
	 * 
	 * @param {User} u The new user to be added to the application.
	 * @throws UserAlreadyExistsException, InvalidArgumentsException
	 */

	public void addUser(User u) throws UserAlreadyExistsException, InvalidArgumentsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if(bd.getUsersSet().isEmpty()) {
			bd.addUsers(u);
		} else {
			
			if(bd.getUserByUsername(u.getUsername()) != null) {
				throw new UserAlreadyExistsException(u.getUsername());
			}
			
			if(u.getName() != null && u.getEmail() != null && u.getUsername() != null) {
				bd.addUsers(u);
			} else {
				throw new InvalidArgumentsException();
			}	
		}	
	}
	
	/**
	 * Method that removes a permission from a user.
	 * 
	 * @param {Spreadsheet} s The spreadsheet that the permission refers to.
	 * @param {User} u The user that is having his permission removed.
	 */
	
	@Override
	public void removePermissionfrom(Spreadsheet s, User u) {
		if(s.getPermissionOfUser(u) == null) {
			throw new InvalidPermissionException(u.getName());
		} else {
			s.getPermissionOfUser(u).setRw(false);
		}
	}
	
	/**
	 * Method that gives permission to a user.
	 * 
	 * @param {Spreadsheet} s The spreadsheet that the permission refers to.
	 * @param {User} u The user thats getting a brand new permission.
	 * @param {Boolean} b The type of permission thats being given.
	 */
	
	@Override
	public void givePermissionto(Spreadsheet s, User u, boolean b) {
		new Permission(s, u, b);
	}
	
	/**
	 * Method to check if it is the root user or not.
	 * 
	 * @return {Boolean} True if yes, false otherwise.
	 */
	
	@Override
	public boolean isRoot() {
		return true;
	}		
}// End Root class
