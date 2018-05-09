// generated with ast extension for cup
// version 0.8
// 9/4/2018 20:42:52


package ba140645d.mjcompiler.ast;

public class VarDeclDefinitionDerived1 extends VarDeclDefinition {

    private OptArrayDecl OptArrayDecl;

    public VarDeclDefinitionDerived1 (OptArrayDecl OptArrayDecl) {
        this.OptArrayDecl=OptArrayDecl;
        if(OptArrayDecl!=null) OptArrayDecl.setParent(this);
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
        buffer.append("VarDeclDefinitionDerived1(\n");

        if(OptArrayDecl!=null)
            buffer.append(OptArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclDefinitionDerived1]");
        return buffer.toString();
    }
}
