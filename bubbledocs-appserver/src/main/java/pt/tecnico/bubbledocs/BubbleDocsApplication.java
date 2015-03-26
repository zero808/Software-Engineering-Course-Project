package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

import org.joda.time.DateTime;

import javax.transaction.*;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class BubbleDocsApplication {

	public static void main(String[] args) {
		System.out.println("Welcome to the BubbleDocs application!");

		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
			tm.begin();

			BubbleDocs bd = BubbleDocs.getInstance();
			Root root = Root.getInstance();
			
			if (!isInicialized()) {
				populateDomain(bd, root);
			}
			
			org.jdom2.Document wholeDoc = convertToXML();
//			printDomainInXML(wholeDoc);
			
			listAllUsers(); 
			
			listSpreadsheetsOf("pf"); 
			
			listSpreadsheetsOf("ra");
			
			org.jdom2.Document doc = convertSpreadsheetsOfUserToXML("pf");
			printDomainInXML(doc);
			
			deleteSpreadsheetOf("pf", "Notas ES");
			
			listSpreadsheetsOf("pf");
			
			recoverFromBackup(wholeDoc);
			
			listSpreadsheetsOf("pf");
			
			org.jdom2.Document doc2 = convertSpreadsheetsOfUserToXML("pf");
			printDomainInXML(doc2);

			tm.commit();
			committed = true;
		} catch (SystemException | NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} finally {
			if (!committed)
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
		}
	}
	
	@Atomic
	private static boolean isInicialized() {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		if(bd.getUsersSet().size() == 1) {
			return false;
		} else 
			return true;
	}
	
	@Atomic
	static void populateDomain(BubbleDocs bd, Root root) {

		User pf = new User("pf", "Paul Door", "sub");
		User ra = new User("ra", "Step Rabbit", "cor");

		root.addUser(pf);
		root.addUser(ra);

		Spreadsheet notas = new Spreadsheet("Notas ES", new DateTime(), 300, 20); //new DateTime() gives the current one.
		pf.addSpreadsheets(notas);
		
		//Literal 5 on position (3,4).
		Literal l = new Literal(5);
		pf.addLiteraltoCell(l, notas, 3, 4);

		//Reference to (5, 6) on (1, 1).
		Cell c1 = notas.getCellByCoords(5, 6);
		Reference r = new Reference(c1);
		pf.addReferencetoCell(r, notas, 1, 1);

		//Function Add with arguments Literal 2 and Reference to (3, 4) on (5, 6).
		Literal l2 = new Literal(2); 
		Cell c2 = notas.getCellByCoords(3, 4);
		Reference r2 = new Reference(c2);
		Add add = new Add(l2, r2); 
		pf.addFunctiontoCell(add, notas, 5, 6);
		
		//Function Div with arguments Reference (1, 1) and Reference to (3, 4) on (2, 2)
		Cell c3 = notas.getCellByCoords(1, 1);
		Cell c4 = notas.getCellByCoords(3, 4);
		Reference r3 = new Reference(c3);	
		Reference r4 = new Reference(c4);
		Div div = new Div(r3, r4);
		pf.addFunctiontoCell(div, notas, 2, 2);
	}

	@Atomic
	public static org.jdom2.Document convertToXML() {
		BubbleDocs bd = BubbleDocs.getInstance();

		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(bd.exportToXML());

		return jdomDoc;
	}

	@Atomic
	public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	}
	
	@Atomic
	private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
		BubbleDocs bd = BubbleDocs.getInstance();

		bd.importFromXML(jdomDoc.getRootElement());
	}
	
	@Atomic
	static void listAllUsers() {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		for(User u : bd.getUsersSet()) {
			if(!(u.isRoot())) {
				System.out.println(u.toString());
			}
		}
	}
	
	@Atomic
	static void listSpreadsheetsOf(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		
		System.out.println("Spreadsheets of " + user.getUsername() + ":\n");
		
		if(user.getSpreadsheetsSet().isEmpty()) {
			System.out.println("User doesnt have any spreadsheets.\n");
			return;
		}
		
		for(Spreadsheet s : user.getSpreadsheetsSet()) {
			System.out.println("Nome: " + s.getName() + "\n");
		}
	}
	
	@Atomic
	public static org.jdom2.Document convertSpreadsheetsOfUserToXML(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(user.exportToXML());

		return jdomDoc;
	}
	
	@Atomic
	public static org.jdom2.Document convertSpreadsheetToXML(String spreadsheetName) {
		BubbleDocs bd = BubbleDocs.getInstance();
		Spreadsheet s = bd.getSpreadsheetByName(spreadsheetName);
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(s.exportToXML());

		return jdomDoc;
	}
	
	@Atomic
	public static void deleteSpreadsheetOf(String username, String spreadsheetName) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		
		if(user.getSpreadsheetsSet().isEmpty()) {
			System.out.println("User doesnt have any spreadsheets.\n");
			return;
		}
		
		for(Spreadsheet s : user.getSpreadsheetsSet()) {
			if(s.getName().equals(spreadsheetName)) {
				s.delete();
			} else {
				throw new SpreadsheetDoesNotExistException();
			}
		}
	}

}// End BubbleDocsApplication Class