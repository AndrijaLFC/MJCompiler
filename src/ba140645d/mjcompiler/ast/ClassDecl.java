// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:40


package ba140645d.mjcompiler.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassName ClassName;
    private OptExtends OptExtends;
    private ClassDefinitionStart ClassDefinitionStart;
    private VarDeclList VarDeclList;
    private OptMethodDeclList OptMethodDeclList;
    private ClassDefinitionEnd ClassDefinitionEnd;

    public ClassDecl (ClassName ClassName, OptExtends OptExtends, ClassDefinitionStart ClassDefinitionStart, VarDeclList VarDeclList, OptMethodDeclList OptMethodDeclList, ClassDefinitionEnd ClassDefinitionEnd) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.OptExtends=OptExtends;
        if(OptExtends!=null) OptExtends.setParent(this);
        this.ClassDefinitionStart=ClassDefinitionStart;
        if(ClassDefinitionStart!=null) ClassDefinitionStart.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.OptMethodDeclList=OptMethodDeclList;
        if(OptMethodDeclList!=null) OptMethodDeclList.setParent(this);
        this.ClassDefinitionEnd=ClassDefinitionEnd;
        if(ClassDefinitionEnd!=null) ClassDefinitionEnd.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public OptExtends getOptExtends() {
        return OptExtends;
    }

    public void setOptExtends(OptExtends OptExtends) {
        this.OptExtends=OptExtends;
    }

    public ClassDefinitionStart getClassDefinitionStart() {
        return ClassDefinitionStart;
    }

    public void setClassDefinitionStart(ClassDefinitionStart ClassDefinitionStart) {
        this.ClassDefinitionStart=ClassDefinitionStart;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public OptMethodDeclList getOptMethodDeclList() {
        return OptMethodDeclList;
    }

    public void setOptMethodDeclList(OptMethodDeclList OptMethodDeclList) {
        this.OptMethodDeclList=OptMethodDeclList;
    }

    public ClassDefinitionEnd getClassDefinitionEnd() {
        return ClassDefinitionEnd;
    }

    public void setClassDefinitionEnd(ClassDefinitionEnd ClassDefinitionEnd) {
        this.ClassDefinitionEnd=ClassDefinitionEnd;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(OptExtends!=null) OptExtends.accept(visitor);
        if(ClassDefinitionStart!=null) ClassDefinitionStart.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(OptMethodDeclList!=null) OptMethodDeclList.accept(visitor);
        if(ClassDefinitionEnd!=null) ClassDefinitionEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(OptExtends!=null) OptExtends.traverseTopDown(visitor);
        if(ClassDefinitionStart!=null) ClassDefinitionStart.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(OptMethodDeclList!=null) OptMethodDeclList.traverseTopDown(visitor);
        if(ClassDefinitionEnd!=null) ClassDefinitionEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(OptExtends!=null) OptExtends.traverseBottomUp(visitor);
        if(ClassDefinitionStart!=null) ClassDefinitionStart.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(OptMethodDeclList!=null) OptMethodDeclList.traverseBottomUp(visitor);
        if(ClassDefinitionEnd!=null) ClassDefinitionEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptExtends!=null)
            buffer.append(OptExtends.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassDefinitionStart!=null)
            buffer.append(ClassDefinitionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptMethodDeclList!=null)
            buffer.append(OptMethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassDefinitionEnd!=null)
            buffer.append(ClassDefinitionEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
