package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public interface StereotypedElement {
    ASTNode getElement();

    List<CodeStereotype> getStereotypes();

    List<StereotypedElement> getStereoSubElements();
    
    List<StereotypedElement> getStereoSubFields();

    String getStereotypesName();
    
    void findStereotypes();

    String getName();

}