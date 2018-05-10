// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:56


package ba140645d.mjcompiler.ast;

public class ClassDefinitionEndDerived1 extends ClassDefinitionEnd {

    public ClassDefinitionEndDerived1 () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDefinitionEndDerived1(\n");

        buffer.append(tab);
        buffer.append(") [ClassDefinitionEndDerived1]");
        return buffer.toString();
    }
}
