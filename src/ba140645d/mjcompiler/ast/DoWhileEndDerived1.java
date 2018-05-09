// generated with ast extension for cup
// version 0.8
// 9/4/2018 20:42:53


package ba140645d.mjcompiler.ast;

public class DoWhileEndDerived1 extends DoWhileEnd {

    public DoWhileEndDerived1 () {
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
        buffer.append("DoWhileEndDerived1(\n");

        buffer.append(tab);
        buffer.append(") [DoWhileEndDerived1]");
        return buffer.toString();
    }
}
