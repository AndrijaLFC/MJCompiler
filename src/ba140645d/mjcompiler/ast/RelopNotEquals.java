// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:42


package ba140645d.mjcompiler.ast;

public class RelopNotEquals extends Relop {

    public RelopNotEquals () {
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
        buffer.append("RelopNotEquals(\n");

        buffer.append(tab);
        buffer.append(") [RelopNotEquals]");
        return buffer.toString();
    }
}
