// generated with ast extension for cup
// version 0.8
// 19/4/2018 17:58:4


package ba140645d.mjcompiler.ast;

public class AddopAddition extends Addop {

    public AddopAddition () {
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
        buffer.append("AddopAddition(\n");

        buffer.append(tab);
        buffer.append(") [AddopAddition]");
        return buffer.toString();
    }
}
