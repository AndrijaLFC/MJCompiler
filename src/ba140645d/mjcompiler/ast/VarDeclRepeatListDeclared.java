// generated with ast extension for cup
// version 0.8
// 24/4/2018 15:22:3


package ba140645d.mjcompiler.ast;

public class VarDeclRepeatListDeclared extends VarDeclRepeatList {

    private VarDeclRepeatList VarDeclRepeatList;
    private VarDeclRepeat VarDeclRepeat;

    public VarDeclRepeatListDeclared (VarDeclRepeatList VarDeclRepeatList, VarDeclRepeat VarDeclRepeat) {
        this.VarDeclRepeatList=VarDeclRepeatList;
        if(VarDeclRepeatList!=null) VarDeclRepeatList.setParent(this);
        this.VarDeclRepeat=VarDeclRepeat;
        if(VarDeclRepeat!=null) VarDeclRepeat.setParent(this);
    }

    public VarDeclRepeatList getVarDeclRepeatList() {
        return VarDeclRepeatList;
    }

    public void setVarDeclRepeatList(VarDeclRepeatList VarDeclRepeatList) {
        this.VarDeclRepeatList=VarDeclRepeatList;
    }

    public VarDeclRepeat getVarDeclRepeat() {
        return VarDeclRepeat;
    }

    public void setVarDeclRepeat(VarDeclRepeat VarDeclRepeat) {
        this.VarDeclRepeat=VarDeclRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclRepeatList!=null) VarDeclRepeatList.accept(visitor);
        if(VarDeclRepeat!=null) VarDeclRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclRepeatList!=null) VarDeclRepeatList.traverseTopDown(visitor);
        if(VarDeclRepeat!=null) VarDeclRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclRepeatList!=null) VarDeclRepeatList.traverseBottomUp(visitor);
        if(VarDeclRepeat!=null) VarDeclRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclRepeatListDeclared(\n");

        if(VarDeclRepeatList!=null)
            buffer.append(VarDeclRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclRepeat!=null)
            buffer.append(VarDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclRepeatListDeclared]");
        return buffer.toString();
    }
}
