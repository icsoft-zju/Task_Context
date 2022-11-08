package edu.zju.icsoft.taskcontext.util.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.zju.icsoft.taskcontext.util.graph.Relationship;
import edu.zju.icsoft.taskcontext.util.jstereocode.JParser;
import edu.zju.icsoft.taskcontext.util.jstereocode.StereotypedElement;






public class Analyzer {
    private JParser parser;
    private List<StereotypedElement> stereotypedElements;
    private ICompilationUnit iUnit;

    public Analyzer(JParser parser, List<StereotypedElement> types) {
        this.parser = parser;
        this.stereotypedElements = types;
    }

    public void update(HashMap<String, String> map, ArrayList<Relationship> relatemap) {
        this.iUnit = (ICompilationUnit)this.parser.getCompilationUnit().getJavaElement();
        Iterator var3 = this.stereotypedElements.iterator();
        
        while(var3.hasNext()) {
            StereotypedElement stereoElement = (StereotypedElement)var3.next();
            this.process(stereoElement,map,relatemap);
        }
    }

    private void process(StereotypedElement stereoElement,HashMap<String, String> map, ArrayList<Relationship> relatemap) {
    	ASTNode parent = stereoElement.getElement();

        if(parent instanceof TypeDeclaration) {
        	
        	TypeDeclaration type = (TypeDeclaration)parent;

    		map.put(type.resolveBinding().getJavaElement().getHandleIdentifier(),stereoElement.getStereotypesName());
    		IMember member = (IMember)((TypeDeclaration)parent).resolveBinding().getJavaElement();
    		
    		try {
				if((type).isInterface()) {
					//interface inherit
					ITypeBinding[] superinter = type.resolveBinding().getInterfaces();
					for(ITypeBinding interBinding : superinter) {
						Relationship relate = new Relationship(member, (IMember)(interBinding.getJavaElement()), "inherit");
						relatemap.add(relate);
		            }
				}
				else {
					//class inherit
					if(((IType)member).getSuperclassName() != null && type.resolveBinding().getSuperclass()!=null){
						IJavaElement extendtype = type.resolveBinding().getSuperclass().getJavaElement();
						Relationship relate = new Relationship(member, (IMember)extendtype, "inherit");
						relatemap.add(relate);
					}
					//class implement
		            ITypeBinding[] interfaces = type.resolveBinding().getInterfaces();
		            for(ITypeBinding interBinding : interfaces) {
		            	Relationship relate = new Relationship(member, (IMember)(interBinding.getJavaElement()), "implement");
						relatemap.add(relate);
		            }
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
    		
    		
    	}
        else if(parent instanceof MethodDeclaration) {
        	
        	MethodDeclaration meth = (MethodDeclaration)parent;
    		if(meth != null) {
    			map.put(meth.resolveBinding().getJavaElement().getHandleIdentifier(),stereoElement.getStereotypesName());
        		IMember member = (IMember)meth.resolveBinding().getJavaElement();
    			Block methodBody = ((MethodDeclaration)meth).getBody();
    			if(methodBody != null) {
    				MethodVisitor visitor = new MethodVisitor();
    				methodBody.accept(visitor);
    				ArrayList<IMethod> methods = visitor.getMethods();
    				for(IMethod method : methods) {
    					if(method==null || method.getCompilationUnit() == null) continue;
    					Relationship ship = new Relationship(member, (IMember)method, "call");
    					relatemap.add(ship);
    				}
    			}
    		}
    		
    	}
        
        Iterator var4 = stereoElement.getStereoSubElements().iterator();
        while(var4.hasNext()) {
            StereotypedElement stereoSubElement = (StereotypedElement)var4.next();
            this.process(stereoSubElement,map,relatemap);
        }

    }

}

