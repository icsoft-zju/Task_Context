package edu.zju.icsoft.taskcontext.visitor;


import java.util.ArrayList;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
	 
public class MethodVisitor extends ASTVisitor{
		private ArrayList<IMethod> methods = new ArrayList<IMethod>();
		
		@Override
		public boolean visit(MethodInvocation  node) {
			IMethodBinding binding = (IMethodBinding) node.getName().resolveBinding();
			if(binding!=null) {
				IMethod method = (IMethod) binding.getJavaElement();
				methods.add(method);
			}
			return true;
		}
	 
		public ArrayList<IMethod> getMethods() {
			return methods;
		}

	}

