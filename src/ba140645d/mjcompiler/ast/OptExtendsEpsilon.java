// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:43


package ba140645d.mjcompiler.ast;

public class OptExtendsEpsilon extends OptExtends {

    public OptExtendsEpsilon () {
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
        buffer.append("OptExtendsEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [OptExtendsEpsilon]");
        return buffer.toString();
    }
}
