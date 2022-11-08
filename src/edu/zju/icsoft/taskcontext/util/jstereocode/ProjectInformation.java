package edu.zju.icsoft.taskcontext.util.jstereocode;

import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectInformation {
    private int totalIUnits = 0;
    private int totalTypes = 0;
    private int totalMethods = 0;
    private Vector<Integer> methodsCounter = new Vector<Integer>();
    private double methodsMean;
    private double methodsStdDev;
    private IJavaProject project;

    public ProjectInformation(IJavaProject project) {
        this.project = project;
    }

    public void compute() {
        try {
            IPackageFragment[] var4;
            int var3 = (var4 = this.project.getPackageFragments()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                IPackageFragment element = var4[var2];
                ICompilationUnit[] var8;
                int var7 = (var8 = element.getCompilationUnits()).length;

                for(int var6 = 0; var6 < var7; ++var6) {
                    ICompilationUnit junit = var8[var6];
                    boolean typeCounted = false;
                    IType[] var13;
                    int var12 = (var13 = junit.getAllTypes()).length;

                    for(int var11 = 0; var11 < var12; ++var11) {
                        IType type = var13[var11];
                        if (type.isInterface() || type.isClass()) {
                            if (!typeCounted) {
                                ++this.totalIUnits;
                                typeCounted = true;
                            }

                            ++this.totalTypes;
                            this.methodsCounter.add(type.getMethods().length);
                            this.totalMethods += type.getMethods().length;
                        }
                    }
                }
            }

            if (this.methodsCounter.size() != 0) {
                this.methodsMean = (double)(this.totalMethods / this.methodsCounter.size());
                double sumOfSquareDifference = 0.0D;

                for(Iterator var17 = this.methodsCounter.iterator(); var17.hasNext(); this.methodsStdDev = Math.sqrt(sumOfSquareDifference / (double)this.methodsCounter.size())) {
                    Integer i = (Integer)var17.next();
                    sumOfSquareDifference += Math.pow((double)i - this.methodsMean, 2.0D);
                }
            } else {
                this.methodsMean = 0.0D;
                this.methodsStdDev = 0.0D;
            }
        } catch (JavaModelException var14) {
            System.err.println("Oops! An error occured when computing project information");
        }

    }

    public int getTotalUnits() {
        return this.totalIUnits;
    }

    public int getTotalTypes() {
        return this.totalTypes;
    }

    public Vector<Integer> getMethodsCounter() {
        return this.methodsCounter;
    }

    public double getMethodsMean() {
        return this.methodsMean;
    }

    public double getMethodsStdDev() {
        return this.methodsStdDev;
    }

    public String getName() {
        return this.project.getElementName();
    }
}

