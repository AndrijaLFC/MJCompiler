// generated with ast extension for cup
// version 0.8
// 9/4/2018 20:42:54


package ba140645d.mjcompiler.ast;

public class MulopMultiplication extends Mulop {

    public MulopMultiplication () {
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
        buffer.append("MulopMultiplication(\n");

        buffer.append(tab);
        buffer.append(") [MulopMultiplication]");
        return buffer.toString();
    }
}