package pt.tecnico.bubbledocs.integration.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mockit.Mocked;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.service.integration.AssignBinaryCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignReferenceCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignUnaryCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class LocalSystemTest extends SystemTest {

	@Mocked
	private StoreRemoteServices storeRemote;
	
	@Mocked
	private IDRemoteServices idRemote;
	
	private BubbleDocs bd;
	private String rootToken;
	private String userToken;
	private int docId;
	
	@Override
	public void populate4Test() {
		bd = BubbleDocs.getInstance();
		Root.getInstance();
	}
	
	@Test
	public void systemExecutionSuccess() {

		LoginUserIntegrator rootLoginService = new LoginUserIntegrator("root", "rootroot");
		rootLoginService.execute();
		rootToken = rootLoginService.getUserToken();

		CreateUserIntegrator createUserService = new CreateUserIntegrator(rootToken, "lff", "lff@tecnico.pt", "Luís");
		createUserService.execute();

		createUserService = new CreateUserIntegrator(rootToken, "xis", "xis@tecnico.pt", "Pedro");
		createUserService.execute();

		LoginUserIntegrator userLoginService = new LoginUserIntegrator("lff", "lffp4ss");
		userLoginService.execute();
		userToken = userLoginService.getUserToken();

		CreateSpreadSheetIntegrator createSpreadsheetService = new CreateSpreadSheetIntegrator(userToken, "System Test", 20, 20);
		createSpreadsheetService.execute();
		docId = createSpreadsheetService.getDocId();

		AssignLiteralCellIntegrator assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "3;4", "5");
		assignLiteralService.execute();

		AssignReferenceCellIntegrator assignReferenceService = new AssignReferenceCellIntegrator(userToken, docId, "1;1", "5;6");
		assignReferenceService.execute();

		AssignBinaryCellIntegrator assignBinaryService = new AssignBinaryCellIntegrator(userToken, docId, "5;6" , "=ADD(2,3;4)");
		assignBinaryService.execute();

		assignBinaryService = new AssignBinaryCellIntegrator(userToken, docId, "2;2" , "=DIV(1;1,3;4)");
		assignBinaryService.execute();

		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "10;10", "5");
		assignLiteralService.execute();
		
		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "10;11", "5");
		assignLiteralService.execute();
		
		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "11;10", "5");
		assignLiteralService.execute();
		
		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "11;11", "5");
		assignLiteralService.execute();

		AssignUnaryCellIntegrator assignUnaryService = new AssignUnaryCellIntegrator(userToken, docId, "12;12", "=AVG(10;10:11;11)");
		assignUnaryService.execute();

		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "15;16", "5");
		assignLiteralService.execute();
		
		assignLiteralService = new AssignLiteralCellIntegrator(userToken, docId, "15;15", "5");
		assignLiteralService.execute();
		
		assignReferenceService = new AssignReferenceCellIntegrator(userToken, docId, "16;15", "15;15");
		assignReferenceService.execute();
		
		assignReferenceService = new AssignReferenceCellIntegrator(userToken, docId, "16;16", "18;18");
		assignReferenceService.execute();

		assignUnaryService = new AssignUnaryCellIntegrator(userToken, docId, "17;17", "=AVG(15;15:16;16)");
		assignUnaryService.execute();	
		
		assertEquals("lff", bd.getUserByUsername("lff").getUsername());
		assertEquals("xis", bd.getUserByUsername("xis").getUsername());
		assertEquals(0, bd.getSpreadsheetById(0).getId());
	}
}// End LocalSystemTest class
