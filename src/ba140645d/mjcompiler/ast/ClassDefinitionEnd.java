// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:43


package ba140645d.mjcompiler.ast;

public class ClassDefinitionEnd implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public ClassDefinitionEnd () {
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
        buffer.append("ClassDefinitionEnd(\n");

        buffer.append(tab);
        buffer.append(") [ClassDefinitionEnd]");
        return buffer.toString();
    }
}
