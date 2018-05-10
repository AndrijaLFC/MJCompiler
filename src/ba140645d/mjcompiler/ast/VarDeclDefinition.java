// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:56


package ba140645d.mjcompiler.ast;

public class VarDeclDefinition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String varName;
    private OptArrayDecl OptArrayDecl;

    public VarDeclDefinition (String varName, OptArrayDecl OptArrayDecl) {
        this.varName=varName;
        this.OptArrayDecl=OptArrayDecl;
        if(OptArrayDecl!=null) OptArrayDecl.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public OptArrayDecl getOptArrayDecl() {
        return OptArrayDecl;
    }

    public void setOptArrayDecl(OptArrayDecl OptArrayDecl) {
        this.OptArrayDecl=OptArrayDecl;
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
        if(OptArrayDecl!=null) OptArrayDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptArrayDecl!=null) OptArrayDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptArrayDecl!=null) OptArrayDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclDefinition(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(OptArrayDecl!=null)
            buffer.append(OptArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclDefinition]");
        return buffer.toString();
    }
}
