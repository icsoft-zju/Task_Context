//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.wayne.cs.severe.jstereocode.core.taxonomy;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wayne.cs.severe.jstereocode.core.element.StereotypedMethod;

public class TypeStereotypeRules {
    protected TypeDeclaration type;
    protected Set<StereotypedMethod> totalMethods;
    protected Set<StereotypedMethod> getMethods;
    protected Set<StereotypedMethod> predicateMethods;
    protected Set<StereotypedMethod> propertyMethods;
    protected Set<StereotypedMethod> voidAccessorMethods;
    protected Set<StereotypedMethod> setMethods;
    protected Set<StereotypedMethod> commandMethods;
    protected Set<StereotypedMethod> nonVoidCommandMethods;
    protected Set<StereotypedMethod> factoryMethods;
    protected Set<StereotypedMethod> localControllerMethods;
    protected Set<StereotypedMethod> collaboratorMethods;
    protected Set<StereotypedMethod> controllerMethods;
    protected Set<StereotypedMethod> incidentalMethods;
    protected Set<StereotypedMethod> emptyMethods;
    protected Set<StereotypedMethod> abstractMethods;
    protected Set<StereotypedMethod> accessorMethods;
    protected Set<StereotypedMethod> mutatorMethods;
    protected Set<StereotypedMethod> collaborationalMethods;
    protected Set<StereotypedMethod> degenerateMethods;
    protected double methodsMean;
    protected double methodsStdDev;

    protected TypeStereotypeRules() {
        this.totalMethods = new HashSet();
        this.getMethods = new HashSet();
        this.predicateMethods = new HashSet();
        this.propertyMethods = new HashSet();
        this.voidAccessorMethods = new HashSet();
        this.commandMethods = new HashSet();
        this.setMethods = new HashSet();
        this.nonVoidCommandMethods = new HashSet();
        this.factoryMethods = new HashSet();
        this.localControllerMethods = new HashSet();
        this.collaboratorMethods = new HashSet();
        this.controllerMethods = new HashSet();
        this.incidentalMethods = new HashSet();
        this.emptyMethods = new HashSet();
        this.abstractMethods = new HashSet();
        this.accessorMethods = new HashSet();
        this.mutatorMethods = new HashSet();
        this.collaborationalMethods = new HashSet();
        this.degenerateMethods = new HashSet();
    }

    public TypeStereotypeRules(TypeDeclaration type, double methodsMean, double methodsStdDev) throws NullPointerException {
        this();
        if (type == null) {
            throw new NullPointerException("The type can't be null");
        } else {
            this.type = type;
            this.methodsMean = methodsMean;
            this.methodsStdDev = methodsStdDev;
        }
    }

    protected boolean checkForInterface() {
        return this.type.isInterface();
    }

