package pt.tecnico.bubbledocs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.Avg;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Range;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.LoginUser;

import javax.transaction.*;

import org.jdom2.Document;
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
			
			if (!isInicialized()) {
				populateDomain(bd, root);
			}
			
			LoginUser service_login = new LoginUser("root", "rootroot");
			service_login.execute();
			LoginUser service_login_pf = new LoginUser("pff", "sub");
			service_login_pf.execute();
			String pfToken = service_login_pf.getUserToken();
			LoginUser service_login_ra = new LoginUser("raa", "cor");
			service_login_ra.execute();
			
			org.jdom2.Document wholeDoc = new org.jdom2.Document();
			
			try {
				wholeDoc = convertToXML();
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			try {
				listAllUsers();
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			} 
			
			try {
				listSpreadsheetsOf("pff");
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			} 
			
			try {
				listSpreadsheetsOf("raa");
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			
			ArrayList<org.jdom2.Document> docs_pf = convertSpreadsheetsOfUserToXML(pfToken);
			
			try {
				for(org.jdom2.Document j : docs_pf) {
					printDomainInXML(j);
				}
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			try {
				deleteSpreadsheetOf("pff", "Notas ES");
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			try {
				listSpreadsheetsOf("pff");
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			try {
				recoverFromBackup(wholeDoc);
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			try {
				listSpreadsheetsOf("pff");
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}
			
			docs_pf = convertSpreadsheetsOfUserToXML(pfToken);

			try {
				for(org.jdom2.Document j : docs_pf) {
					printDomainInXML(j);
				}
			} catch (BubbleDocsException exception) {
				exception.printStackTrace();
				System.out.println(exception.toString());
			}

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
		
		LoginUser service_login = new LoginUser("root", "rootroot");
		service_login.execute();
		String rootToken = service_login.getUserToken();
		
		CreateUser service_pf = new CreateUser(rootToken, "pff", "sub", "Paul Door");
		service_pf.execute();
		
		CreateUser service_ra = new CreateUser(rootToken, "raa", "cor", "Step Rabbit");
		service_ra.execute();
		
		LoginUser service_login_pf = new LoginUser("pff", "sub");
		service_login_pf.execute();
		String pfToken = service_login_pf.getUserToken();
		
		CreateSpreadSheet service_spreadsheet = new CreateSpreadSheet(pfToken, "Notas ES", 300, 20);
		service_spreadsheet.execute();
		
		int docId = bd.getSpreadsheetByName("Notas ES").getId();
		Spreadsheet notas = bd.getSpreadsheetByName("Notas ES");
		User pf = bd.getUserByUsername("pff");
		
		//Literal 5 on position (3,4).
		AssignLiteralCell service_literal1 = new AssignLiteralCell(pfToken, docId, "3;4", "5");
		service_literal1.execute();

		//Reference to (5, 6) on (1, 1).
		AssignReferenceCell service_reference1 = new AssignReferenceCell(pfToken, docId, "1;1", "5;6");
		service_reference1.execute();

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
		
		//Literal 5 on position (10,10), (10,11), (11,10), (11,11).
		AssignLiteralCell service_literal2 = new AssignLiteralCell(pfToken, docId, "10;10", "5");
		service_literal2.execute();
		AssignLiteralCell service_literal3 = new AssignLiteralCell(pfToken, docId, "10;11", "5");
		service_literal3.execute();
		AssignLiteralCell service_literal4 = new AssignLiteralCell(pfToken, docId, "11;10", "5");
		service_literal4.execute();
		AssignLiteralCell service_literal5 = new AssignLiteralCell(pfToken, docId, "11;11", "5");
		service_literal5.execute();
		
		//Avg between (10,10) and (11,11) on (12,12)
		Avg avg = new Avg(new Range(notas.getCellByCoords(10, 10), notas.getCellByCoords(11, 11)));
		pf.addFunctiontoCell(avg, notas, 12, 12);	
		
		//Literal 5 on position (15,15), (15,16)
		//Reference to literal 5 on position (16,15)
		//#Value on position (16,16).
		AssignLiteralCell service_literal6 = new AssignLiteralCell(pfToken, docId, "15;16", "5");
		service_literal6.execute();
		AssignLiteralCell service_literal7 = new AssignLiteralCell(pfToken, docId, "15;15", "5");
		service_literal7.execute();
		AssignReferenceCell service_reference2 = new AssignReferenceCell(pfToken, docId, "16;15", "15;15");
		service_reference2.execute();
		AssignReferenceCell service_reference3 = new AssignReferenceCell(pfToken, docId, "16;16", "18;18");
		service_reference3.execute();
		
		//Avg between (15,15) and (16,16) on (17,17)
		Avg avg2 = new Avg(new Range(notas.getCellByCoords(15, 15), notas.getCellByCoords(16, 16)));
		pf.addFunctiontoCell(avg2, notas, 17, 17);	
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
	public static ArrayList<org.jdom2.Document> convertSpreadsheetsOfUserToXML(String userToken) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(bd.getUsernameByToken(userToken));
		
		ArrayList<Document> documentsList = new ArrayList<org.jdom2.Document>();
		
		for(Spreadsheet s : user.getSpreadsheetsSet()) {
			ExportDocument service = new ExportDocument(userToken, s.getId());
			service.execute();
			
			byte[] serviceDocBytes = service.getDocXML();
			
			org.jdom2.Document serviceDoc = new org.jdom2.Document();
			
			SAXBuilder builder = new SAXBuilder();
			builder.setIgnoringElementContentWhitespace(true);
			try {
				serviceDoc = builder.build(new ByteArrayInputStream(serviceDocBytes));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
			
			documentsList.add(serviceDoc);
		}
		
		return documentsList;
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
}// End BubbleDocsApplication class