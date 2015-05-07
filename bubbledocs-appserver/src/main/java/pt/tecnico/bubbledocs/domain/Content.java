package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

/**
 * Class that abstracts all types of content.
 * 
 * All types of content must extend this class.
 */

public abstract class Content extends Content_Base {
    
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Content}
	 */
	
    public Content() {
        super();
    }
    
    /**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Content}
	 * 
	 * @param {Argument} c The cell to which the content belongs to.
	 */
    
    public Content(Cell c){
    	super();
    	/** @private */ 
    	super.setCell(c);
    }
    
    /**
	 * Method that deletes a specific content.
	 * 
	 * To delete a specific content, given the architecture of
	 * the application, first its required to sever all the
	 * connections the content has. 
	 * More specifically, to the cell which has this content.
	 * After doing that, then the object is deleted.
	 */
    
    public void delete() {
    	setCell(null);
    	deleteDomainObject();
    }
    
    /**
	 * Abstract method that returns the specific
	 * value of each type of content.
	 * 
	 * @return {number} The value of the content.
	 */

    public abstract int getValue();
    
    /**
	 * Abstract method that exports a specific content
	 * to a XML document.
	 * 
	 * @return {XML Element} The element describing the content.
	 */

	public abstract Element exportToXML();
	
	/**
	 * Abstract method that imports a specific content
	 * from a XML document.
	 * 
	 * @param {XML Element} cellElement The element that has the
	 * contents data.
	 */
	
	public abstract void importFromXML(Element cellElement);
}// End Content class
