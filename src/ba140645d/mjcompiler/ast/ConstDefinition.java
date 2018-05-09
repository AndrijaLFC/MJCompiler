// generated with ast extension for cup
// version 0.8
// 9/4/2018 20:42:52


package ba140645d.mjcompiler.ast;

public class ConstDefinition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private NumCharBoolConst NumCharBoolConst;

    public ConstDefinition (NumCharBoolConst NumCharBoolConst) {
        this.NumCharBoolConst=NumCharBoolConst;
        if(NumCharBoolConst!=null) NumCharBoolConst.setParent(this);
    }

    public NumCharBoolConst getNumCharBoolConst() {
        return NumCharBoolConst;
    }

    public void setNumCharBoolConst(NumCharBoolConst NumCharBoolConst) {
        this.NumCharBoolConst=NumCharBoolConst;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NumCharBoolConst!=null) NumCharBoolConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NumCharBoolConst!=null) NumCharBoolConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NumCharBoolConst!=null) NumCharBoolConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDefinition(\n");

        if(NumCharBoolConst!=null)
            buffer.append(NumCharBoolConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDefinition]");
        return buffer.toString();
    }
}
