// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:44


package ba140645d.mjcompiler.ast;

public class OptElseStatementEpsilon extends OptElseStatement {

    public OptElseStatementEpsilon () {
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
        buffer.append("OptElseStatementEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [OptElseStatementEpsilon]");
        return buffer.toString();
    }
}
