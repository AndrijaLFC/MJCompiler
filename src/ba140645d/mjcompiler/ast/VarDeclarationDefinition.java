// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:29


package ba140645d.mjcompiler.ast;

public class VarDeclarationDefinition extends VarDeclDefinition {

    private String varName;
    private OptArrayDecl OptArrayDecl;

    public VarDeclarationDefinition (String varName, OptArrayDecl OptArrayDecl) {
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
        buffer.append("VarDeclarationDefinition(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(OptArrayDecl!=null)
            buffer.append(OptArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarationDefinition]");
        return buffer.toString();
    }
}
