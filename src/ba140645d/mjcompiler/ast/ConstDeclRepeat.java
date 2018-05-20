// generated with ast extension for cup
// version 0.8
// 19/4/2018 17:58:2


package ba140645d.mjcompiler.ast;

public class ConstDeclRepeat implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDefinition ConstDefinition;

    public ConstDeclRepeat (ConstDefinition ConstDefinition) {
        this.ConstDefinition=ConstDefinition;
        if(ConstDefinition!=null) ConstDefinition.setParent(this);
    }

    public ConstDefinition getConstDefinition() {
        return ConstDefinition;
    }

    public void setConstDefinition(ConstDefinition ConstDefinition) {
        this.ConstDefinition=ConstDefinition;
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
        if(ConstDefinition!=null) ConstDefinition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDefinition!=null) ConstDefinition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDefinition!=null) ConstDefinition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclRepeat(\n");

        if(ConstDefinition!=null)
            buffer.append(ConstDefinition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclRepeat]");
        return buffer.toString();
    }
}
