package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class SemanticAnalyzer extends VisitorAdaptor{

    private boolean firstIteration = true;
    private Type currentType = null;
    private Obj currentTypeObj = null;

    @Override
    public void visit(Program Program) {

    }

    @Override
    public void visit(ProgramName programName){

    }

    @Override
    public void visit(ConstDecl constDecl){

    }

    private boolean isDefinedInCurrentScope(String symName){
        return Tab.currentScope().findSymbol(symName) != Tab.noObj;
    }

    private boolean isDefined(String symName){
        return Tab.find(symName) != Tab.noObj;
    }


    private void logInfo(String message){
        System.err.println(message);
    }
    @Override
    public void visit(Type type){
        String typeName = type.getTypeName();

        currentTypeObj = Tab.noObj;
        if (isDefined(typeName)){
            Obj typeObj = Tab.find(typeName);

            if (typeObj.getKind() == Obj.Type){
                currentTypeObj = typeObj;
            }else{
                logInfo("Simbol [" + typeName + "] nije tip!");
            }

        }else{
            logInfo("Simbol [" + typeName + "] nije definisan!");
        }

        currentType = type;
    }

}