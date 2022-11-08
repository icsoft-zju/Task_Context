//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class VariableInfo {
    private IVariableBinding variableBinding;
    private Set<IVariableBinding> assignedFields;
    private boolean isInstantiated;
    private boolean isReturned;
    private boolean isModified;

    public VariableInfo(IVariableBinding name) {
        this.variableBinding = name;
        this.isInstantiated = false;
        this.isReturned = false;
        this.isModified = false;
        this.assignedFields = new HashSet();
    }

    public VariableInfo(IVariableBinding name, boolean isInstantiated) {
        this.variableBinding = name;
        this.isInstantiated = isInstantiated;
        this.isReturned = false;
        this.isModified = false;
        this.assignedFields = new HashSet();
    }

    public IVariableBinding getVariableBinding() {
        return this.variableBinding;
    }

    public Set<IVariableBinding> getAssignedFields() {
        return this.assignedFields;
    }

    public void addAssignedField(IVariableBinding field) {
        this.assignedFields.add(field);
    }

    public void setInstantiated(boolean isInstantiated) {
        this.isInstantiated = isInstantiated;
    }

    public void setReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public boolean isInstantiated() {
        return this.isInstantiated;
    }

    public boolean isReturned() {
        return this.isReturned;
    }

    public boolean isModified() {
        return this.isModified;
    }

    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + (this.variableBinding == null ? 0 : this.variableBinding.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            VariableInfo other = (VariableInfo)obj;
            if (this.variableBinding == null) {
                if (other.variableBinding != null) {
                    return false;
                }
            } else if (!this.variableBinding.equals(other.variableBinding)) {
                return false;
            }

            return true;
        }
    }
}

