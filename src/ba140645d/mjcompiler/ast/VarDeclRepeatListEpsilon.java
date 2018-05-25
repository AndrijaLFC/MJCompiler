// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:40


package ba140645d.mjcompiler.ast;

public class VarDeclRepeatListEpsilon extends VarDeclRepeatList {

    public VarDeclRepeatListEpsilon () {
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
        buffer.append("VarDeclRepeatListEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [VarDeclRepeatListEpsilon]");
        return buffer.toString();
    }
}
