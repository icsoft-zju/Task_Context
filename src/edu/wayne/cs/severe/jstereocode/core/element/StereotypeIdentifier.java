package edu.wayne.cs.severe.jstereocode.core.element;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.ui.actions.NewWizardAction;

import edu.wayne.cs.severe.jstereocode.core.analyzer.JParser;
import edu.wayne.cs.severe.jstereocode.core.taxonomy.CodeStereotype;


public class StereotypeIdentifier {
    private static final String TypeDeclaration = null;
	private JParser parser;
    private List<StereotypedElement> stereotypedElements;
    double methodsMean;
    double methodsStdDev;

    public StereotypeIdentifier() {
        this.stereotypedElements = new LinkedList<StereotypedElement>();
    }

    public void setParameters(ICompilationUnit unit, double methodsMean, double methodsStdDev) {
        this.parser = new JParser(unit);
        this.methodsMean = methodsMean;
        this.methodsStdDev = methodsStdDev;
    }
    public void setParameters(IMember member, double methodsMean, double methodsStdDev) {
        this.parser = new JParser(member);
        this.methodsMean = methodsMean;
        this.methodsStdDev = methodsStdDev;
    }

	public void identifyStereotypes() {
        if (this.parser != null) {
            this.parser.parse();
            Iterator<?> var2 = this.parser.getElements().iterator();
            while(var2.hasNext()) {
                ASTNode element = (ASTNode)var2.next();
                try {
                    Object stereoElement;
                    if (element instanceof TypeDeclaration) {
                        stereoElement = new StereotypedType((TypeDeclaration)element, this.methodsMean, this.methodsStdDev);
                    } else {
                        if (!(element instanceof MethodDeclaration)) {
                            continue;
                        }
                        stereoElement = new StereotypedMethod((MethodDeclaration)element);
                    }
                    ((StereotypedElement)stereoElement).findStereotypes();
                    this.stereotypedElements.add((StereotypedElement) stereoElement);
                } catch (NullPointerException var4) {
                }
            }
        }
    }

    public List<StereotypedElement> getStereotypedElements() {
        return this.stereotypedElements;
    }

    public JParser getParser() {
        return this.parser;
    }
//
//    public String findMethodStereotype(String qualifiedMethodName) {
//        return this.findMethodStereotype(qualifiedMethodName, this.stereotypedElements);
//    }
//
//    private String findMethodStereotype(String qualifiedMethodName, List<StereotypedElement> elements) {
//        Iterator var4 = elements.iterator();
//
//        while(var4.hasNext()) {
//            StereotypedElement elem = (StereotypedElement)var4.next();
//            if (elem instanceof StereotypedType) {
//                return this.findMethodStereotype(qualifiedMethodName, elem.getStereoSubElements());
//            }
//
//            if (elem instanceof StereotypedMethod) {
//                String qName = ((StereotypedMethod)elem).getQualifiedName();
//                if (qName.equals(qualifiedMethodName)) {
//                    StringBuilder stereoString = new StringBuilder();
//                    Iterator var8 = elem.getStereotypes().iterator();
//
//                    while(var8.hasNext()) {
//                        CodeStereotype stereo = (CodeStereotype)var8.next();
//                        stereoString.append(stereo + " ");
//                    }
//
//                    return stereoString.toString().trim();
//                }
//            }
//        }
//
//        return null;
//    }
}
