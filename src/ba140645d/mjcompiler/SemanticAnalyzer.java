package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import com.sun.istack.internal.Nullable;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor{

    // da li je semanticka provera
    private boolean semanticCheck = true;

    // trenutni tip
    private Type currentType = null;

    // objekat trenutnog tipa
    private Obj currentTypeObj = Tab.noObj;

    //struktura trenutnog tipa
    private Struct currentTypeStruct = Tab.noType;

    // objekat trenutnog metoda
    private Obj currentMethod = Tab.noObj;

    // objekat pocetnog designatora
    private Obj designatorObj = Tab.noObj;




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
        String lineInfo = "Na liniji (" + node.getLine() + "): ";
        System.out.println(lineInfo + message);
    }

    private void logError(String message, SyntaxNode node){
        String lineInfo = "Greska na liniji (" + node.getLine() + "): ";
        System.err.println(lineInfo + message);
    }

    private String formatAlreadyDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] je vec definisan!";
    }

    private String formatSymbolNotDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] nije definisan!";
    }

    private String formatAssigmentMessage(Struct leftSideType, Struct rightSideType, @Nullable Struct expectedType){
        String leftSideTypeName = symbolTypeToString(leftSideType.getKind());
        String rightSideTypeName = symbolTypeToString(rightSideType.getKind());
        String expectedTypeName = (expectedType != null)? symbolTypeToString(expectedType.getKind()) : null;

        String message = "Na levoj strani je tip : {" + leftSideTypeName + "}. Na desnoj strani je tip : {" + rightSideTypeName +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedTypeName + "}";

        return message;
    }

    private String formatAssigmentMessage(String leftSideType, String rightSideType, @Nullable String expectedType){

        String message = "Na levoj strani je tip : {" + leftSideType+ "}. Na desnoj strani je tip : {" + rightSideType +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedType + "}";

        return message;
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

            // da li je sa desne strane bool konstanta
            boolean isConstValBool = false;

            // postavljamo tip podatka sa desne strane i citamo vrednost
            if (constValue instanceof NumConst){
                rightSide = Tab.intType;

                value = ((NumConst) constValue).getNumConst();
            }else if (constValue instanceof CharConst){
                rightSide = Tab.charType;

                value = ((CharConst) constValue).getCharConst();
            }else if (constValue instanceof BoolConst){
                rightSide = Tab.find("bool").getType();

                isConstValBool = true;

                value = ((BoolConst) constValue).getBoolConst()  ? 1 : 0;
            }

            // da li je bool tip
            boolean isBoolDef = currentTypeObj.getName().equals("bool");

            if ((isBoolDef && isConstValBool) || (type.assignableTo(rightSide) && !isBoolDef) ){
                Obj constObj = Tab.insert(Obj.Con, constName, type);

                logInfo(formatSymbolInfo(constObj), constDefinition);

                constObj.setAdr(value);
            }else{
                String assigmentErrorMsg = formatAssigmentMessage(currentTypeObj.getName(), symbolTypeToString(rightSide.getKind()), currentTypeObj.getName());

                logError(assigmentErrorMsg, constDefinition);
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
            logError(formatSymbolName(typeObj.getName()) + " nije tip!", type);

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

        Obj varObj = null;
        // ako je deklaracija niza, onda postavljamo za tip array strukturu
        // inace postavljamo sam tip koji je naveden
        if (isArrayDecl)
            varObj = Tab.insert(Obj.Var, varName, arrayStruct);
        else
            varObj = Tab.insert(Obj.Var, varName, currentTypeObj.getType());

        logInfo(formatSymbolInfo(varObj), varDeclDefinition);
    }

    @Override
    public void visit(MethodName methodName){
        String name = methodName.getMethodName();

        // uzimamo da je podrazumevano noObj
        currentMethod = Tab.noObj;

        // ako je vec definisano onda prijavimo gresku
        if (isDefined(name))
            logError(formatAlreadyDefinedMessage(name), methodName);
        else
            currentMethod = Tab.insert(Obj.Meth, name, currentTypeStruct);

        // otvorimo opseg
        Tab.openScope();
    }

    @Override
    public void visit(MethodDecl methodDecl){

        // uvezujemo lokalne simbole samo ukoliko je
        // metoda dodata u tabelu simbola
        if (currentMethod != Tab.noObj) {
            Tab.chainLocalSymbols(currentMethod);

            logInfo(formatSymbolInfo(currentMethod), methodDecl);
            //logInfo(for);
        }

        // zatvorimo doseg
        Tab.closeScope();
    }


    @Override
    public void visit(ReturnTypeVoid voidReturnType){
        currentTypeStruct = Tab.noType;
    }

    @Override
    public void visit(FormParDecl formParDecl){
        // naziv parametra
        String formParName = formParDecl.getFormParName();

        // ako je vec definisan samo napisati gresku i vratiti se
        if (isDefinedInCurrentScope(formParName)){
            logError(formatAlreadyDefinedMessage(formParName), formParDecl);
            return;
        }

        // da li je niz?
        OptArrayDecl isArray = formParDecl.getOptArrayDecl();

        // niz struktura u slucaju da je tip niz nekog tipa podatka (npr int[])
        Struct arrayStruct = new Struct(Struct.Array, currentTypeStruct);

        Obj paramObj = null;

        if (isArray instanceof  OptArrayDeclared)
            paramObj = Tab.insert(Obj.Var, formParName, arrayStruct);
        else
            paramObj = Tab.insert(Obj.Var, formParName, currentTypeStruct);

        // informativno logovanje
        logInfo(formatSymbolInfo(paramObj), formParDecl);
    }


    @Override
    public void visit(DesignatorInitialName designatorInitialName){
        logInfo(designatorInitialName.getDesignatorName() +" initial", designatorInitialName);

        designatorObj = Tab.find(designatorInitialName.getDesignatorName());

        if (designatorObj == Tab.noObj)
            logError(formatSymbolNotDefinedMessage(designatorInitialName.getDesignatorName()), designatorInitialName);
        
    }

    @Override
    public void visit(DesignatorRepeatField designatorRepeatField){
        logInfo(designatorRepeatField.getFieldName(), designatorRepeatField);
    }














}