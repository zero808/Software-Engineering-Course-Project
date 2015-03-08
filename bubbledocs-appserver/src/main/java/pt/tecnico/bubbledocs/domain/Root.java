package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

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
	   
	   @Override
	   public User addUser(String username, String name, String pass){
		   return new User(username, name, pass);
	   }
	   
	   @Override
	   public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException {
		   Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);
		   
		   if(toRemove == null) throw new SpreadsheetDoesNotExistException(spreadsheetName);
		   
		   
	   }
}
