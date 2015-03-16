package pt.tecnico.bubbledocs;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

import org.joda.time.DateTime;

import javax.transaction.*;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
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
			
			populateDomain(bd, root);
			
			tm.commit();
			committed = true;
		} catch (SystemException | NotSupportedException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} finally {
			if (!committed)
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: "
							+ ex);
				}
		}
	
		listAllUsers(); 
		
		listSpreadsheetsOf("pf"); 
		
		listSpreadsheetsOf("ra");
		
		org.jdom2.Document doc = convertSpreadsheetsOfUserToXML("pf");
		printDomainInXML(doc);
		
		try {
			deleteSpreadsheetOf("pf", "Notas ES");
		} catch (SpreadsheetDoesNotExistException e) {
			System.out.println("User does not have that spreadsheet" + e.getSpreadsheetName());
		}
		
		listSpreadsheetsOf("pf");

		//org.jdom2.Document doc = convertToXML();

		//printDomainInXML(doc);

	}

	static void populateDomain(BubbleDocs bd, Root root) {

		User pf = new User("pf", "Paul Door", "sub");
		User ra = new User("ra", "Step Rabbit", "cor");

		root.addUser(pf);
		root.addUser(ra);

		Spreadsheet notas = new Spreadsheet("Notas ES", new DateTime(), 10, 10); //new DateTime() gives the current one.
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
		Cell copy = notas.getCellByCoords(3, 4);
		Cell c4 = new Cell(copy.getRow(), copy.getCollumn(), copy.getWProtected()); //Terrible Hax.
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
	private static void recoverFromBackup(byte[] doc, BubbleDocs bd) {
		try {
			org.jdom2.Document jdomDoc;
			SAXBuilder builder = new SAXBuilder();
			builder.setIgnoringElementContentWhitespace(true);
			try {
				jdomDoc = builder.build(new ByteArrayInputStream(doc));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
				throw new ImportDocumentException();
			}
			Element rootElement = jdomDoc.getRootElement();
			bd.importFromXML(rootElement);
			
		} catch (ImportDocumentException ide) {
			System.err.println("Error importing document");
		}
	}
	
	@Atomic
	static void listAllUsers() {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		for(User u : bd.getUsersSet()) {
			System.out.println("Id: " + u.getId() + " " + "Nome: " + u.getName() + " " + "Username: " + u.getUsername() + " " + "Password: " + u.getPassword() + "\n");
		}
	}
	
	@Atomic
	static void listSpreadsheetsOf(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		
		System.out.println("Spreadsheets of " + user.getUsername() + "\n");
		
		if(user.getSpreadsheetsSet().isEmpty()) {
			System.out.println("User doesnt have any spreadsheets");
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
	public static void deleteSpreadsheetOf(String username, String spreadsheetName) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		
		if(user.getSpreadsheetsSet().isEmpty()) {
			System.out.println("User doesnt have any spreadsheets");
			return;
		}
		
		for(Spreadsheet s : user.getSpreadsheetsSet()) {
			if(s.getName().equals(spreadsheetName)) {
				s.deleteSpreadsheetContent();
			} else {
				throw new SpreadsheetDoesNotExistException(spreadsheetName);
			}
		}
	}

}// End BubbleDocsApplication Class