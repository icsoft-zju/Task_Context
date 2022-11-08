
package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


public class StereotypedType extends TypeStereotypeRules implements StereotypedElement{
    private Set<IVariableBinding> fields = new HashSet();
    private TypeStereotype primaryStereotype;
    private TypeStereotype secondaryStereotype;
    private List<StereotypedMethod> stereotypedMethods = new ArrayList();
    private List<StereotypedType> stereotypedSubTypes = new ArrayList();
    private List<StereotypedField> stereotypedSubField = new ArrayList();
    private List<TypeInfo> relatedTypes = new LinkedList();

    public StereotypedType(TypeDeclaration type, double methodsMean, double methodsStdDev) throws NullPointerException {
        super(type, methodsMean, methodsStdDev);
        try {
            type.resolveBinding().getBinaryName();
        } catch (NullPointerException var7) {
            throw new NullPointerException("No type name found");
        }
    }

    public TypeDeclaration getElement() {
        return this.type;
    }

    public List<StereotypedElement> getStereoSubElements() {
        List<StereotypedElement> elements = new ArrayList(this.stereotypedMethods);
        elements.addAll(this.stereotypedSubTypes);
        return elements;
    }

    public List<StereotypedElement> getStereoSubFields() {
        List<StereotypedElement> elements = new ArrayList();
        elements.addAll(this.stereotypedSubField);
        return elements;
    }
    
    public List<CodeStereotype> getStereotypes() {
        ArrayList<CodeStereotype> stereotypes = new ArrayList();
        if (this.primaryStereotype != null) {
            stereotypes.add(this.primaryStereotype);
        }

        if (this.secondaryStereotype != null) {
            stereotypes.add(this.secondaryStereotype);
        }
        return stereotypes;
    }
    
    public String getStereotypesName() {
    	if(this.secondaryStereotype != null) {
    		return this.primaryStereotype.toString()+"-"+this.secondaryStereotype.toString();
    	}
    	if(this.primaryStereotype != null)
		return primaryStereotype.toString();
    	else return "NULL";
	}

    public void findStereotypes() {
        FieldDeclaration[] var4;
        int var3 = (var4 = this.type.getFields()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            FieldDeclaration field = var4[var2];
            Iterator var6 = field.fragments().iterator();
            while(var6.hasNext()) {
                Object o = var6.next();
                VariableDeclarationFragment fragment = (VariableDeclarationFragment)o;
                this.fields.add(fragment.resolveBinding());
            }
        }
      
        this.findMethodsStereotypes();
        this.findTypeStereotypes();
        this.findSubtypesStereotypes();
    }

    private void findSubtypesStereotypes() {
        TypeDeclaration[] var4;
        int var3 = (var4 = this.type.getTypes()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            TypeDeclaration subtype = var4[var2];
            if (subtype.resolveBinding().isClass()) {
                StereotypedType subStereotypedType = new StereotypedType(subtype, this.methodsMean, this.methodsStdDev);
                subStereotypedType.findStereotypes();
                this.stereotypedSubTypes.add(subStereotypedType);
            }
        }

    }
    
    private void setStereotype(TypeStereotype stereotype) {
        if (this.primaryStereotype == null) {
            this.primaryStereotype = stereotype;
        } else {
            this.secondaryStereotype = stereotype;
        }

    }

    private void findTypeStereotypes() {
        if (this.checkForInterface()) {
            this.setStereotype(TypeStereotype.INTERFACE);
        } else if (this.checkForPool()) {
            this.setStereotype(TypeStereotype.POOL);
        } else if (this.checkForDegenerate()) {
            this.setStereotype(TypeStereotype.DEGENERATE);
        } else if (this.checkForLazyClass()) {
            this.setStereotype(TypeStereotype.LAZY_CLASS);
        } else if (this.checkForLargeClass()) {
            this.setStereotype(TypeStereotype.LARGE_CLASS);
        } else if (this.checkForDataClass()) {
            this.setStereotype(TypeStereotype.DATA_CLASS);
        } else if (this.checkForMinimalEntity()) {
            this.setStereotype(TypeStereotype.MINIMAL_ENTITY);
        } else if (this.checkForEntity()) {
            this.setStereotype(TypeStereotype.ENTITY);
        } else if (this.checkForFactory()) {
            this.setStereotype(TypeStereotype.FACTORY);
        } else if (this.checkForController()) {
            this.setStereotype(TypeStereotype.CONTROLLER);
        } else if (this.checkForPureController()) {
            this.setStereotype(TypeStereotype.PURE_CONTROLLER);
        } else {
            if (this.checkForBoundary()) {
                this.setStereotype(TypeStereotype.BOUNDARY);
            }

            if (this.checkForDataProvider()) {
                this.setStereotype(TypeStereotype.DATA_PROVIDER);
            } else if (this.checkForCommander()) {
                this.setStereotype(TypeStereotype.COMMANDER);
            }
        }
    }

