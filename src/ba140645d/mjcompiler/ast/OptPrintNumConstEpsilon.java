// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:57


package ba140645d.mjcompiler.ast;

public class OptPrintNumConstEpsilon extends OptPrintNumConst {

    public OptPrintNumConstEpsilon () {
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
        buffer.append("OptPrintNumConstEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [OptPrintNumConstEpsilon]");
        return buffer.toString();
    }
}
