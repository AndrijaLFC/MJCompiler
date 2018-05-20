// generated with ast extension for cup
// version 0.8
// 19/4/2018 17:58:3


package ba140645d.mjcompiler.ast;

public class OptMinusDeclared extends OptMinus {

    public OptMinusDeclared () {
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
        buffer.append("OptMinusDeclared(\n");

        buffer.append(tab);
        buffer.append(") [OptMinusDeclared]");
        return buffer.toString();
    }
}
