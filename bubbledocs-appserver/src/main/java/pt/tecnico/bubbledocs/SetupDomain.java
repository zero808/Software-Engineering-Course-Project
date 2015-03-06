package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

//import pt.ist.fenixframework.FenixFramework;
//import pt.ist.fenixframework.Config;

public class SetupDomain {

    @Atomic
    public static void main(String[] args) {
    	populateDomain();
    }
    
    static void populateDomain() {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	
    	//Populate BD with initial data here later
    	//Currently bd is unused so there's a small warning
    }
}