    protected boolean checkForEntity() {
        boolean rule1 = !this.accessorMethods.isEmpty() && !this.mutatorMethods.isEmpty();
        boolean rule2 = 0.4D * (double)this.totalMethods.size() <= (double)this.collaborationalMethods.size() && (double)this.collaborationalMethods.size() <= 0.6D * (double)this.totalMethods.size();
        boolean rule3 = this.controllerMethods.isEmpty();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForMinimalEntity() {
        HashSet<StereotypedMethod> temp = new HashSet(this.totalMethods);
        temp.removeAll(this.accessorMethods);
        temp.removeAll(this.mutatorMethods);
        temp.removeAll(this.localControllerMethods);
        boolean rule1 = temp.isEmpty();
        boolean rule2 = !this.accessorMethods.isEmpty() && !this.mutatorMethods.isEmpty();
        boolean rule3 = this.controllerMethods.isEmpty() && this.factoryMethods.isEmpty();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForDataProvider() {
        boolean rule1 = (double)this.accessorMethods.size() > 2.0D * (double)this.mutatorMethods.size();
        Set<StereotypedMethod> temp = this.controllerMethods;
        temp.addAll(this.factoryMethods);
        boolean rule2 = (double)this.accessorMethods.size() > 2.0D * (double)temp.size();
        return rule1 && rule2;
    }

    protected boolean checkForCommander() {
        boolean rule1 = (double)this.mutatorMethods.size() > 2.0D * (double)this.accessorMethods.size();
        Set<StereotypedMethod> temp = this.controllerMethods;
        temp.addAll(this.factoryMethods);
        boolean rule2 = (double)this.mutatorMethods.size() > 2.0D * (double)temp.size();
        return rule1 && rule2;
    }

    protected boolean checkForBoundary() {
        boolean rule1 = this.collaboratorMethods.size() >= this.totalMethods.size() - this.collaboratorMethods.size();
        boolean rule2 = (double)this.factoryMethods.size() <= 0.5D * (double)this.totalMethods.size();
        boolean rule3 = (double)this.controllerMethods.size() <= 0.3333333333333333D * (double)this.totalMethods.size();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForFactory() {
        boolean rule1 = (double)this.factoryMethods.size() >= 0.6666666666666666D * (double)this.totalMethods.size();
        return rule1;
    }

    protected boolean checkForController() {
        HashSet<StereotypedMethod> temp = new HashSet(this.controllerMethods);
        temp.addAll(this.factoryMethods);
        boolean rule1 = (double)temp.size() >= 0.6666666666666666D * (double)this.totalMethods.size();
        boolean rule2 = !this.controllerMethods.isEmpty() || !this.factoryMethods.isEmpty();
        boolean rule3 = !this.mutatorMethods.isEmpty() || !this.accessorMethods.isEmpty();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForPureController() {
        HashSet<StereotypedMethod> temp = new HashSet(this.totalMethods);
        temp.removeAll(this.controllerMethods);
        temp.removeAll(this.factoryMethods);
        temp.removeAll(this.localControllerMethods);
        boolean rule1 = temp.isEmpty();
        boolean rule2 = this.mutatorMethods.isEmpty() && this.accessorMethods.isEmpty();
        boolean rule3 = !this.controllerMethods.isEmpty();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForLargeClass() {
        boolean rule1 = 0.2D * (double)this.totalMethods.size() <= (double)(this.accessorMethods.size() + this.mutatorMethods.size()) && (double)(this.accessorMethods.size() + this.mutatorMethods.size()) <= 0.6666666666666666D * (double)this.totalMethods.size();
        HashSet<StereotypedMethod> temp = new HashSet(this.controllerMethods);
        temp.addAll(this.factoryMethods);
        boolean rule2 = 0.2D * (double)this.totalMethods.size() <= (double)temp.size() && (double)temp.size() <= 0.6666666666666666D * (double)this.totalMethods.size();
        boolean rule3 = !this.controllerMethods.isEmpty();
        boolean rule4 = !this.factoryMethods.isEmpty();
        boolean rule5 = !this.accessorMethods.isEmpty();
        boolean rule6 = !this.mutatorMethods.isEmpty();
        boolean rule7 = (double)this.totalMethods.size() > this.methodsMean + this.methodsStdDev;
        return rule1 && rule2 && rule3 && rule4 && rule5 && rule6 && rule7;
    }

    protected boolean checkForLazyClass() {
        boolean rule1 = (double)this.incidentalMethods.size() >= 0.3333333333333333D * (double)this.totalMethods.size();
        boolean rule2 = (double)(this.getMethods.size() + this.setMethods.size() + this.incidentalMethods.size()) >= 0.8D * (double)this.totalMethods.size();
        return rule1 && rule2;
    }

    protected boolean checkForDegenerate() {
        boolean rule1 = (double)this.emptyMethods.size() >= 0.3333333333333333D * (double)this.totalMethods.size();
        boolean rule2 = (double)(this.getMethods.size() + this.setMethods.size() + this.emptyMethods.size()) >= 0.8D * (double)this.totalMethods.size();
        return rule1 && rule2;
    }

    protected boolean checkForDataClass() {
        boolean rule1 = (double)(this.getMethods.size() + this.setMethods.size()) > 0.0D;
        boolean rule2 = this.totalMethods.size() == this.getMethods.size() + this.setMethods.size();
        boolean rule3 = this.collaborationalMethods.isEmpty();
        return rule1 && rule2 && rule3;
    }

    protected boolean checkForPool() {
        double staticFinalFields = 0.0D;
        FieldDeclaration[] var6;
        int var5 = (var6 = this.type.getFields()).length;

        for(int var4 = 0; var4 < var5; ++var4) {
            FieldDeclaration field = var6[var4];
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                ++staticFinalFields;
            }
        }

        boolean rule1 = staticFinalFields >= 0.6666666666666666D * staticFinalFields && this.totalMethods.size() < 2;
        boolean rule2 = staticFinalFields > 2.0D;
        return rule1 && rule2;
    }
}

