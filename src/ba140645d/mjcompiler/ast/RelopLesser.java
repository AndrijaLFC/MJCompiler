// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:45


package ba140645d.mjcompiler.ast;

public class RelopLesser extends Relop {

    public RelopLesser () {
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
        buffer.append("RelopLesser(\n");

        buffer.append(tab);
        buffer.append(") [RelopLesser]");
        return buffer.toString();
    }
}
