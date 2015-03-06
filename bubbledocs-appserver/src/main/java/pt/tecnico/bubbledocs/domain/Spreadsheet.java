package pt.tecnico.bubbledocs.domain;

import org.joda.time.DateTime;

public class Spreadsheet extends Spreadsheet_Base {
    
    public Spreadsheet(String name, DateTime date, int nRows, int nCollumns) {
        super();
        
        int _idnext;
		
		BubbleDocs bd = getBubbledocs();
		_idnext = bd.getIdGlobal();
		_idnext++;
		
		setId(_idnext);
	    setName(name);
	    setDate(date);
	    setNRows(nRows);
	    setNCols(nCollumns);
    }
    
    public void delete() {
    	//TODO
    }
    
}//End Spreadsheet Class