    private void findMethodsStereotypes() {
        TypeAnalyzer analyzer = new TypeAnalyzer(this.type);
        this.stereotypedMethods = analyzer.getStereotypedMethods();
        Iterator var3 = this.stereotypedMethods.iterator();

        while(var3.hasNext()) {
            StereotypedMethod stereotypedMethod = (StereotypedMethod)var3.next();
            if (!stereotypedMethod.overridesObjectMethod() && !stereotypedMethod.isConstructor() && !stereotypedMethod.isAnonymous()) {
                this.addRelatedTypes(stereotypedMethod.getUsedTypes());
                this.addMethodToSet(stereotypedMethod);
                this.totalMethods.add(stereotypedMethod);
            }
        }
    }

    private void addRelatedTypes(List<TypeInfo> types) {
        Iterator var3 = types.iterator();

        while(var3.hasNext()) {
            TypeInfo type = (TypeInfo)var3.next();
            if (this.relatedTypes.contains(type)) {
                ((TypeInfo)this.relatedTypes.get(this.relatedTypes.indexOf(type))).incrementFrequencyBy(type.getFrequency());
            } else if (type.getTypeBinding().getPackage() != null &&!type.getTypeBinding().getPackage().getName().equals("java.lang")) {
                this.relatedTypes.add(type);
            }
        }

    }

    private void addMethodToSet(StereotypedMethod stereotypedMethod) {
        if (stereotypedMethod.isAccessor()) {
            this.accessorMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isMutator()) {
            this.mutatorMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isCollaborational()) {
            this.collaborationalMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isDegenerate()) {
            this.degenerateMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isGet()) {
            this.getMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isPredicate()) {
            this.predicateMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isProperty()) {
            this.propertyMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isVoidAccessor()) {
            this.voidAccessorMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isSet()) {
            this.setMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isCommand()) {
            this.commandMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isNonVoidCommand()) {
            this.nonVoidCommandMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isFactory()) {
            this.factoryMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isCollaborator()) {
            this.collaboratorMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isLocalController()) {
            this.localControllerMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isController()) {
            this.controllerMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isIncidental()) {
            this.incidentalMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isEmpty()) {
            this.emptyMethods.add(stereotypedMethod);
        }

        if (stereotypedMethod.isAbstract()) {
            this.abstractMethods.add(stereotypedMethod);
        }

    }

    public List<StereotypedMethod> getStereotypedMethods() {
        return this.stereotypedMethods;
    }

    public List<StereotypedType> getStereotypedSubTypes() {
        return this.stereotypedSubTypes;
    }

    private boolean typeIs(TypeStereotype stereotype) {
        boolean result = false;
        if (this.primaryStereotype != null) {
            result = this.primaryStereotype.equals(stereotype);
        }

        if (this.secondaryStereotype != null) {
            result = result || this.secondaryStereotype.equals(stereotype);
        }

        return result;
    }

    public boolean isInterface() {
        return this.typeIs(TypeStereotype.INTERFACE);
    }

    public boolean isEntity() {
        return this.typeIs(TypeStereotype.ENTITY);
    }

    public boolean isMinimalEntity() {
        return this.typeIs(TypeStereotype.MINIMAL_ENTITY);
    }

    public boolean isDataProvider() {
        return this.typeIs(TypeStereotype.DATA_PROVIDER);
    }

    public boolean isCommander() {
        return this.typeIs(TypeStereotype.COMMANDER);
    }

    public boolean isBoundary() {
        return this.typeIs(TypeStereotype.BOUNDARY);
    }

    public boolean isFactory() {
        return this.typeIs(TypeStereotype.FACTORY);
    }

    public boolean isController() {
        return this.typeIs(TypeStereotype.CONTROLLER);
    }

    public boolean isPureController() {
        return this.typeIs(TypeStereotype.PURE_CONTROLLER);
    }

    public boolean isLargeClass() {
        return this.typeIs(TypeStereotype.LARGE_CLASS);
    }

    public boolean isLazyClass() {
        return this.typeIs(TypeStereotype.LAZY_CLASS);
    }

    public boolean isDegenerate() {
        return this.typeIs(TypeStereotype.DEGENERATE);
    }

    public boolean isDataClass() {
        return this.typeIs(TypeStereotype.DATA_CLASS);
    }

    public boolean isPool() {
        return this.typeIs(TypeStereotype.POOL);
    }

  

    public List<TypeInfo> getRelatedTypes() {
        return this.relatedTypes;
    }

    public Set<IVariableBinding> getFields() {
        return this.fields;
    }

    
    public String getName() {
        return this.type != null && this.type.resolveBinding() != null ? this.type.resolveBinding().getName() : "";
    }

//    public String getQualifiedName() {
//        return this.type != null && this.type.resolveBinding() != null ? this.type.resolveBinding().getQualifiedName() : "";
//    }

}
