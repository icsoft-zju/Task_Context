//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.wayne.cs.severe.jstereocode.core.analyzer;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MethodAnalyzer {
    private boolean isConstructor = false;
    private boolean isAnonymous = false;
    private boolean hasBody = false;
    private boolean hasStatements = false;
    private boolean usesNonStaticFinalFields = false;
    private boolean overridesClone = false;
    private boolean overridesFinalize = false;
    private boolean overridesToString = false;
    private boolean overridesHashCode = false;
    private boolean overridesEquals = false;
    private Type returnType;
    private ITypeBinding declaringClass;
    private Set<IVariableBinding> getFields = new HashSet();
    private Set<IVariableBinding> propertyFields = new HashSet();
    private Set<IVariableBinding> voidAccessorFields = new HashSet();
    private Set<IVariableBinding> setFields = new HashSet();
    private List<VariableInfo> parameters = new LinkedList();
    private List<VariableInfo> variables = new LinkedList();
    private boolean instantiatedReturn = false;
    private Set<IMethodBinding> invokedLocalMethods = new HashSet();
    private Set<IMethodBinding> invokedExternalMethods = new HashSet();
    private List<TypeInfo> usedTypes = new LinkedList();

    public MethodAnalyzer(MethodDeclaration method) {
        method.accept(new MethodAnalyzer.MethodVisitor(method));
    }

    public boolean isConstructor() {
        return this.isConstructor;
    }

    public boolean isAnonymous() {
        return this.isAnonymous;
    }

    public boolean hasBody() {
        return this.hasBody;
    }

    public boolean hasStatements() {
        return this.hasStatements;
    }

    public boolean usesFields() {
        return this.usesNonStaticFinalFields;
    }

    public boolean overridesClone() {
        return this.overridesClone;
    }

    public boolean overridesFinalize() {
        return this.overridesFinalize;
    }

    public boolean overridesToString() {
        return this.overridesToString;
    }

    public boolean overridesHashCode() {
        return this.overridesHashCode;
    }

    public boolean overridesEquals() {
        return this.overridesEquals;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public ITypeBinding getDeclaringClass() {
        return this.declaringClass;
    }

    public Set<IVariableBinding> getGetFields() {
        return this.getFields;
    }

    public Set<IVariableBinding> getPropertyFields() {
        return this.propertyFields;
    }

    public Set<IVariableBinding> getVoidAccessorFields() {
        return this.voidAccessorFields;
    }

    public Set<IVariableBinding> getSetFields() {
        return this.setFields;
    }

    public List<VariableInfo> getParameters() {
        return this.parameters;
    }

    public List<VariableInfo> getVariables() {
        return this.variables;
    }

    public boolean isInstantiatedReturn() {
        return this.instantiatedReturn;
    }

    public Set<IMethodBinding> getInvokedLocalMethods() {
        return this.invokedLocalMethods;
    }

    public Set<IMethodBinding> getInvokedExternalMethods() {
        return this.invokedExternalMethods;
    }

    public List<TypeInfo> getUsedTypes() {
        return this.usedTypes;
    }

    private void checkNonFinalStaticFieldUse(IVariableBinding field) {
        if (field != null && (!Modifier.isFinal(field.getModifiers()) || !Modifier.isStatic(field.getModifiers()))) {
            this.usesNonStaticFinalFields = true;
        }

    }

    private class MethodVisitor extends ASTVisitor {
        private int inReturn = 0;
        private int inAssignmentRightSide = 0;
        private int inCondition = 0;
        private int inMethodArguments = 0;
        private int assignedVariableIndex = -1;
        private int assignedParameterIndex = -1;
        private static final String TO_STRING_METHOD = ";.toString()Ljava/lang/String;";
        private static final String EQUALS_METHOD = ";.equals(Ljava/lang/Object;)Z";
        private static final String FINALIZE_METHOD = ";.finalize()V";
        private static final String HASH_CODE_METHOD = ";.hashCode()I";
        private static final String CLONE_METHOD = ";.clone()Ljava/lang/Object;";

        public MethodVisitor(MethodDeclaration node) {
            MethodAnalyzer.this.declaringClass = node.resolveBinding().getDeclaringClass();
            MethodAnalyzer.this.isAnonymous = MethodAnalyzer.this.declaringClass.isAnonymous();
            if (node.getBody() != null) {
                MethodAnalyzer.this.hasBody = true;
                if (node.getBody().statements().size() > 0) {
                    MethodAnalyzer.this.hasStatements = true;
                }
            }

            MethodAnalyzer.this.isConstructor = node.isConstructor();
            MethodAnalyzer.this.returnType = node.getReturnType2();
            MethodAnalyzer.this.overridesClone = node.resolveBinding().getKey().contains(";.clone()Ljava/lang/Object;");
            MethodAnalyzer.this.overridesFinalize = node.resolveBinding().getKey().contains(";.finalize()V");
            MethodAnalyzer.this.overridesToString = node.resolveBinding().getKey().contains(";.toString()Ljava/lang/String;");
            MethodAnalyzer.this.overridesEquals = node.resolveBinding().getKey().contains(";.equals(Ljava/lang/Object;)Z");
            MethodAnalyzer.this.overridesHashCode = node.resolveBinding().getKey().contains(";.hashCode()I");
            Iterator var4 = node.parameters().iterator();

            while(var4.hasNext()) {
                Object o = var4.next();
                SingleVariableDeclaration parameter = (SingleVariableDeclaration)o;
                MethodAnalyzer.this.parameters.add(new VariableInfo(parameter.resolveBinding()));
            }

        }

        public boolean visit(VariableDeclarationFragment node) {
            boolean isInstantiated = false;
            if (node.getInitializer() != null) {
                isInstantiated = node.getInitializer() instanceof ClassInstanceCreation;
            }

            MethodAnalyzer.this.variables.add(new VariableInfo(node.resolveBinding(), isInstantiated));
            return super.visit(node);
        }

        public boolean visit(ConditionalExpression node) {
            ++this.inCondition;
            node.getExpression().accept(this);
            --this.inCondition;
            node.getThenExpression().accept(this);
            node.getElseExpression().accept(this);
            return false;
        }

        public boolean visit(ClassInstanceCreation node) {
            if (this.inReturn > 0 && this.inMethodArguments == 0 && this.inCondition == 0 && MethodAnalyzer.this.returnType != null && !MethodAnalyzer.this.returnType.isPrimitiveType()) {
                MethodAnalyzer.this.instantiatedReturn = true;
            }

            return super.visit(node);
        }

        public boolean visit(ReturnStatement node) {
            ++this.inReturn;
            IVariableBinding field = this.getLocalField(node.getExpression());
            if (field != null) {
                MethodAnalyzer.this.getFields.add(field);
            } else {
                int index = this.getVariableIndex(node.getExpression());
                if (index != -1 && ((VariableInfo)MethodAnalyzer.this.variables.get(index)).isInstantiated() && !MethodAnalyzer.this.returnType.isPrimitiveType()) {
                    MethodAnalyzer.this.instantiatedReturn = true;
                    ((VariableInfo)MethodAnalyzer.this.variables.get(index)).setReturned(true);
                } else {
                    IVariableBinding var;
                    Iterator var5;
                    if (index != -1) {
                        if (!((VariableInfo)MethodAnalyzer.this.variables.get(index)).getAssignedFields().isEmpty()) {
                            var5 = ((VariableInfo)MethodAnalyzer.this.variables.get(index)).getAssignedFields().iterator();

                            while(var5.hasNext()) {
                                var = (IVariableBinding)var5.next();
                                MethodAnalyzer.this.propertyFields.add(var);
                            }
                        }

                        ((VariableInfo)MethodAnalyzer.this.variables.get(index)).setReturned(true);
                    } else {
                        index = this.getParameterIndex(node.getExpression());
                        if (index != -1) {
                            var5 = ((VariableInfo)MethodAnalyzer.this.parameters.get(index)).getAssignedFields().iterator();

                            while(var5.hasNext()) {
                                var = (IVariableBinding)var5.next();
                                MethodAnalyzer.this.propertyFields.add(var);
                            }

                            ((VariableInfo)MethodAnalyzer.this.parameters.get(index)).setReturned(true);
                        }
                    }
                }
            }

            return super.visit(node);
        }

        public void endVisit(ReturnStatement node) {
            --this.inReturn;
            super.endVisit(node);
        }

        public boolean visit(Assignment node) {
            IVariableBinding field = this.getLocalField(node.getLeftHandSide());
            if (field != null) {
                MethodAnalyzer.this.setFields.add(field);
            }

            this.assignedVariableIndex = this.getVariableIndex(node.getLeftHandSide());
            if (this.assignedVariableIndex != -1) {
                ((VariableInfo)MethodAnalyzer.this.variables.get(this.assignedVariableIndex)).setModified(true);
            }

            this.assignedParameterIndex = this.getParameterIndex(node.getLeftHandSide());
            if (this.assignedParameterIndex != -1) {
                ((VariableInfo)MethodAnalyzer.this.parameters.get(this.assignedParameterIndex)).setModified(true);
            }

            node.getLeftHandSide().accept(this);
            ++this.inAssignmentRightSide;
            node.getRightHandSide().accept(this);
            --this.inAssignmentRightSide;
            return false;
        }

        public void endVisit(Assignment node) {
            this.assignedVariableIndex = -1;
            this.assignedParameterIndex = -1;
            super.endVisit(node);
        }

        public boolean visit(MethodInvocation node) {
            if (this.inReturn > 0) {
                IVariableBinding field = this.getLocalField(node.getExpression());
                if (field != null) {
                    MethodAnalyzer.this.propertyFields.add(field);
                }
            }

            if (this.isLocalMethod(node)) {
                MethodAnalyzer.this.invokedLocalMethods.add(node.resolveMethodBinding());
            }

            if (node.getExpression() instanceof SimpleName) {
                SimpleName name = (SimpleName)node.getExpression();
                if (name.resolveBinding() instanceof ITypeBinding) {
                    MethodAnalyzer.this.invokedExternalMethods.add(node.resolveMethodBinding());
                }
            }

            if (node.getExpression() != null) {
                node.getExpression().accept(this);
            }

            node.getName().accept(this);
            ++this.inMethodArguments;
            Iterator var3 = node.arguments().iterator();

            while(var3.hasNext()) {
                Object o = var3.next();
                ((Expression)o).accept(this);
            }

            --this.inMethodArguments;
            return false;
        }

        public boolean visit(ExpressionStatement node) {
            if (node.getExpression() instanceof MethodInvocation) {
                MethodInvocation method = (MethodInvocation)node.getExpression();

                Expression expression;
                for(expression = method.getExpression(); expression instanceof MethodInvocation; expression = ((MethodInvocation)expression).getExpression()) {
                }

                IVariableBinding field = this.getLocalField(expression);
                if (field != null) {
                    MethodAnalyzer.this.setFields.add(field);
                }

                int index = this.getVariableIndex(method.getExpression());
                if (index != -1) {
                    ((VariableInfo)MethodAnalyzer.this.variables.get(index)).setModified(true);
                } else {
                    index = this.getParameterIndex(method.getExpression());
                    if (index != -1) {
                        ((VariableInfo)MethodAnalyzer.this.parameters.get(index)).setModified(true);
                    }
                }

                if (this.isLocalMethod(method)) {
                    MethodAnalyzer.this.invokedLocalMethods.add(method.resolveMethodBinding());
                }
            }

            return super.visit(node);
        }

        public boolean visit(SimpleName node) {
            IVariableBinding field = this.getLocalField(node);
            MethodAnalyzer.this.checkNonFinalStaticFieldUse(field);
            if (field != null) {
                if (this.inAssignmentRightSide > 0 && this.assignedVariableIndex != -1) {
                    ((VariableInfo)MethodAnalyzer.this.variables.get(this.assignedVariableIndex)).addAssignedField(field);
                    return super.visit(node);
                }

                if (this.inAssignmentRightSide > 0 && this.assignedParameterIndex != -1 && !((VariableInfo)MethodAnalyzer.this.parameters.get(this.assignedParameterIndex)).getVariableBinding().getType().isPrimitive()) {
                    ((VariableInfo)MethodAnalyzer.this.parameters.get(this.assignedParameterIndex)).addAssignedField(field);
                    MethodAnalyzer.this.voidAccessorFields.add(field);
                    super.visit(node);
                }

                if (this.inReturn > 0 && !(node.getParent() instanceof ReturnStatement) && this.inMethodArguments == 0) {
                    MethodAnalyzer.this.propertyFields.add((IVariableBinding)node.resolveBinding());
                    return super.visit(node);
                }
            }

            if (node.resolveBinding() instanceof ITypeBinding) {
                ITypeBinding typeBinding = (ITypeBinding)node.resolveBinding();
                if (!typeBinding.isPrimitive() && !typeBinding.isParameterizedType()) {
                    this.addUsedType(typeBinding);
                }
            }

            return super.visit(node);
        }

        private void addUsedType(ITypeBinding typeBinding) {
            TypeInfo type = new TypeInfo(typeBinding);
            if (MethodAnalyzer.this.usedTypes.contains(type)) {
                ((TypeInfo)MethodAnalyzer.this.usedTypes.get(MethodAnalyzer.this.usedTypes.indexOf(type))).incrementFrequency();
            } else {
                MethodAnalyzer.this.usedTypes.add(type);
            }

        }

        public boolean visit(FieldAccess node) {
            MethodAnalyzer.this.checkNonFinalStaticFieldUse(this.getLocalField(node));
            return super.visit(node);
        }

        public boolean visit(SuperFieldAccess node) {
            MethodAnalyzer.this.checkNonFinalStaticFieldUse(this.getLocalField(node));
            return super.visit(node);
        }

        public boolean visit(QualifiedName node) {
            MethodAnalyzer.this.checkNonFinalStaticFieldUse(this.getLocalField(node));
            return super.visit(node);
        }

        public boolean visit(ArrayAccess node) {
            MethodAnalyzer.this.checkNonFinalStaticFieldUse(this.getLocalField(node));
            return super.visit(node);
        }

        private IVariableBinding getLocalField(Expression expression) {
            IVariableBinding variableBinding = null;
            if (expression instanceof FieldAccess) {
                variableBinding = ((FieldAccess)expression).resolveFieldBinding();
            }

            if (expression instanceof SimpleName) {
                SimpleName simpleName = (SimpleName)expression;
                if (simpleName.resolveBinding() instanceof IVariableBinding) {
                    variableBinding = (IVariableBinding)simpleName.resolveBinding();
                }
            }

            if (variableBinding != null && variableBinding.isField() && this.isLocalField(variableBinding, MethodAnalyzer.this.declaringClass)) {
                return variableBinding;
            } else {
                IVariableBinding nameBinding;
                if (expression instanceof SuperFieldAccess) {
                    SuperFieldAccess superFieldAccess = (SuperFieldAccess)expression;
                    nameBinding = this.getLocalField(superFieldAccess.getName());
                    return nameBinding != null ? nameBinding : this.getLocalField(superFieldAccess.getQualifier());
                } else if (expression instanceof ArrayAccess) {
                    ArrayAccess arrayAccess = (ArrayAccess)expression;
                    return this.getLocalField(arrayAccess.getArray());
                } else if (expression instanceof QualifiedName) {
                    QualifiedName qualifiedName = (QualifiedName)expression;
                    nameBinding = this.getLocalField(qualifiedName.getName());
                    return nameBinding != null ? nameBinding : this.getLocalField(qualifiedName.getQualifier());
                } else {
                    return null;
                }
            }
        }

        private boolean isLocalField(IVariableBinding field, ITypeBinding relatedType) {
            if (field.getDeclaringClass() != null) {
                if (field.getDeclaringClass().equals(relatedType)) {
                    return true;
                }

                if (relatedType != null && relatedType.isNested() && !Modifier.isStatic(relatedType.getModifiers()) && this.isLocalField(field, relatedType.getDeclaringClass())) {
                    return true;
                }

                if (relatedType.getSuperclass() != null && this.isLocalField(field, relatedType.getSuperclass())) {
                    return true;
                }

                ITypeBinding[] var6;
                int var5 = (var6 = relatedType.getInterfaces()).length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    ITypeBinding interfaceType = var6[var4];
                    if (this.isLocalField(field, interfaceType)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private int getVariableIndex(Expression expression) {
            if (expression instanceof SimpleName) {
                SimpleName simpleName = (SimpleName)expression;
                if (simpleName.resolveBinding() instanceof IVariableBinding) {
                    VariableInfo var = new VariableInfo((IVariableBinding)simpleName.resolveBinding());
                    if (MethodAnalyzer.this.variables.contains(var)) {
                        return MethodAnalyzer.this.variables.indexOf(var);
                    }
                }
            }

            if (expression instanceof QualifiedName) {
                QualifiedName qualifiedName = (QualifiedName)expression;
                int index = this.getVariableIndex(qualifiedName.getName());
                return index != -1 ? index : this.getVariableIndex(qualifiedName.getQualifier());
            } else {
                return -1;
            }
        }

        private int getParameterIndex(Expression expression) {
            if (expression instanceof SimpleName) {
                SimpleName simpleName = (SimpleName)expression;
                if (simpleName.resolveBinding() instanceof IVariableBinding) {
                    VariableInfo var = new VariableInfo((IVariableBinding)simpleName.resolveBinding());
                    if (MethodAnalyzer.this.parameters.contains(var)) {
                        return MethodAnalyzer.this.parameters.indexOf(var);
                    }
                }
            }

            if (expression instanceof QualifiedName) {
                QualifiedName qualifiedName = (QualifiedName)expression;
                int index = this.getParameterIndex(qualifiedName.getName());
                return index != -1 ? index : this.getParameterIndex(qualifiedName.getQualifier());
            } else {
                return -1;
            }
        }

        private boolean isLocalMethod(MethodInvocation method) {
            IMethodBinding methodBinding = method.resolveMethodBinding();
            if (methodBinding != null && methodBinding.getDeclaringClass() != null) {
                if (methodBinding.getDeclaringClass().equals(MethodAnalyzer.this.declaringClass)) {
                    return true;
                }

                ITypeBinding relatedClass = MethodAnalyzer.this.declaringClass;

                while(relatedClass.isNested() && !Modifier.isStatic(relatedClass.getModifiers())) {
                    relatedClass = relatedClass.getDeclaringClass();
                    if (relatedClass == null) {
                        break;
                    }

                    if (methodBinding.getDeclaringClass().equals(relatedClass)) {
                        return true;
                    }
                }

                for(relatedClass = MethodAnalyzer.this.declaringClass.getSuperclass(); relatedClass != null; relatedClass = relatedClass.getSuperclass()) {
                    if (methodBinding.getDeclaringClass().equals(relatedClass)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
