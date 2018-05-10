// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:57


package ba140645d.mjcompiler.ast;

public class ActParsRepeatListDeclared extends ActParsRepeatList {

    private ActParsRepeatList ActParsRepeatList;
    private ActParsRepeat ActParsRepeat;

    public ActParsRepeatListDeclared (ActParsRepeatList ActParsRepeatList, ActParsRepeat ActParsRepeat) {
        this.ActParsRepeatList=ActParsRepeatList;
        if(ActParsRepeatList!=null) ActParsRepeatList.setParent(this);
        this.ActParsRepeat=ActParsRepeat;
        if(ActParsRepeat!=null) ActParsRepeat.setParent(this);
    }

    public ActParsRepeatList getActParsRepeatList() {
        return ActParsRepeatList;
    }

    public void setActParsRepeatList(ActParsRepeatList ActParsRepeatList) {
        this.ActParsRepeatList=ActParsRepeatList;
    }

    public ActParsRepeat getActParsRepeat() {
        return ActParsRepeat;
    }

    public void setActParsRepeat(ActParsRepeat ActParsRepeat) {
        this.ActParsRepeat=ActParsRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsRepeatList!=null) ActParsRepeatList.accept(visitor);
        if(ActParsRepeat!=null) ActParsRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsRepeatList!=null) ActParsRepeatList.traverseTopDown(visitor);
        if(ActParsRepeat!=null) ActParsRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsRepeatList!=null) ActParsRepeatList.traverseBottomUp(visitor);
        if(ActParsRepeat!=null) ActParsRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsRepeatListDeclared(\n");

        if(ActParsRepeatList!=null)
            buffer.append(ActParsRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsRepeat!=null)
            buffer.append(ActParsRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsRepeatListDeclared]");
        return buffer.toString();
    }
}
