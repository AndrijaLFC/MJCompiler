// generated with ast extension for cup
// version 0.8
// 19/4/2018 17:58:2


package ba140645d.mjcompiler.ast;

public class VarDeclListEpsilon extends VarDeclList {

    public VarDeclListEpsilon () {
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
        buffer.append("VarDeclListEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListEpsilon]");
        return buffer.toString();
    }
}
