// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:56


package ba140645d.mjcompiler.ast;

public class FormPars implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private FormParDecl FormParDecl;
    private FormParsRepeatList FormParsRepeatList;

    public FormPars (FormParDecl FormParDecl, FormParsRepeatList FormParsRepeatList) {
        this.FormParDecl=FormParDecl;
        if(FormParDecl!=null) FormParDecl.setParent(this);
        this.FormParsRepeatList=FormParsRepeatList;
        if(FormParsRepeatList!=null) FormParsRepeatList.setParent(this);
    }

    public FormParDecl getFormParDecl() {
        return FormParDecl;
    }

    public void setFormParDecl(FormParDecl FormParDecl) {
        this.FormParDecl=FormParDecl;
    }

    public FormParsRepeatList getFormParsRepeatList() {
        return FormParsRepeatList;
    }

    public void setFormParsRepeatList(FormParsRepeatList FormParsRepeatList) {
        this.FormParsRepeatList=FormParsRepeatList;
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
        if(FormParDecl!=null) FormParDecl.accept(visitor);
        if(FormParsRepeatList!=null) FormParsRepeatList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParDecl!=null) FormParDecl.traverseTopDown(visitor);
        if(FormParsRepeatList!=null) FormParsRepeatList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParDecl!=null) FormParDecl.traverseBottomUp(visitor);
        if(FormParsRepeatList!=null) FormParsRepeatList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormPars(\n");

        if(FormParDecl!=null)
            buffer.append(FormParDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsRepeatList!=null)
            buffer.append(FormParsRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormPars]");
        return buffer.toString();
    }
}
