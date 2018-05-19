// generated with ast extension for cup
// version 0.8
// 18/4/2018 18:2:51


package ba140645d.mjcompiler.ast;

public class FormParDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String formParName;
    private OptArrayDecl OptArrayDecl;

    public FormParDecl (Type Type, String formParName, OptArrayDecl OptArrayDecl) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.formParName=formParName;
        this.OptArrayDecl=OptArrayDecl;
        if(OptArrayDecl!=null) OptArrayDecl.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getFormParName() {
        return formParName;
    }

    public void setFormParName(String formParName) {
        this.formParName=formParName;
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
        if(Type!=null) Type.accept(visitor);
        if(OptArrayDecl!=null) OptArrayDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptArrayDecl!=null) OptArrayDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptArrayDecl!=null) OptArrayDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+formParName);
        buffer.append("\n");

        if(OptArrayDecl!=null)
            buffer.append(OptArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParDecl]");
        return buffer.toString();
    }
}
