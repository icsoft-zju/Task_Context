//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.zju.icsoft.taskcontext.util.jstereocode;

public enum MethodStereotype implements CodeStereotype {
    GET(MethodStereotype.Category.ACCESSOR, MethodStereotype.Subcategory.GET),
    PREDICATE(MethodStereotype.Category.ACCESSOR, MethodStereotype.Subcategory.PREDICATE),
    PROPERTY(MethodStereotype.Category.ACCESSOR, MethodStereotype.Subcategory.PROPERTY),
    VOID_ACCESSOR(MethodStereotype.Category.ACCESSOR, MethodStereotype.Subcategory.VOID_ACCESSOR),
    SET(MethodStereotype.Category.MUTATOR, MethodStereotype.Subcategory.SET),
    COMMAND(MethodStereotype.Category.MUTATOR, MethodStereotype.Subcategory.COMMAND),
    NON_VOID_COMMAND(MethodStereotype.Category.MUTATOR, MethodStereotype.Subcategory.NON_VOID_COMMAND),
    CONSTRUCTOR(MethodStereotype.Category.CREATIONAL, MethodStereotype.Subcategory.CONSTRUCTOR),
    COPY_CONSTRUCTOR(MethodStereotype.Category.CREATIONAL, MethodStereotype.Subcategory.COPY_CONSTRUCTOR),
    DESTRUCTOR(MethodStereotype.Category.CREATIONAL, MethodStereotype.Subcategory.DESTRUCTOR),
    FACTORY(MethodStereotype.Category.CREATIONAL, MethodStereotype.Subcategory.FACTORY),
    COLLABORATOR(MethodStereotype.Category.COLLABORATIONAL, MethodStereotype.Subcategory.COLLABORATOR),
    CONTROLLER(MethodStereotype.Category.COLLABORATIONAL, MethodStereotype.Subcategory.CONTROLLER),
    LOCAL_CONTROLLER(MethodStereotype.Category.COLLABORATIONAL, MethodStereotype.Subcategory.LOCAL_CONTROLLER),
    INCIDENTAL(MethodStereotype.Category.DEGENERATE, MethodStereotype.Subcategory.INCIDENTAL),
    EMPTY(MethodStereotype.Category.DEGENERATE, MethodStereotype.Subcategory.EMPTY),
    ABSTRACT(MethodStereotype.Category.DEGENERATE, MethodStereotype.Subcategory.ABSTRACT);

    private final MethodStereotype.Category category;
    private final MethodStereotype.Subcategory subcategory;

    private MethodStereotype(MethodStereotype.Category category, MethodStereotype.Subcategory subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

    public MethodStereotype.Category getCategory() {
        return this.category;
    }

    public MethodStereotype.Subcategory getSubcategory() {
        return this.subcategory;
    }

    public static enum Category {
        ACCESSOR,
        MUTATOR,
        CREATIONAL,
        COLLABORATIONAL,
        DEGENERATE;

        private Category() {
        }
    }

    public static enum Subcategory {
        GET,
        PREDICATE,
        PROPERTY,
        VOID_ACCESSOR,
        SET,
        COMMAND,
        NON_VOID_COMMAND,
        CONSTRUCTOR,
        COPY_CONSTRUCTOR,
        DESTRUCTOR,
        FACTORY,
        COLLABORATOR,
        CONTROLLER,
        LOCAL_CONTROLLER,
        INCIDENTAL,
        EMPTY,
        ABSTRACT;

        private Subcategory() {
        }
    }
}

