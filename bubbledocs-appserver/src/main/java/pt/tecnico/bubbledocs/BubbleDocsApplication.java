package pt.tecnico.bubbledocs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.service.AssignBinaryCellService;
import pt.tecnico.bubbledocs.service.AssignLiteralCellService;
import pt.tecnico.bubbledocs.service.AssignReferenceCellService;
import pt.tecnico.bubbledocs.service.AssignUnaryCellService;
import pt.tecnico.bubbledocs.service.CreateSpreadSheetService;
import pt.tecnico.bubbledocs.service.CreateUserService;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;

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
			
			LoginUserIntegrator service_login = new LoginUserIntegrator("root", "rootroot");
			service_login.execute();
			LoginUserIntegrator service_login_pf = new LoginUserIntegrator("pff", "sub");
			service_login_pf.execute();
			String pfToken = service_login_pf.getUserToken();
			LoginUserIntegrator service_login_ra = new LoginUserIntegrator("raa", "cor");
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
		
		LoginUserIntegrator service_login = new LoginUserIntegrator("root", "rootroot");
		service_login.execute();
		String rootToken = service_login.getUserToken();
		
		CreateUserService service_pf = new CreateUserService(rootToken, "pff", "pf@stuff", "Paul Door");
		service_pf.execute();
		
		CreateUserService service_ra = new CreateUserService(rootToken, "raa", "ra@stuff", "Step Rabbit");
		service_ra.execute();
		
		LoginUserIntegrator service_login_pf = new LoginUserIntegrator("pff", "sub");
		service_login_pf.execute();
		String pfToken = service_login_pf.getUserToken();
		
		CreateSpreadSheetService service_spreadsheet = new CreateSpreadSheetService(pfToken, "Notas ES", 300, 20);
		service_spreadsheet.execute();
		
		int docId = bd.getSpreadsheetByName("Notas ES").getId();
		
		//Literal 5 on position (3,4).
		AssignLiteralCellService service_literal1 = new AssignLiteralCellService(pfToken, docId, "3;4", "5");
		service_literal1.execute();

		//Reference to (5, 6) on (1, 1).
		AssignReferenceCellService service_reference1 = new AssignReferenceCellService(pfToken, docId, "1;1", "5;6");
		service_reference1.execute();

		//Function Add with arguments Literal 2 and Reference to (3, 4) on (5, 6).
		AssignBinaryCellService service_binary1 = new AssignBinaryCellService(pfToken, docId, "5;6" , "=ADD(2,3;4)");
		service_binary1.execute();
			
		//Function Div with arguments Reference (1, 1) and Reference to (3, 4) on (2, 2)
		AssignBinaryCellService service_binary2 = new AssignBinaryCellService(pfToken, docId, "2;2" , "=DIV(1;1,3;4)");
		service_binary2.execute();
		
		//Literal 5 on position (10,10), (10,11), (11,10), (11,11).
		AssignLiteralCellService service_literal2 = new AssignLiteralCellService(pfToken, docId, "10;10", "5");
		service_literal2.execute();
		AssignLiteralCellService service_literal3 = new AssignLiteralCellService(pfToken, docId, "10;11", "5");
		service_literal3.execute();
		AssignLiteralCellService service_literal4 = new AssignLiteralCellService(pfToken, docId, "11;10", "5");
		service_literal4.execute();
		AssignLiteralCellService service_literal5 = new AssignLiteralCellService(pfToken, docId, "11;11", "5");
		service_literal5.execute();
		
		//Avg between (10,10) and (11,11) on (12,12)
		AssignUnaryCellService service_unary1 = new AssignUnaryCellService(pfToken, docId, "12;12", "=AVG(10;10:11;11)");
		service_unary1.execute();
		
		//Literal 5 on position (15,15), (15,16)
		//Reference to literal 5 on position (16,15)
		//#Value on position (16,16).
		AssignLiteralCellService service_literal6 = new AssignLiteralCellService(pfToken, docId, "15;16", "5");
		service_literal6.execute();
		AssignLiteralCellService service_literal7 = new AssignLiteralCellService(pfToken, docId, "15;15", "5");
		service_literal7.execute();
		AssignReferenceCellService service_reference2 = new AssignReferenceCellService(pfToken, docId, "16;15", "15;15");
		service_reference2.execute();
		AssignReferenceCellService service_reference3 = new AssignReferenceCellService(pfToken, docId, "16;16", "18;18");
		service_reference3.execute();
		
		//Avg between (15,15) and (16,16) on (17,17)
		AssignUnaryCellService service_unary2 = new AssignUnaryCellService(pfToken, docId, "17;17", "=AVG(15;15:16;16)");
		service_unary2.execute();
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
			ExportDocumentIntegrator service = new ExportDocumentIntegrator(userToken, s.getId());
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