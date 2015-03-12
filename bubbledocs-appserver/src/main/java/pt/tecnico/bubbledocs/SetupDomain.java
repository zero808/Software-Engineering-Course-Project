package pt.tecnico.bubbledocs;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;

public class SetupDomain {

    @Atomic
    public static void main(String[] args) {
    	populateDomain();
    }
    
    static void populateDomain() {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	Root root = Root.getInstance();
    	
    	root.addUser("pf", "Paul Door", "sub");
    	root.addUser("ra", "Step Rabbit", "cor");
    	
    	User pf = bd.getUserByName("pf");
    	Spreadsheet notas = new Spreadsheet("Notas ES", new DateTime(), 300, 20);
    	pf.addSpreadsheets(notas);
    	
    	Literal l = new Literal(5);
    	pf.addLiteraltoCell(l, notas, 3, 4);
    	
    	Cell c1 = notas.getCellByCoords(5, 6);
    	Reference r = new Reference(c1);
    	pf.addReferencetoCell(r, notas, 1, 1);
    	
    	Literal l2 = new Literal(2);
    	Cell c2 = notas.getCellByCoords(3, 4);
    	Reference r2 = new Reference(c2);
    	
    	Add add = new Add(l2, r2);
    	pf.addFunctiontoCell(add, notas, 5, 6);
    	
    	Cell c3 = notas.getCellByCoords(1, 1);
    	Cell c4 = notas.getCellByCoords(3, 4);
    	Reference r3 = new Reference(c3);
    	Reference r4 = new Reference(c4);
    	
    	Div div = new Div(r3, r4);
    	pf.addFunctiontoCell(div, notas, 2, 2);
    }
}
