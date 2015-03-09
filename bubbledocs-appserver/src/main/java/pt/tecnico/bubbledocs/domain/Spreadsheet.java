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
    	
		setUser(null);
		deleteDomainObject();
    }

    public void printCells() {
    	 
		for (Cell cell : getCellsSet()) {
	    	cell.toString(); //toString precisa de ser feito para cada tipo de conteudo
			}
    }
    
    public String toString() {
    	return "Nome:" + getName() + "Data Criacao:" + getDate() + "N-Linhas:" + getNRows() + "N-Colunas:" + getNCols();
    }
    
}//End Spreadsheet Class
