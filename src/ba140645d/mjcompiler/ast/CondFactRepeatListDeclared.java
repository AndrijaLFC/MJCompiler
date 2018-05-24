// generated with ast extension for cup
// version 0.8
// 24/4/2018 15:22:4


package ba140645d.mjcompiler.ast;

public class CondFactRepeatListDeclared extends CondFactRepeatList {

    private CondFactRepeatList CondFactRepeatList;
    private CondFactRepeat CondFactRepeat;

    public CondFactRepeatListDeclared (CondFactRepeatList CondFactRepeatList, CondFactRepeat CondFactRepeat) {
        this.CondFactRepeatList=CondFactRepeatList;
        if(CondFactRepeatList!=null) CondFactRepeatList.setParent(this);
        this.CondFactRepeat=CondFactRepeat;
        if(CondFactRepeat!=null) CondFactRepeat.setParent(this);
    }

    public CondFactRepeatList getCondFactRepeatList() {
        return CondFactRepeatList;
    }

    public void setCondFactRepeatList(CondFactRepeatList CondFactRepeatList) {
        this.CondFactRepeatList=CondFactRepeatList;
    }

    public CondFactRepeat getCondFactRepeat() {
        return CondFactRepeat;
    }

    public void setCondFactRepeat(CondFactRepeat CondFactRepeat) {
        this.CondFactRepeat=CondFactRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondFactRepeatList!=null) CondFactRepeatList.accept(visitor);
        if(CondFactRepeat!=null) CondFactRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFactRepeatList!=null) CondFactRepeatList.traverseTopDown(visitor);
        if(CondFactRepeat!=null) CondFactRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFactRepeatList!=null) CondFactRepeatList.traverseBottomUp(visitor);
        if(CondFactRepeat!=null) CondFactRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactRepeatListDeclared(\n");

        if(CondFactRepeatList!=null)
            buffer.append(CondFactRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactRepeat!=null)
            buffer.append(CondFactRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactRepeatListDeclared]");
        return buffer.toString();
    }
}
