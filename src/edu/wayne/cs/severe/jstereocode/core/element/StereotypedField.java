package edu.wayne.cs.severe.jstereocode.core.element;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import edu.wayne.cs.severe.jstereocode.core.taxonomy.CodeStereotype;
import edu.wayne.cs.severe.jstereocode.core.taxonomy.FieldStereoType;

public class StereotypedField implements StereotypedElement{
	private VariableDeclaration field;
	private FieldStereoType primaryStereotype;
	public StereotypedField(VariableDeclaration field) {
        this.field = field;
    }
	
	@Override
	public ASTNode getElement() {
		// TODO Auto-generated method stub
		return this.field;
	}

	@Override
	public List<CodeStereotype> getStereotypes() {
		// TODO Auto-generated method stub
		ArrayList<CodeStereotype> stereotypes = new ArrayList();
		stereotypes.add(primaryStereotype);
		return stereotypes;
	}
	
	public String getStereotypesName() {
		return primaryStereotype.toString();
	}
	
	@Override
	public List<StereotypedElement> getStereoSubElements() {
		// TODO Auto-generated method stub
		return new ArrayList();
	}

	@Override
	public void findStereotypes() {
		// TODO Auto-generated method stub
		this.primaryStereotype = FieldStereoType.FIELD;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return field.getName().toString();
	}

	@Override
	public List<StereotypedElement> getStereoSubFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
