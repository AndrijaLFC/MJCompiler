// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:20:47


package ba140645d.mjcompiler.ast;

public class Else extends ElseStart {

    public Else () {
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
        buffer.append("Else(\n");

        buffer.append(tab);
        buffer.append(") [Else]");
        return buffer.toString();
    }
}