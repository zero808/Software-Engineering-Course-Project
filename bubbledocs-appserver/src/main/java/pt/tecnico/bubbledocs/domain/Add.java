package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes an ADD function.
 * 
 * It's a binary function, therefore it has 2 arguments.
 * 
 * Returns the sum of its arguments.
 * 
 * It's one of the many operations that can be made on a
 * spreadsheet.
 */

public class Add extends Add_Base {
	
	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {Add}
	 */

	public Add() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {Add}
	 * 
	 * @param {Argument} arg1 The function's first argument.
	 * @param {Argument} arg2 The function's second argument.
	 */

	public Add(Argument arg1, Argument arg2) {
		Content a1 = arg1.retrieveContent();
		Content a2 = arg2.retrieveContent();
		a1.setBinary1(this);
		a2.setBinary1(this);
		
		/** @private */ 
		setArg1((Content)arg1);
		/** @private */ 
		setArg2((Content)arg2);
	}
	
	/**
	 * Returns the sum of the arguments given.
	 * 
	 * @throws InvalidArgumentsException
	 * @return {number} The sum of the arguments.
	 */

	@Override
	public int getValue() throws InvalidArgumentsException {
		
		if (getArg1().toString().equals("#VALUE")) 
			throw new InvalidArgumentsException();
		
		if (getArg2().toString().equals("#VALUE")) 
			throw new InvalidArgumentsException();
		
		return super.getArg1().getValue()+super.getArg2().getValue();
	}
	
	/**
	 * Export this function to a XML document.
	 * 
	 * @return {XML Element} The element describing the function.
	 */

	@Override
	public Element exportToXML() {
		Element f = new Element("function");
		Element bf = new Element("binary_function");
		Element add = new Element("add");
		
		add.addContent(super.getArg1().exportToXML());
		add.addContent(super.getArg2().exportToXML());
		
		bf.addContent(add);
		f.addContent(bf);

		return f;
	}
	
	/**
	 * Import this function from a XML document.
	 * 
	 * @param {XML Element} AddElement The element that has the function's data.
	 */
	
	@Override
	public void importFromXML(Element AddElement) {

		int count=1;
		Content a1 = null;
		Content a2 = null;

		for (Element argElement : AddElement.getChildren()) {
			if (argElement.getName().equals("literal")) {
				Literal l = new Literal();
				l.setBinary1(this);
				l.importFromXML(argElement);
				if(count==1) a1=l.retrieveContent();
				if(count==2) a2=l.retrieveContent();
			}
			if (argElement.getName().equals("reference")) {
				Reference r = new Reference();
				r.setBinary1(this);
				r.importFromXML(argElement);
				if(count==1) a1 = r.retrieveContent();
				if(count==2) a2 = r.retrieveContent();
			}
			count++;
		}
		setArg1(a1);
		setArg2(a2);
	}
	
	/**
	 * The string representation of an ADD function.
	 * 
	 * @return {String} The string that represents the function.
	 */
	
	@Override
	public String toString() {
		return "ADD(" + getArg1().toString() + "," + getArg2().toString() + ")";
	}
}// End Add class
