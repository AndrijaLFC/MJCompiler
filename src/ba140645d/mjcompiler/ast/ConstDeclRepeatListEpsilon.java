// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:29


package ba140645d.mjcompiler.ast;

public class ConstDeclRepeatListEpsilon extends ConstDeclRepeatList {

    public ConstDeclRepeatListEpsilon () {
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
        buffer.append("ConstDeclRepeatListEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclRepeatListEpsilon]");
        return buffer.toString();
    }
}
