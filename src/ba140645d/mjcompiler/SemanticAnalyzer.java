package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor{

    private boolean firstIteration = true;
    private Type currentType = null;
    private Obj currentTypeObj = Tab.noObj;
    private Obj currentMethod = Tab.noObj;
    private Struct currentTypeStruct = Tab.nullType;



    public SemanticAnalyzer(){
        Tab.init();


        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", Tab.intType));
    }


    private boolean isDefinedInCurrentScope(String symName){
        return Tab.currentScope().findSymbol(symName) != null;
    }

    private boolean isDefined(String symName){
        return Tab.find(symName) != Tab.noObj;
    }


    private void logInfo(String message, SyntaxNode node){
        String lineInfo = "Na liniji (" + node.getLine() + "):";
        System.out.println(lineInfo + message);
    }

    private void logError(String message, SyntaxNode node){
        String lineInfo = "Greska na liniji (" + node.getLine() + "):";
        System.err.println(lineInfo + message);
    }

    private String formatAlreadyDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] je vec definisan!";
    }

    private String formatSymbolNotDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] nije definisan!";
    }

    private String formatSymbolName(String symbolName){
        return "Simbol [" + symbolName + "]";
    }

    private String symbolKindToString(int symbolKind){
        String retVal = "Unknown";
        switch(symbolKind){
            case Obj.Con :  retVal =  "Con"; break;
            case Obj.Elem : retVal = "Elem"; break;
            case Obj.Fld : retVal = "Fld"; break;
            case Obj.Meth : retVal = "Meth"; break;
            case Obj.Prog : retVal = "Prog"; break;
            case Obj.Type : retVal = "Type"; break;
            case Obj.Var : retVal = "Var"; break;
        }

        return retVal;
    }

    private String symbolTypeToString(int symbolType){
        String retVal = "Unknown";

        switch(symbolType){
            case Struct.Array : retVal = "Array";       break;
            case Struct.Char :  retVal = "Char";        break;
            case Struct.Int :   retVal = "Int";         break;
            case Struct.None :  retVal = "None";        break;
        }

        return retVal;
    }

    private String formatSymbolInfo(Obj symbol){
        String symbolNameFormat = formatSymbolName(symbol.getName());
        String symbolKind = symbolKindToString(symbol.getKind());
        String symbolType = symbolTypeToString(symbol.getType().getKind());

        String format = symbolNameFormat + " kind : " + symbolKind + ", type : " + symbolType;

        if (symbol.getType().getKind() == Struct.Array)
            format += " of " + symbolTypeToString(symbol.getType().getElemType().getKind());


        return format;

    }

    @Override
    public void visit(Program program) {

        // pronalazimo simbol programa u tabeli simbola

        Obj programObj = Tab.find(program.getProgramName().getProgramName());

        logInfo(formatSymbolInfo(programObj), program);

        // ulancamo u njega sve lokalne simbole
        Tab.chainLocalSymbols(programObj);

        // zatvorimo opseg
        Tab.closeScope();
    }

    @Override
    public void visit(ProgramName programName){
        // ubacujemo Program u tabelu simbola
        Tab.insert(Obj.Prog, programName.getProgramName(), Tab.noType);

        // otvaramo opseg vazenja
        Tab.openScope();
    }


    @Override
    public void visit(ConstDefinition constDefinition){

        // ako tip ne postoji, odnosno nismo ga prepoznali u tabeli simbola
        // to znaci da nema potrebe da proveravamo definiciju konstante
        if (currentTypeObj == Tab.noObj)
            return;

        // naziv konstante
        String constName = constDefinition.getConstName();

        if (isDefined(constName))
            logError(formatAlreadyDefinedMessage(constName), constDefinition);
        else{
            // dohvatanje vrste tipa podatka
            Struct type = currentTypeObj.getType();

            // produkcija koja je jedna od 3 moguce konstante
            ConstValue constValue = constDefinition.getConstValue();
            // vrednost konstante
            int value = 0;

            // tip sa desne strane dodele
            Struct rightSide = Tab.noType;

            if (constValue instanceof NumConst){
                rightSide = Tab.intType;

                value = ((NumConst) constValue).getNumConst();
            }else if (constValue instanceof CharConst){
                rightSide = Tab.charType;

                value = ((CharConst) constValue).getCharConst();
            }else if (constValue instanceof BoolConst){
                rightSide = Tab.find("bool").getType();

                value = ((BoolConst) constValue).getBoolConst()  ? 1 : 0;
            }

            if (type.assignableTo(rightSide)){
                Obj constObj = Tab.insert(Obj.Con, constName, type);

                logInfo(formatSymbolInfo(constObj), constDefinition);

                constObj.setAdr(value);
            }else{
                //TODO logError incompatible types
            }


        }
    }

    @Override
    public void visit(Type type){


        String typeName = type.getTypeName();

        currentTypeObj = Tab.noObj;

        currentType = type;

        if (!isDefined(typeName)){
            logError(formatSymbolNotDefinedMessage(typeName), type);

            return;
        }


        // simbol je vec definisan,sada treba videti da li je u pitanju tip
        Obj typeObj = Tab.find(typeName);

        if (typeObj.getKind() == Obj.Type)
            currentTypeObj = typeObj;
        else
            logInfo("Simbol [" + typeName + "] nije tip!", type);

        currentTypeStruct = currentTypeObj.getType();
    }

    @Override
    public void visit(VarDeclDefinition varDeclDefinition){
        String varName = varDeclDefinition.getVarName();

        // simbol vec definisan
        if (isDefinedInCurrentScope(varName)) {
            logInfo("Simbol[" + varName + "] je vec definisan u trenutnom opsegu!", varDeclDefinition);
            return;
        }

        // vracamo se takodje i ako nismo naisli na validan tip podatka
        if (currentTypeObj == Tab.noObj)
            return;

        boolean isArrayDecl = (varDeclDefinition.getOptArrayDecl() instanceof OptArrayDeclared);

        // u slucaju da je tip podatka niz nekog tipa, potrebna nam je struktura
        // koja predstavlja niz tog tipa
        Struct arrayStruct = new Struct(Struct.Array, currentTypeObj.getType());

        // ako je deklaracija niza, onda postavljamo za tip array strukturu
        // inace postavljamo sam tip koji je naveden
        if (isArrayDecl)
            Tab.insert(Obj.Var, varName, arrayStruct);
        else
            Tab.insert(Obj.Var, varName, currentTypeObj.getType());
    }

    @Override
    public void visit(MethodName methodName){
        String name = methodName.getMethodName();

        if (isDefined(name))
            logError(formatAlreadyDefinedMessage(name), methodName);
        else
            currentMethod = Tab.insert(Obj.Meth, name, currentTypeStruct);

        Tab.openScope();
    }

    @Override
    public void visit(MethodDecl methodDecl){

        if (currentMethod != Tab.noObj) {
            Tab.chainLocalSymbols(currentMethod);

            //logInfo(for);
        }

        Tab.closeScope();
    }


    @Override
    public void visit(ReturnTypeVoid voidReturnType){
        currentTypeStruct = Tab.nullType;
    }


}