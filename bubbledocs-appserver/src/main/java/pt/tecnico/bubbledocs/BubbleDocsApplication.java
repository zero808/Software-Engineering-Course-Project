package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.SetupDomain;

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
		    setupIfNeed(bd, root);
		    
		    //bd.setIdGlobal(bd.getIdGlobal() + 1);
		    //System.out.println("ID:" + bd.getIdGlobal());
		    
		    //Do stuff here later
		    
		    tm.commit();
		    committed = true;
		}catch (SystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
		    System.err.println("Error in execution of transaction: " + ex);
		} finally {
		    if (!committed) 
			try {
			    tm.rollback();
			} catch (SystemException ex) {
			    System.err.println("Error in roll back of transaction: " + ex);
				}
	    	}
	   	
	   	//org.jdom2.Document doc = convertToXML();

		//printDomainInXML(doc);
    }

    // setup the initial state if bubbledocs is empty
    private static void setupIfNeed(BubbleDocs bd, Root root) {
    	if (bd.getUsersSet().isEmpty())
    		SetupDomain.populateDomain();
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
}
