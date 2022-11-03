//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.wayne.cs.severe.jstereocode.core.analyzer;

import java.util.Comparator;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeInfo {
    private ITypeBinding typeBinding;
    private int frequency;

    public TypeInfo(ITypeBinding type) {
        this.typeBinding = type;
        this.frequency = 1;
    }

    public ITypeBinding getTypeBinding() {
        return this.typeBinding;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void incrementFrequency() {
        ++this.frequency;
    }

    public void incrementFrequencyBy(int x) {
        this.frequency += x;
    }

    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + (this.typeBinding == null ? 0 : this.typeBinding.hashCode());
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
            TypeInfo other = (TypeInfo)obj;
            if (this.typeBinding == null) {
                if (other.typeBinding != null) {
                    return false;
                }
            } else if (!this.typeBinding.equals(other.typeBinding)) {
                return false;
            }

            return true;
        }
    }

    public static class TypeInformationComparator implements Comparator<TypeInfo> {
        public TypeInformationComparator() {
        }

        public int compare(TypeInfo o1, TypeInfo o2) {
            Integer freq1 = o1.getFrequency();
            Integer freq2 = o2.getFrequency();
            return freq2.compareTo(freq1);
        }
    }
}
