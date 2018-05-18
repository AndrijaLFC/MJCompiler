// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:43


package ba140645d.mjcompiler.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private ConstDefinition ConstDefinition;
    private ConstDeclRepeatList ConstDeclRepeatList;

    public ConstDecl (Type Type, ConstDefinition ConstDefinition, ConstDeclRepeatList ConstDeclRepeatList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstDefinition=ConstDefinition;
        if(ConstDefinition!=null) ConstDefinition.setParent(this);
        this.ConstDeclRepeatList=ConstDeclRepeatList;
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstDefinition getConstDefinition() {
        return ConstDefinition;
    }

    public void setConstDefinition(ConstDefinition ConstDefinition) {
        this.ConstDefinition=ConstDefinition;
    }

    public ConstDeclRepeatList getConstDeclRepeatList() {
        return ConstDeclRepeatList;
    }

    public void setConstDeclRepeatList(ConstDeclRepeatList ConstDeclRepeatList) {
        this.ConstDeclRepeatList=ConstDeclRepeatList;
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
        if(ConstDefinition!=null) ConstDefinition.accept(visitor);
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstDefinition!=null) ConstDefinition.traverseTopDown(visitor);
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstDefinition!=null) ConstDefinition.traverseBottomUp(visitor);
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDefinition!=null)
            buffer.append(ConstDefinition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclRepeatList!=null)
            buffer.append(ConstDeclRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
