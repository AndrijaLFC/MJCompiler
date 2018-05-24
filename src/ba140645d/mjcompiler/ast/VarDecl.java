// generated with ast extension for cup
// version 0.8
// 24/4/2018 15:22:3


package ba140645d.mjcompiler.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private VarDeclDefinition VarDeclDefinition;
    private VarDeclRepeatList VarDeclRepeatList;

    public VarDecl (Type Type, VarDeclDefinition VarDeclDefinition, VarDeclRepeatList VarDeclRepeatList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclDefinition=VarDeclDefinition;
        if(VarDeclDefinition!=null) VarDeclDefinition.setParent(this);
        this.VarDeclRepeatList=VarDeclRepeatList;
        if(VarDeclRepeatList!=null) VarDeclRepeatList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclDefinition getVarDeclDefinition() {
        return VarDeclDefinition;
    }

    public void setVarDeclDefinition(VarDeclDefinition VarDeclDefinition) {
        this.VarDeclDefinition=VarDeclDefinition;
    }

    public VarDeclRepeatList getVarDeclRepeatList() {
        return VarDeclRepeatList;
    }

    public void setVarDeclRepeatList(VarDeclRepeatList VarDeclRepeatList) {
        this.VarDeclRepeatList=VarDeclRepeatList;
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
        if(VarDeclDefinition!=null) VarDeclDefinition.accept(visitor);
        if(VarDeclRepeatList!=null) VarDeclRepeatList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclDefinition!=null) VarDeclDefinition.traverseTopDown(visitor);
        if(VarDeclRepeatList!=null) VarDeclRepeatList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclDefinition!=null) VarDeclDefinition.traverseBottomUp(visitor);
        if(VarDeclRepeatList!=null) VarDeclRepeatList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclDefinition!=null)
            buffer.append(VarDeclDefinition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclRepeatList!=null)
            buffer.append(VarDeclRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
