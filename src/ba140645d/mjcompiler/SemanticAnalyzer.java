package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import ba140645d.mjcompiler.ast.Condition;
import ba140645d.mjcompiler.utilities.StructLinkedList;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

import java.util.Iterator;

public class SemanticAnalyzer extends VisitorAdaptor{

    // kod za Bool tip
    public static final int Bool = 5;

    public static final Struct boolType = new Struct(Bool);
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

    // broj formalnih parametara metode
    private int currentMethodFormParNum = 0;

    // objekat pocetnog designatora
    private Obj designatorObj = Tab.noObj;

    // fleg da li smo u petlji
    private int insideLoop = 0;

    // fleg koji oznacava da li je program semanticki ispravan
    private boolean semanticallyCorrect = true;

    // fleg koji oznacava da li je main metoda definisana
    private boolean mainMethodDefined = false;

    // koliko duboko smo u if-u ugnjezdeni
    private int ifLevel = 0;

    // fleg koji oznacava da li smo postavili return statement u metodi
    private boolean returnStatementInMethod = false;


    /****************************************************************************
     ****************************************************************************
     *****************         KONSTANTNE VREDNOSTI         *********************
     ****************************************************************************
     ****************************************************************************/

    private static final String MAIN_METHOD_NAME = "main";

    private static final String ARR_INDEX_TYPE_ERR = "Indeks niza mora biti tipa Int!";

    private static final String NOT_ARR_TYPE_ERR_MSG = "Tip mora biti niz!";

    private static final String INCOMPATIBLE_TYPE_ERR_MSG = "Tipovi nisu kompatabilni!";

    private static final String REF_TYPE_RELOP_ERR_MSG = "Nedozvoljen relacioni operator za reference";

    private static final String BREAK_STMT_ERR_MSG = "Break naredba se moze koristiti samo u petlji!";

    private static final String CONTINUE_STMT_ERR_MSG = "Continue naredba se moze koristiti samo u petlji!";

    private static final String READ_FUNC_PAR_ERR_MSG = "Funkcija read prima parametar koji je promenljiva, element niza ili polje klase!";

    private static final String READ_FUNC_TYPE_ERR_MSG = "Tip promenljive mora biti Int, Char ili Bool!";

    private static final String PRINT_FUNC_EXPR_ERR_MSG = "Parametar mora biti tipa Int, Char ili Bool!";

    private static final String FUNC_RETURN_TYPE_ERR_MSG = "Pogresna povratna vrednost u return naredbi!";

    private static final String NEW_CLASS_TYPE_ERR_MSG = "Naredba new ocekuje unutrasnju klasu!";

    private static final String MAIN_METHOD_UNDEFINED_ERR_MSG = "Metoda " + MAIN_METHOD_NAME + " nije definisana!";

    private static final String MAIN_METHOD_PARAM_NUM_ERR_MSG = "Metoda " + MAIN_METHOD_NAME + " ne sme imati argumente!";

    private static final String MAIN_METHOD_RETURN_TYPE_ERR_MSG = "Metoda " + MAIN_METHOD_NAME + " mora imati void povratni tip!";

    /****************************************************************************
     ****************************************************************************
     ****************        KONSTRUKTOR  I JAVNE METODE         ****************
     ****************************************************************************
     ****************************************************************************/

    public SemanticAnalyzer(){
        Tab.init();


        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
    }

    /**
     *  Vraca vrednost true ukoliko je program semanticki ispravan
     * @return da li je semanticki ispravno
     */
    public boolean isSemanticallyCorrect(){
        return semanticallyCorrect;
    }


    /****************************************************************************
     ****************************************************************************
     *******************          PRIVATNE METODE           *********************
     ****************************************************************************
     ****************************************************************************/


    /**
     * Proverava da li u trenutnom opsegu postoji simbol sa zadatim imenom
     * @param symName naziv simbola
     * @return vraca true ukoliko je simbol definisan u trenutnom opsegu, false u suprotnom
     */
    private boolean isDefinedInCurrentScope(String symName){
        return Tab.currentScope().findSymbol(symName) != null;
    }


    /**
     * Proverava da li je simbol sa zadatim imenom definisan(proverava od najugnjezdenijeg do najsireg opsega)
     * @param symName naziv simbola
     * @return vraca true ukoliko je simbol definisan, false u suprotnom
     */
    private boolean isDefined(String symName){
        return Tab.find(symName) != Tab.noObj;
    }


    private void logInfo(String message, SyntaxNode node){
        String lineInfo = "Na liniji (" + node.getLine() + "): ";
        System.out.println(lineInfo + message);
    }

    private void logError(String message, SyntaxNode node){
        String lineInfo = "Greska na liniji (" + node.getLine() + "): ";

        semanticallyCorrect = false;

        System.err.println(lineInfo + message);
    }

