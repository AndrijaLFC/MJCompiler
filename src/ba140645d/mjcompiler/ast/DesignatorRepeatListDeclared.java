// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:31


package ba140645d.mjcompiler.ast;

public class DesignatorRepeatListDeclared extends DesignatorRepeatList {

    private DesignatorRepeatList DesignatorRepeatList;
    private DesignatorRepeat DesignatorRepeat;

    public DesignatorRepeatListDeclared (DesignatorRepeatList DesignatorRepeatList, DesignatorRepeat DesignatorRepeat) {
        this.DesignatorRepeatList=DesignatorRepeatList;
        if(DesignatorRepeatList!=null) DesignatorRepeatList.setParent(this);
        this.DesignatorRepeat=DesignatorRepeat;
        if(DesignatorRepeat!=null) DesignatorRepeat.setParent(this);
    }

    public DesignatorRepeatList getDesignatorRepeatList() {
        return DesignatorRepeatList;
    }

    public void setDesignatorRepeatList(DesignatorRepeatList DesignatorRepeatList) {
        this.DesignatorRepeatList=DesignatorRepeatList;
    }

    public DesignatorRepeat getDesignatorRepeat() {
        return DesignatorRepeat;
    }

    public void setDesignatorRepeat(DesignatorRepeat DesignatorRepeat) {
        this.DesignatorRepeat=DesignatorRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorRepeatList!=null) DesignatorRepeatList.accept(visitor);
        if(DesignatorRepeat!=null) DesignatorRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorRepeatList!=null) DesignatorRepeatList.traverseTopDown(visitor);
        if(DesignatorRepeat!=null) DesignatorRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorRepeatList!=null) DesignatorRepeatList.traverseBottomUp(visitor);
        if(DesignatorRepeat!=null) DesignatorRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorRepeatListDeclared(\n");

        if(DesignatorRepeatList!=null)
            buffer.append(DesignatorRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorRepeat!=null)
            buffer.append(DesignatorRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorRepeatListDeclared]");
        return buffer.toString();
    }
}
