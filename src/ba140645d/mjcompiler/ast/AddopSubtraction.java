// generated with ast extension for cup
// version 0.8
// 18/4/2018 18:2:52


package ba140645d.mjcompiler.ast;

public class AddopSubtraction extends Addop {

    public AddopSubtraction () {
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
        buffer.append("AddopSubtraction(\n");

        buffer.append(tab);
        buffer.append(") [AddopSubtraction]");
        return buffer.toString();
    }
}