    private String formatAlreadyDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] je vec definisan!";
    }

    private String formatSymbolNotDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] nije definisan!";
    }

    private String formatAssignmentMessage(Struct leftSideType, Struct rightSideType, @Nullable Struct expectedType){
        String leftSideTypeName = symbolTypeToString(leftSideType.getKind());
        String rightSideTypeName = symbolTypeToString(rightSideType.getKind());
        String expectedTypeName = (expectedType != null)? symbolTypeToString(expectedType.getKind()) : null;

        String message = "Na levoj strani je tip : {" + leftSideTypeName + "}. Na desnoj strani je tip : {" + rightSideTypeName +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedTypeName + "}";

        return message;
    }


    private String formatWrongSymbolKindMessage(int actualSymbolKind, int expectedSymbolKind){
        return "Stvarna vrsta simbola je : {" + symbolKindToString(actualSymbolKind) + "}. Ocekivana vrsta simbola je : {" + symbolKindToString(expectedSymbolKind) + "}";
    }

    private String formatWrongTypeMessage(Struct expectedType, Struct actualType){
        String expectedTypeName = symbolTypeToString(expectedType.getKind());
        String actualTypeName = symbolTypeToString(actualType.getKind());

        if (expectedType.getKind() == Struct.Array)
            expectedTypeName += " of " + symbolTypeToString(expectedType.getElemType().getKind());

        if (actualType.getKind() == Struct.Array)
            actualTypeName += " of " + symbolTypeToString(actualType.getElemType().getKind());


        return "Tip je : {" + actualTypeName +"}. Ocekivan tip je : {" + expectedTypeName + "} ";
    }

    private String formatClassMemberDoesntExistMessage(Obj classObj, String fieldName){
        String className = classObj.getName();

        return "U klasi : {" + className + "} ne postoji polje sa nazivom : {" + fieldName + "} ";
    }

    private String formatWrongTypeMessage(int expectedType, int actualType){
        String expectedTypeName = symbolTypeToString(expectedType);
        String actualTypeName = symbolTypeToString(actualType);

        return "Tip je : {" + actualTypeName +"}. Ocekivan tip je : {" + expectedTypeName + "} ";
    }

    private String formatAssignmentMessage(String leftSideType, String rightSideType, @Nullable String expectedType){

        String message = "Na levoj strani je tip : {" + leftSideType+ "}. Na desnoj strani je tip : {" + rightSideType +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedType + "}";

        return message;
    }

    private String formatSymbolName(String symbolName){
        return "Simbol [" + symbolName + "]";
    }

    /**
     * Konvertuje kod vrste simbola u string
     * @param symbolKind kod vrste simbola
     * @return vrsta simbola kao string
     */
    private String symbolKindToString(int symbolKind){
        String retVal = "Unknown";
        switch(symbolKind){
            case Obj.Con :  retVal =  "Con";    break;
            case Obj.Elem : retVal = "Elem";    break;
            case Obj.Fld : retVal = "Fld";      break;
            case Obj.Meth : retVal = "Meth";    break;
            case Obj.Prog : retVal = "Prog";    break;
            case Obj.Type : retVal = "Type";    break;
            case Obj.Var : retVal = "Var";      break;
        }

        return retVal;
    }

    /**
     * Konvertuje kod tipa simbola u string
     * @param symbolType tip simbola predstavljen celobrojnom vrednoscu
     * @return vraca zeljeni tip simbola u vidu stringa
     */
    private String symbolTypeToString(int symbolType){
        String retVal = "Unknown";

        switch(symbolType){
            case Struct.Array : retVal = "Array";       break;
            case Struct.Char :  retVal = "Char";        break;
            case Struct.Int :   retVal = "Int";         break;
            case Struct.None :  retVal = "None";        break;
            case Bool :         retVal = "Bool";        break;
        }

        return retVal;
    }

    /**
     * Formatira opste informacije simbola za ispis ( naziv, vrsta, tip)
     * @param symbol zeljeni simbol za ispis
     * @return formatirana poruka sa opstim informacijama simbola
     */
    private String formatSymbolInfo(Obj symbol){
        String symbolNameFormat = formatSymbolName(symbol.getName());
        String symbolKind = symbolKindToString(symbol.getKind());
        String symbolType = symbolTypeToString(symbol.getType().getKind());

        String format = symbolNameFormat + " kind : " + symbolKind + ", type : " + symbolType;

        if (symbol.getType().getKind() == Struct.Array)
            format += " of " + symbolTypeToString(symbol.getType().getElemType().getKind());


        return format;

    }


    /**
     * Formatira ispis koji naglasava koliko parametara ima data metoda
     * @param funcObj simbol metode
     * @return formatirana poruka za ispis
     */
    private String formatFuncParNumIncorrectMessage(@NotNull Obj funcObj){

        if (funcObj.getLevel() == 0)
            return "Funkcija : {" + funcObj.getName() + "} ne prima parametre";
        else if (funcObj.getLevel() == 1)
            return "Funkcija : {" + funcObj.getName() + "} ima " + funcObj.getLevel() + " parametar";
        else
            return "Funkcija : {" + funcObj.getName() + "} ima " + funcObj.getLevel() + " parametra";
    }


    /**
     * Formatira ispis greske kada tip formalnog i stvarnog parametra metode nije kompatabilan pri dodeli
     * @param actualType tip stvarnog parametra metode
     * @param formalType tip formalnog parametra metode
     * @param paramNum redni broj parametra u metodi
     * @return formatirana poruka za ispis
     */
    private String formatFuncWrongParTypeMessage(@NotNull Struct actualType, @NotNull Struct formalType, int paramNum){
        String wrongTypeMsg = formatWrongTypeMessage(formalType.getKind(), actualType.getKind());

        return "Parametar broj : " + paramNum + ". " + wrongTypeMsg;
    }

    /**
     * Formatira ispis greske za slucaj kada nije navedena return naredba u funkciji
     * @param methodObj objekat metode
     * @return poruka za ispis
     */
    private String formatFuncMissingReturnStmtMessage(@NotNull Obj methodObj){
        return "Metoda : {" + methodObj.getName() + "} mora imati return naredbu!";
    }


    /**
     * Funkcija koja obmotava ponasanje za visit metode vezane za izraze
     * Moze biti maksimalno jedan od tipova operanda izostavljen, ali ne i oba ( smatra se greskom )
     * @param leftOperandType tip levog operanda, mooze biti izostavljen
     * @param rightOperandType tip desnog operanda, moze biti izostavljen
     * @return vraca tip koji je rezultat izraza
     */
    private Struct checkIfValidExpr(@Nullable  Struct leftOperandType, @Nullable Struct rightOperandType, SyntaxNode visitedNode){
        // rezultat dva izraza mora da je int
        Struct returnType = Tab.intType;

        if (leftOperandType == null && rightOperandType == null){
            System.err.println("FATAL ERROR!!! Function checkIfValidExpr has null left and right parameter");

            return returnType;
        }

        // postoji samo jedan operand i samo se vratimo
        if (rightOperandType == null)
            return leftOperandType;
        else if (leftOperandType == null)
            return rightOperandType;


        if (leftOperandType.equals(Tab.intType) && rightOperandType.equals(Tab.intType))
            return returnType;
        else if (!leftOperandType.equals(Tab.noType) || !rightOperandType.equals(Tab.noType))
            logError("Aritmeticki izrazi zahtevaju da operandi budu tipa Int", visitedNode);


        return returnType;
    }

    /**
     *  Formatira poruku za ispis koja navodi da nisu navedeni parametri za poziv funkcije
     * @param funcObj simbol funkcije
     * @return string poruke
     */
    private String formatMissingFuncActParsMessage(@NotNull Obj funcObj){
        // dohvatamo vrstu simbola
        int objKind = funcObj.getKind();


        if (objKind != Obj.Meth){
            System.err.println("FATAL ERROR!!! In function formatMissingFuncActParsMessage. Obj kind is : " + symbolKindToString(objKind));

            return "";
        }

        // naziv fje
        String funcName = funcObj.getName();

        return "Naveden je poziv funkcije {" + funcName + "} bez stvarnih argumenata!";
    }


    /**
     * Proverava da li se tipovi formalnih poklapaju sa tipovima stvarnih argumenata
     * @param actualParsList lista stvarnih argumenata
     * @param funcObj simbol funkcije
     * @param syntaxNode referentni cvor stabla u slucaju da treba da se ispise greska
     * @return fleg koji oznacava da li je sve u redu ili ne
     */
    private boolean checkFuncFormalAndActualPars(@NotNull StructLinkedList actualParsList, @NotNull Obj funcObj, @NotNull SyntaxNode syntaxNode){
        // proverimo da li je broj elemenata liste jednak broju formalnih parametara fje
        // ukoliko nije jednostavno se vratimo nazad
        if (actualParsList.size() != funcObj.getLevel()){
            logError(formatFuncParNumIncorrectMessage(funcObj), syntaxNode);

            return false;
        }

        // kolekcija svih lokalnih simbola fje
        // prvih N simbola su formalni parametri funkcije
        Iterator<Obj> formalParsIt = funcObj.getLocalSymbols().iterator();

        // broj formalnih parametara
        int parNum = funcObj.getLevel();

        boolean allOk = true;

        for(int i = 0; i <  parNum; ++i){
            // formalni tip parametra
            Struct formParType = formalParsIt.next().getType();

            // stvarni tip parametra
            Struct actualParType = actualParsList.get(i);

            // ako nije moguce dodeliti onda treba ispisati gresku
            if (!actualParType.assignableTo(formParType)) {
                logError(formatFuncWrongParTypeMessage(actualParType, formParType, i + 1), syntaxNode);

                allOk = false;
            }
        }

        return allOk;
    }


    /****************************************************************************
     ****************************************************************************
     *******************       VISIT METODE ZA PROGRAM       ********************
     **********************    KONSTANTE I PROMENLJIVE     **********************
     ****************************************************************************
     ****************************************************************************/

    @Override
    public void visit(Program program) {

        // pronalazimo simbol programa u tabeli simbola
        Obj programObj = Tab.find(program.getProgramName().getProgramName());

        logInfo(formatSymbolInfo(programObj), program);

        // ako main metoda nije definisana, ispisemo gresku
        if (!mainMethodDefined)
            logError(MAIN_METHOD_UNDEFINED_ERR_MSG, program);

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


            // postavljamo tip podatka sa desne strane i citamo vrednost
            if (constValue instanceof NumConst){
                rightSide = Tab.intType;

                value = ((NumConst) constValue).getNumConst();
            }else if (constValue instanceof CharConst){
                rightSide = Tab.charType;

                value = ((CharConst) constValue).getCharConst();
            }else if (constValue instanceof BoolConst){
                rightSide = boolType;

                value = ((BoolConst) constValue).getBoolConst()  ? 1 : 0;
            }

            // ispitujemo da li je dodela moguca
            if (type.assignableTo(rightSide)){
                Obj constObj = Tab.insert(Obj.Con, constName, type);

                logInfo(formatSymbolInfo(constObj), constDefinition);

                constObj.setAdr(value);
            }else{
                String assigmentErrorMsg = formatAssignmentMessage(currentTypeObj.getName(), symbolTypeToString(rightSide.getKind()), currentTypeObj.getName());

                logError(assigmentErrorMsg, constDefinition);
            }


        }
    }

    @Override
    public void visit(Type type){

        // naziv tipa
        String typeName = type.getTypeName();

        // prvo postavljamo da nismo naisli na simbol tipa
        currentTypeObj = Tab.noObj;

        currentType = type;

        // ako nismo pronasli simbol sa zadatim nazivom
        // vratimo se
        if (!isDefined(typeName)){
            logError(formatSymbolNotDefinedMessage(typeName), type);

            return;
        }


        // simbol je vec definisan,sada treba videti da li je u pitanju tip
        Obj typeObj = Tab.find(typeName);

        // ako je simbol vrste tip, onda zapamtimo simbol
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
            logError("Simbol[" + varName + "] je vec definisan u trenutnom opsegu!", varDeclDefinition);
            return;
        }else if (currentMethod != Tab.noObj && varName.equals(currentMethod.getName())){
            logError(formatSymbolName(varName) + " je vec definisan u trenutnom opsegu!", varDeclDefinition);
            return;
        }


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

    /****************************************************************************
     * **************************************************************************
     ***************     VISIT METODE ZA DEFINISANJE METODA     *****************
     ***************************    I PARAMETARA   ******************************
     ****************************************************************************/

    @Override
    public void visit(MethodName methodName){
        String name = methodName.getMethodName();

        // uzimamo da je podrazumevano noObj
        currentMethod = Tab.noObj;

        // inicijalizujemo broj formalnih parametara na 0
        currentMethodFormParNum = 0;

        // resetujemo fleg za proveru return naredbe u metodi
        returnStatementInMethod = false;

        // ako je vec definisano onda prijavimo gresku
        if (isDefined(name))
            logError(formatAlreadyDefinedMessage(name), methodName);
        else
            currentMethod = Tab.insert(Obj.Meth, name, currentTypeStruct);

        if (currentMethod != Tab.noObj)
            mainMethodDefined = name.equals(MAIN_METHOD_NAME);

        // otvorimo opseg
        Tab.openScope();

    }

    @Override
    public void visit(MethodDecl methodDecl){

        // uvezujemo lokalne simbole samo ukoliko je
        // metoda dodata u tabelu simbola
        if (currentMethod != Tab.noObj) {
            Tab.chainLocalSymbols(currentMethod);

            // postavljamo broj formalnih parametara
            currentMethod.setLevel(currentMethodFormParNum);

            // proveravamo da li je definisana main metoda
            boolean isMainMethod = currentMethod.getName().equals(MAIN_METHOD_NAME);

            // proveravamo ispravnost definicije main metode
            // main metoda ne sme imati argumente i mora imati povratni tip void(Tab.noType)
            if (isMainMethod && currentMethodFormParNum != 0)
                logError(MAIN_METHOD_PARAM_NUM_ERR_MSG, methodDecl);
            else if (isMainMethod && currentMethod.getType() != Tab.noType)
                logError(MAIN_METHOD_RETURN_TYPE_ERR_MSG, methodDecl);
            else
                logInfo(formatSymbolInfo(currentMethod), methodDecl);

            // ako ima povratni tip metoda mora vracati rezultat, mozemo navesti kao gresku
            // ili samo obavestimo da nije naveo da svi putevi vracaju vrednost
            if (currentMethod.getType() != Tab.noType && !returnStatementInMethod)
                logError(formatFuncMissingReturnStmtMessage(currentMethod), methodDecl);
        }

        // vratimo currentMethod na pocetnu vrednost
        currentMethod = Tab.noObj;

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
        // ili ima naziv isti kao i metoda
        if (isDefinedInCurrentScope(formParName) || formParName.equals(currentMethod.getName())){
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

        // uvecavamo broj formalnih parametara
        currentMethodFormParNum++;

        // informativno logovanje
        logInfo(formatSymbolInfo(paramObj), formParDecl);
    }

    /****************************************************************************
     ****************************************************************************
     *************     VISIT METODE ZA PRISTUP PROMENLJIVAMA     ****************
     ****************************************************************************
     ****************************************************************************/


    @Override
    public void visit(Designator designator){
        designator.obj = designator.getDesignatorRepeatList().obj;
    }

    @Override
    public void visit(DesignatorInitialName designatorInitialName){
        //logInfo(designatorInitialName.getDesignatorName() +" initial", designatorInitialName);

        // nadjemo objekat sa datim nazivom
        designatorObj = Tab.find(designatorInitialName.getDesignatorName());

        // ako ne postoji prijavimo gresku
        if (designatorObj == Tab.noObj)
            logError(formatSymbolNotDefinedMessage(designatorInitialName.getDesignatorName()), designatorInitialName);
    }

    @Override
    public void visit(DesignatorRepeatListDeclared designatorRepeatListDeclared){
        designatorRepeatListDeclared.obj = Tab.noObj;


        Obj designatorObj = designatorRepeatListDeclared.getDesignatorRepeatList().obj;

        DesignatorRepeat designatorRepeat = designatorRepeatListDeclared.getDesignatorRepeat();

        // da li je u pitanju pristup polju ili elementu niza
        if (designatorRepeat instanceof DesignatorRepeatField){
            boolean isClass = designatorObj.getType().getKind() == Struct.Class;
            String message = "";

            String fieldName = ((DesignatorRepeatField) designatorRepeat).getFieldName();

            // pristup preko tacke je moguc samo za podatke koji su tipa Class
            // ukoliko nisu tipa class, jednostavno treba da prijavimo gresku i vratimo se
            if (!isClass){
                message = formatWrongTypeMessage(Struct.Class, designatorObj.getType().getKind());
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // dohvatimo sve clanove klase(funkcije i metode)
            SymbolDataStructure classMembers = designatorObj.getType().getMembers();

            // trazimo simbol sa datim nazivom
            Obj fieldObj = classMembers.searchKey(fieldName);

            // ukoliko dato polje ne postoji u klasi
            // javiti gresku
            if (fieldObj == null){
                message = formatClassMemberDoesntExistMessage(designatorObj, fieldName);
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // nasli smo dato polje, samo taj objekat dodelimo cvoru AST-a
            designatorRepeatListDeclared.obj = fieldObj;

        }else{ // slucaj kada je DesignatorRepeat zapravo DesignatorRepeatExpr(pristup elementu niza)
            Expr arraySize = ((DesignatorRepeatExpr)designatorRepeatListDeclared.getDesignatorRepeat()).getExpr();

            // ako indeks niza nije int, prijavimo gresku
            if (!arraySize.struct.equals(Tab.intType)){
                logError(ARR_INDEX_TYPE_ERR, designatorRepeatListDeclared);

                return;
            }

            // ako je pristup elementu niza, a promenljiva nije tipa niz, prijavimo gresku
            if (designatorObj.getType().getKind() != Struct.Array){
                logError(NOT_ARR_TYPE_ERR_MSG, designatorRepeatListDeclared.getDesignatorRepeatList());
            }

            // naziv elementa
            String elementName = designatorObj.getName();

            // sam tip elementa
            // posto je tip designatorObj niz nekog tipa, treba da dohvatimo taj tip
            Struct elementType = designatorObj.getType().getElemType();

            //objekat koji je tipa element niza
            designatorRepeatListDeclared.obj = new Obj(Obj.Elem, elementName, elementType);
        }
    }


    @Override
    public void visit(DesignatorRepeatListEpsilon designatorRepeatListEpsilon){
        designatorRepeatListEpsilon.obj = designatorObj;
    }

    /***********************************************************************************
     ***********************************************************************************
     ********************       VISIT METODE NAREDBI DESIGNATORA       *****************
     ***********************************************************************************
     ***********************************************************************************/


    @Override
    public  void visit(DesignatorStatementAssign assignStatement){
        // promenljiva kojoj dodeljujemo vrednost
        Obj designatorObj = assignStatement.getDesignator().obj;

        // tip rezultata izraza
        Struct exprType = assignStatement.getExpr().struct;

        // tip promenljive
        Struct designatorType = designatorObj.getType();

        // ukoliko nisu kompatabilni pri dodeli prijavi se greska
        if (!designatorType.assignableTo(exprType)){
            logError(formatWrongTypeMessage(designatorType, exprType), assignStatement.getExpr());
        }
    }


    @Override
    public void visit(DesignatorStatementActualPars designatorFuncCall){
        Obj funcObj = designatorFuncCall.getDesignator().obj;

        if (funcObj.getKind() != Obj.Meth){
            logError(formatWrongSymbolKindMessage(funcObj.getKind(), Obj.Meth), designatorFuncCall.getDesignator());

            return;
        }

        checkFuncFormalAndActualPars(designatorFuncCall.getOptActPars().structlinkedlist, funcObj, designatorFuncCall);
    }

    @Override
    public void visit(DesignatorStatementIncrement statementIncrement){
        // objekat promenljive
        Obj designatorObj = statementIncrement.getDesignator().obj;

        // tip promenljive
        Struct designatorType = designatorObj.getType();

        // ako nije tipa int, prijaviti gresku
        if (designatorType.getKind() !=  Struct.Int){
            logError(formatWrongSymbolKindMessage(designatorType.getKind(), Struct.Int), statementIncrement.getDesignator());
        }
    }


    @Override
    public void visit(DesignatorStatementDecrement statementDecrement){
        // objekat promenljive
        Obj designatorObj = statementDecrement.getDesignator().obj;

        // tip promenljive
        Struct designatorType = designatorObj.getType();

        // ako nije tipa int, prijaviti gresku
        if (designatorType.getKind() !=  Struct.Int){
            logError(formatWrongSymbolKindMessage(designatorType.getKind(), Struct.Int), statementDecrement.getDesignator());
        }
    }


    /**********************************************************************
     **********************************************************************
     ********************       VISIT METODE IZRAZA       *****************
     **********************************************************************
     ********************************************************************/

    @Override
    public void visit(Expr expr){
        // cvor koji predstavlja unarni minus
        OptMinus optMinus = expr.getOptMinus();

        // u pocetku stavljamo da je expr noType
        expr.struct = Tab.noType;

        // tip podatka iz cvora Term
        Struct leftOperandType = expr.getTerm().struct;

        // da li je naveden unarni operator
        // ako jeste onda term mora biti integer
        if (optMinus instanceof OptMinusDeclared){

            if (!leftOperandType.equals(Tab.intType)){
                logError("Operator unarni - ocekuje tip Int", expr);

                return;
            }
        }

        Struct rightOperandType = expr.getAddopTermList().struct;


        expr.struct = checkIfValidExpr(leftOperandType, rightOperandType, expr.getTerm());
    }

    @Override
    public void visit(AddopTerm addopTerm){
        addopTerm.struct = addopTerm.getTerm().struct;
    }


    @Override
    public void visit(AddopTermListEpsilon addopTermListEpsilon){
        addopTermListEpsilon.struct = null;
    }

    @Override
    public void visit(AddopTermListDeclared addopTermListDeclared){
        // tip levog operanda
        Struct leftOperandType = addopTermListDeclared.getAddopTermList().struct;

        // tip desnog operanda
        Struct rightOperandType = addopTermListDeclared.getAddopTerm().struct;

       addopTermListDeclared.struct =  checkIfValidExpr(leftOperandType, rightOperandType, addopTermListDeclared.getAddopTermList());
    }


    @Override
    public void visit(Term term){
        // tip levog operanda
        Struct leftOperandType = term.getFactor().struct;

        // tip desnog operanda
        Struct rightOperandType = term.getMulopFactorList().struct;

        term.struct = checkIfValidExpr(leftOperandType, rightOperandType, term);

    }

    @Override
    public void visit(MulopFactor mulopFactor){
        mulopFactor.struct = mulopFactor.getFactor().struct;
    }

    @Override
    public void visit(MulopFactorListEpsilon mulopFactorListEpsilon){
        mulopFactorListEpsilon.struct = null;
    }

    @Override
    public void visit(MulopFactorListDeclared mulopFactorListDeclared){
        // sve je isto kao i za addopTermListDeclared
        mulopFactorListDeclared.struct = Tab.noType;

        Struct leftOperandType = mulopFactorListDeclared.getMulopFactorList().struct;

        Struct rightOperandType = mulopFactorListDeclared.getMulopFactor().struct;

        mulopFactorListDeclared.struct = checkIfValidExpr(leftOperandType, rightOperandType, mulopFactorListDeclared.getMulopFactorList());
    }


    @Override
    public void visit(FactorFuncCallOrVar factorFuncCallOrVar){

        // dohvatamo objekat
        Obj funcObj = factorFuncCallOrVar.getDesignator().obj;

        factorFuncCallOrVar.struct = funcObj.getType();

        // cvor koji sadrzi parametre fje
        OptParenthesesActPars funcPars = factorFuncCallOrVar.getOptParenthesesActPars();

        // ako nije poziv f-je, a ima prosledjene parametre
        if (funcObj.getKind() != Obj.Meth && funcPars instanceof  OptParenthesesActParsDeclared){
            logError(formatWrongSymbolKindMessage(funcObj.getKind(), Obj.Meth), factorFuncCallOrVar);

            return;
        }

        // ako nije metoda, jednostavno se vratimo
        if (funcObj.getKind() != Obj.Meth)
            return;


        // ukoliko jeste metoda a parametri nisu postavljeni onda treba da prijavimo gresku
        if (funcPars instanceof OptParenthesesActParsEpsilon){
            logError(formatMissingFuncActParsMessage(funcObj), factorFuncCallOrVar);

            return;
        }


        // dohvatimo listu stvarnih parametara
        StructLinkedList actualParsList = factorFuncCallOrVar.getOptParenthesesActPars().structlinkedlist;

        // proverimo da li se tipovi stvarnih i formalnih argumenata poklapaju
        checkFuncFormalAndActualPars(actualParsList, funcObj, factorFuncCallOrVar);
    }

    @Override
    public  void visit(FactorConst factorConst){
        factorConst.struct = Tab.noType;

        ConstValue constValue = factorConst.getConstValue();

        if (constValue instanceof NumConst)
            factorConst.struct = Tab.intType;
        else if (constValue instanceof CharConst)
            factorConst.struct = Tab.charType;
        else
            factorConst.struct = boolType;

    }

    @Override
    public void visit(FactorNew factorNew){


        factorNew.struct = currentTypeStruct;

        // tip rezultata izraza, ukolko je naveden dobicemo vrednost != null
        Struct exprType = factorNew.getOptArrExpr().struct;

        if (exprType == null){
            // exprType je null, znaci pravimo novi objekat klase
            // moramo da vidimo da li je tip klasa
            if (currentTypeStruct.getKind() != Struct.Class){
                logError(NEW_CLASS_TYPE_ERR_MSG, factorNew);
            }

            return;
        }


        factorNew.struct = new Struct(Struct.Array, currentTypeStruct);

        // nije u pitanju kreiranje klase vec niza elemenata
        // proveravamo da li je velicina niza int
        if (exprType != Tab.intType){
            logError(formatWrongTypeMessage(Tab.intType, exprType), factorNew);
        }
    }

    @Override
    public void visit(FactorExpr factorExpr) {
        factorExpr.struct = factorExpr.getExpr().struct;
    }


    @Override
    public void visit(OptExprDeclared optExpr){
        optExpr.struct = optExpr.getExpr().struct;
    }

    @Override
    public void visit(OptExprEpsilon optExpr){
        optExpr.struct = Tab.noType;
    }



    /**********************************************************************
     **********************************************************************
     ********************     VISIT METODE LOGICKIH      *****************
     ****************************     IZRAZA    **************************
     ********************************************************************/

    @Override
    public void visit(Condition condition){

        // tip levog operanda
        Struct leftOperandType = condition.getCondTerm().struct;

        // tip desnog operanda
        Struct rightOperandType = condition.getConditionRepeatList().struct;


        if (leftOperandType != boolType || rightOperandType != boolType)
            condition.struct = Tab.noType;
        else
            condition.struct = boolType;
    }

    @Override
    public void visit(ConditionRepeatListDeclared conditionRepeatList){
        conditionRepeatList.struct = conditionRepeatList.getConditionRepeat().struct;
    }

    @Override
    public void visit(ConditionRepeatListEpsilon conditionRepeatListEpsilon){
        conditionRepeatListEpsilon.struct = null;
    }

    @Override
    public void visit(ConditionRepeat conditionRepeat){
        conditionRepeat.struct = conditionRepeat.getCondTerm().struct;
    }

    @Override
    public void visit(CondTerm condTerm){
        // tip levog operanda
        Struct leftOperandType = condTerm.getCondFact().struct;

        // tip desnog operanda
        Struct rightOperandType = condTerm.getCondFactRepeatList().struct;

        // rezultat operacija mora da je bool
        condTerm.struct = boolType;

        if (rightOperandType == null)
            return;

        // tipovi nisu kompatabilni
        if (!leftOperandType.compatibleWith(rightOperandType)){
            logError(INCOMPATIBLE_TYPE_ERR_MSG,condTerm.getCondFact().getExpr());

            return;
        }
    }

    @Override
    public void visit(CondFact condFact){
        // tip levog operanda
        Struct leftOperandType = condFact.getExpr().struct;

        // tip desnog operanda
        Struct rightOperandType = condFact.getOptRelopExpr().struct;

        condFact.struct = boolType;

        // ne postoji desni operand
        if (rightOperandType == null)
            return;


        // tipovi nisu kompatabilni
        if (!leftOperandType.compatibleWith(rightOperandType)){
            logError(INCOMPATIBLE_TYPE_ERR_MSG,condFact.getExpr());

            return;
        }


        Relop relop = ((OptRelopExprDeclared)condFact.getOptRelopExpr()).getRelop();

        boolean areRefType = leftOperandType.compatibleWith(Tab.nullType) && rightOperandType.compatibleWith(Tab.nullType);

        boolean relopIsEqualsOrNotEquals = (relop instanceof RelopEquals) || (relop instanceof  RelopNotEquals);

        // ako je referencijalni tip i ako nije operator == ili != onda je to greska
        if (areRefType && !relopIsEqualsOrNotEquals){
            logError(REF_TYPE_RELOP_ERR_MSG, condFact);
        }
    }

    @Override
    public void visit(OptRelopExprDeclared relopExpr){
        relopExpr.struct = relopExpr.getExpr().struct;
    }

    @Override
    public void visit(CondFactRepeatListDeclared condFactList){
        // samo propagiramo bool tip
        condFactList.struct = boolType;
    }

    @Override
    public void visit(CondFactRepeatListEpsilon condFactEpsilonList){
        condFactEpsilonList.struct = null;
    }


    @Override
    public void visit(OptArrExprDeclared arrSizeExpr){
        arrSizeExpr.struct = arrSizeExpr.getExpr().struct;
    }

    @Override
    public void visit(OptArrExprEpsilon epsilonArrExpr){
        epsilonArrExpr.struct = null;
    }

    /**********************************************************************
     **********************************************************************
     ****************   VISIT METODE Stvarnih Parametara  *****************
     **********************************************************************
     ********************************************************************/

    @Override
    public void visit(OptParenthesesActParsEpsilon noParams){

        noParams.structlinkedlist = new StructLinkedList();

       // logInfo("Visited OptParanthesesActParsEpsilon", noParams);
    }

    @Override
    public void visit(OptParenthesesActParsDeclared params){
        params.structlinkedlist = params.getOptActPars().structlinkedlist;
    }

    @Override
    public void visit(OptActParsDeclared actualPars){
        actualPars.structlinkedlist = actualPars.getActPars().structlinkedlist;
    }

    @Override
    public void visit(OptActParsEpsilon noPars){
        // epsilon cvor, ovde samo kreiramo listu
        noPars.structlinkedlist = new StructLinkedList();
    }

    @Override
    public void visit(ActPars actPars){
        // dohvatimo parametra iz izraza
        Struct paramType = actPars.getExpr().struct;

        // dodamo tip stvarnog parametra u listu na pocetak
        // iz razloga sto ce se prvi element obici tek na kraju
        // a ostali elementi ce se obici po normalnom redosledu
        // stoga prvi treba da stavimo na pocetak
        (actPars.structlinkedlist = actPars.getActParsRepeatList().structlinkedlist).addFirst(paramType);
    }

    @Override
    public void visit(ActParsRepeatListDeclared actParsRepeatListDeclared){
        // propagiramo listu
        actParsRepeatListDeclared.structlinkedlist = actParsRepeatListDeclared.getActParsRepeatList().structlinkedlist;

        // dodamo stvarni argument na kraj liste
        actParsRepeatListDeclared.structlinkedlist.add(actParsRepeatListDeclared.getActParsRepeat().struct);
    }


    @Override
    public void visit(ActParsRepeatListEpsilon actParsRepeatListEpsilon){
        actParsRepeatListEpsilon.structlinkedlist = new StructLinkedList();
    }

    @Override
    public void visit(ActParsRepeat actualPars){
        actualPars.struct = actualPars.getExpr().struct;
    }

    /**********************************************************************
     **********************************************************************
     ******************      VISIT METODE NAREDBI     *********************
     **********************************************************************
     ********************************************************************/


    @Override
    public void visit(DoWhileBegin loopBegin){

        // uvecavamo koliko smo ugnjezdeni u petlji
        ++insideLoop;
    }

    @Override
    public void visit(DoWhileEnd loopEnd){
        // napustamo jednu petlju, smanjimo ugnjezdavanje
        --insideLoop;
    }


    @Override
    public void visit(BreakStatement breakStatement){
        // break naredba sme biti samo unutar petlje
        // ukoliko nismo unutar petlje prijavimo gresku
        if (insideLoop == 0)
            logError(BREAK_STMT_ERR_MSG, breakStatement);
    }

    @Override
    public void visit(ContinueStatement continueStatement){
        // isto kao i break naredba
        if (insideLoop == 0)
            logError(CONTINUE_STMT_ERR_MSG, continueStatement);
    }

    @Override
    public void visit(ReturnStatement returnStatement){
        // ako nije metoda definisana vratimo se nazad
        if (currentMethod == Tab.noObj)
            return;

        // dohvatimo tip povratne vrednosti
        Struct returnValueType = returnStatement.getOptExpr().struct;

        // ukoliko tipovi nisu jednaki, onda prijavimo gresku
        if (!returnValueType.compatibleWith(currentMethod.getType()))
            logError(FUNC_RETURN_TYPE_ERR_MSG + " " + formatWrongTypeMessage(currentMethod.getType(), returnValueType), returnStatement);


        returnStatementInMethod = returnStatementInMethod || (ifLevel == 0);

    }

    @Override
    public void visit(ReadStatement readStatement){
        // promenljiva u koju treba upisati rezultat read fje
        Obj designatorObj = readStatement.getDesignator().obj;

        // vrsta promenljive
        int designatorKind = designatorObj.getKind();

        // tip promenljive
        Struct designatorType = designatorObj.getType();

        // promenljiva nije odgovarajuce vrste
        if (!(designatorKind == Obj.Var || designatorKind == Obj.Fld || designatorKind == Obj.Elem)){
            logError(READ_FUNC_PAR_ERR_MSG, readStatement);

            return;
        }

        if (!(designatorType == boolType || designatorType == Tab.intType || designatorType == Tab.charType))
            logError(READ_FUNC_TYPE_ERR_MSG, readStatement);
    }

    @Override
    public void visit(PrintStatement printStatement){
        // tip rezultata izraza
        Struct exprType = printStatement.getExpr().struct;

        // dozvoljeni tip izraza je bool, int i char
        if (exprType != boolType && exprType != Tab.intType && exprType != Tab.charType) {
            logError(PRINT_FUNC_EXPR_ERR_MSG, printStatement);

            return;
        }
    }

    @Override
    public void visit(IfStart ifStart){
        // uvecavamo ugnjezdenje if-a
        ++ifLevel;
    }

    @Override
    public void visit(IfElseStatement ifElseStatement){
        // zavrsili smo sa if-om, smanjiti nivo ugnezdavanja
        --ifLevel;
    }

    @Override
    public void visit(IfStatement ifStatement){
        // zavrsili smo sa if-om, smanjiti nivo ugnezdavanja
        --ifLevel;
    }
}