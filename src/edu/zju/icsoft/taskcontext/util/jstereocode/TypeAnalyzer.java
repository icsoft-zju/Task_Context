//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeAnalyzer {
    private List<StereotypedMethod> stereotypedMethods = new LinkedList();

    public TypeAnalyzer(TypeDeclaration type) {
        TypeAnalyzer.TypeVisitor visitor = new TypeAnalyzer.TypeVisitor();
        type.accept(visitor);
    }

    public List<StereotypedMethod> getStereotypedMethods() {
        return this.stereotypedMethods;
    }

    
    private class TypeVisitor extends ASTVisitor {
        private boolean isRoot;

        private TypeVisitor() {
            this.isRoot = true;
        }

        public boolean visit(MethodDeclaration node) {
            StereotypedMethod stereotypedMethod = new StereotypedMethod(node);
            stereotypedMethod.findStereotypes();
            TypeAnalyzer.this.stereotypedMethods.add(stereotypedMethod);
            return super.visit(node);
        }

        public boolean visit(TypeDeclaration node) {
            if (this.isRoot) {
                this.isRoot = false;
                return super.visit(node);
            } else {
                return false;
            }
        }
    }
}
