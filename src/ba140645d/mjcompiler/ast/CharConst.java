// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:56


package ba140645d.mjcompiler.ast;

public class CharConst extends ConstValue {

    private Character charConst;

    public CharConst (Character charConst) {
        this.charConst=charConst;
    }

    public Character getCharConst() {
        return charConst;
    }

    public void setCharConst(Character charConst) {
        this.charConst=charConst;
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
        buffer.append("CharConst(\n");

        buffer.append(" "+tab+charConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharConst]");
        return buffer.toString();
    }
}
