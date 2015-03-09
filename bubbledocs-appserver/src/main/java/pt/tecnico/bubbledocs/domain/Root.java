package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;

public class Root extends Root_Base {
	
	   private static Root instance = null;
	   private Root() {
		  setBubbledocs(FenixFramework.getDomainRoot().getBubbledocs());
	      setId(0);
	      setUsername("root");
	      setName("Super User");
	      //setPassword("");
	   }
	   public static Root getInstance() {
	      if(instance == null) {
	         instance = new Root();
	      }
	      return instance;
	   }
	   
	   private Spreadsheet getSpreadsheetByName(String spreadsheetName){
		   //TODO
		   return null;
	   }
	   
	   private User getUserByName(String name) {
		   BubbleDocs bd = getBubbledocs();
		   for(User usr : bd.getUsersSet()) {
			   if(usr.getName().equals(name)) {
				   return usr;
				}
			}
			return null;
		}
	   
	   public void removeUser(String username) throws UserDoesNotExistException {
		   BubbleDocs bd = getBubbledocs();
		   User toRemove = getUserByName(username);

		   if(toRemove == null) throw new UserDoesNotExistException(username);
		   
		   bd.removeUsers(toRemove);
		   toRemove.delete();
	   }
	   
	   @Override
	   public void addUser(String username, String name, String pass) throws UserAlreadyExistsException {
		   BubbleDocs bd = getBubbledocs();

		   User usr = getUserByName(username);
		   if (usr != null)
			   throw new UserAlreadyExistsException(username);

		   bd.addUsers(new User(username, name, pass));
		   
	   }
	   
	   @Override
	   public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException {
		   Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);
		   
		   if(toRemove == null) throw new SpreadsheetDoesNotExistException(spreadsheetName);
		   
		   super.removeSpreadsheets(toRemove);
		   toRemove.delete();
		   
		   
	   }
}
