package pt.tecnico.bubbledocs.domain;

import org.joda.time.DateTime;

public class Spreadsheet extends Spreadsheet_Base {
    
	Cell _cells[][];
	
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
	    _cells = new Cell[nRows][nCollumns];
    }
    
    public void delete() {
    	
		setUser(null);
		deleteDomainObject();
    }
    
    public void printCells() {
    	
    	int _rows = getNRows();
    	int _c;
    	int _d;
    	int _collumns = getNCols();
    	
    	for ( _c = 0 ; _c < _rows ; _c++ ) {
           for ( _d = 0 ; _d < _collumns ; _d++ )
              System.out.print(_cells[_c][_d].toString() + "\t"); //O metodo toString() precisa de ser feito (para os varios conteudos)
           System.out.println();
        }
    }
    
    public String toString() {
    	return "Nome:" + getName() + "Data Criacao:" + getDate() + "N-Linhas:" + getNRows() + "N-Colunas:" + getNCols();
    }
    
}//End Spreadsheet Class
