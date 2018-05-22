// generated with ast extension for cup
// version 0.8
// 22/4/2018 18:10:37


package ba140645d.mjcompiler.ast;

public class OptArrayDeclEpsilon extends OptArrayDecl {

    public OptArrayDeclEpsilon () {
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
        buffer.append("OptArrayDeclEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [OptArrayDeclEpsilon]");
        return buffer.toString();
    }
}
