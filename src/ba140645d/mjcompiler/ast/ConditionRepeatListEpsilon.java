// generated with ast extension for cup
// version 0.8
// 18/4/2018 18:2:51


package ba140645d.mjcompiler.ast;

public class ConditionRepeatListEpsilon extends ConditionRepeatList {

    public ConditionRepeatListEpsilon () {
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
        buffer.append("ConditionRepeatListEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [ConditionRepeatListEpsilon]");
        return buffer.toString();
    }
}
