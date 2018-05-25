// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:40


package ba140645d.mjcompiler.ast;

public class FormParsRepeatListDeclared extends FormParsRepeatList {

    private FormParsRepeatList FormParsRepeatList;
    private FormParsRepeat FormParsRepeat;

    public FormParsRepeatListDeclared (FormParsRepeatList FormParsRepeatList, FormParsRepeat FormParsRepeat) {
        this.FormParsRepeatList=FormParsRepeatList;
        if(FormParsRepeatList!=null) FormParsRepeatList.setParent(this);
        this.FormParsRepeat=FormParsRepeat;
        if(FormParsRepeat!=null) FormParsRepeat.setParent(this);
    }

    public FormParsRepeatList getFormParsRepeatList() {
        return FormParsRepeatList;
    }

    public void setFormParsRepeatList(FormParsRepeatList FormParsRepeatList) {
        this.FormParsRepeatList=FormParsRepeatList;
    }

    public FormParsRepeat getFormParsRepeat() {
        return FormParsRepeat;
    }

    public void setFormParsRepeat(FormParsRepeat FormParsRepeat) {
        this.FormParsRepeat=FormParsRepeat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsRepeatList!=null) FormParsRepeatList.accept(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsRepeatList!=null) FormParsRepeatList.traverseTopDown(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsRepeatList!=null) FormParsRepeatList.traverseBottomUp(visitor);
        if(FormParsRepeat!=null) FormParsRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsRepeatListDeclared(\n");

        if(FormParsRepeatList!=null)
            buffer.append(FormParsRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsRepeat!=null)
            buffer.append(FormParsRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsRepeatListDeclared]");
        return buffer.toString();
    }
}
