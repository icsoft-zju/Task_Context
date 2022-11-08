package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclaration;

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
