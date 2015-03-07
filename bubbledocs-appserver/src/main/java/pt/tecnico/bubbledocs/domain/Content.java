package pt.tecnico.bubbledocs.domain;

public abstract class Content extends Content_Base {
    
    public Content() {
        super();
    }
    
    public Content(Cell c){
    	super();
    	super.setCell(c);
    }
    
    public abstract int getValue();
}