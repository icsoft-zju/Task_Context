package edu.wayne.cs.severe.jstereocode.core.element;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Javadoc;

import edu.wayne.cs.severe.jstereocode.core.taxonomy.CodeStereotype;

public interface StereotypedElement {
    ASTNode getElement();

    List<CodeStereotype> getStereotypes();

    List<StereotypedElement> getStereoSubElements();
    
    List<StereotypedElement> getStereoSubFields();

    String getStereotypesName();
    
    void findStereotypes();

    String getName();

}