package pt.tecnico.bubbledocs.domain;

public class Literal extends Literal_Base {
    
    public Literal() {
        super();
    }
    
    public Literal(int value) {
    	super();
    	super.setVal(value);
    }
    
    @Override
    public String toString() {
    	return "Literal value: " + super.getVal();
    }

	@Override
	public int getValue() {
		return super.getVal();
	}
    
}