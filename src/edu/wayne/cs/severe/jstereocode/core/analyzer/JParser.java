package edu.wayne.cs.severe.jstereocode.core.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

public class JParser {
    private CompilationUnit unit;
    private List<ASTNode> elements;
    private IMember member;
    public JParser(ICompilationUnit unit){
        ASTParser parser = ASTParser.newParser(AST.JLS16);
        parser.setKind(8);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        this.unit = (CompilationUnit)parser.createAST((IProgressMonitor)null);
        this.elements = new ArrayList();
    }

    public JParser(IMember member) {
        this(member.getCompilationUnit());
        this.member = member;
        
    }

    public void parse() {
        if (this.member != null) {
            if (this.member instanceof IType) {
                this.elements.add(this.unit.findDeclaringNode(((IType)this.member).getKey()));
            } else if (this.member instanceof IMethod) {
                ASTParser parser = ASTParser.newParser(AST.JLS16);
                parser.setProject(((IMethod)this.member).getJavaProject());
                parser.setResolveBindings(true);
                IBinding binding = parser.createBindings(new IJavaElement[]{(IMethod)this.member}, (IProgressMonitor)null)[0];
                if (binding instanceof IMethodBinding) {
                    ASTNode method = this.unit.findDeclaringNode(((IMethodBinding)binding).getKey());
                    this.elements.add(method);
                }
            }
            else {
                ASTParser parser = ASTParser.newParser(AST.JLS16);
                parser.setProject(((IField)this.member).getJavaProject());
                parser.setResolveBindings(true);
                IBinding binding = parser.createBindings(new IJavaElement[]{(IField)this.member}, (IProgressMonitor)null)[0];
                ASTNode field = this.unit.findDeclaringNode(binding.getKey());
                this.elements.add(field);
            }
        } else {
            Iterator var5 = this.unit.types().iterator();
            while(var5.hasNext()) {
                Object o = var5.next();
                if (o instanceof TypeDeclaration) {
                    this.elements.add((TypeDeclaration)o);
                }
            }
        }
    }

    public List<ASTNode> getElements() {
        return this.elements;
    }

    public CompilationUnit getCompilationUnit() {
        return this.unit;
    }

    

}
