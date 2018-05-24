package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import ba140645d.mjcompiler.utilities.SymbolTableVisitor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

import java.util.Collection;
import java.util.HashMap;

public class CodeGenerator extends VisitorAdaptor {


    private static final String ORD_METH_NAME = "ord";

    private static final String CHR_METH_NAME = "chr";

    private static final String LEN_METH_NAME = "len";

    private static final String MAIN_METH_NAME = "main";


    private Obj designatorObj = Tab.noObj;


    // svi simboli programa
    private HashMap<String, Obj> programSymbols = new HashMap<>();

    /**
     * Generise kod za predefinisane metode
     * Predefinisane metode su : ord, chr, len
     */
    private void generateCodeForPredefinedMethods(){
        // simboli iz scope-a univerzuma
        Collection<Obj> universeSymbols = Tab.currentScope.getLocals().symbols();

        // kod universe scope-a postoje predefinisani tipovi podataka, metode
        // i naziv programa, od interesa su nam samo predefinisane metode
        // i za njih zelimo da generisemo kod
        universeSymbols.forEach((universeSymbol)->{
            // ako nije metoda, ne obradjujemo
            if (universeSymbol.getKind() != Obj.Meth)
                return;

            // postavljamo adresu funkcije na trenutni pokazivac PC-a
            universeSymbol.setAdr(Code.pc);

            // instrukcija koja frsi pamcenje stack pointera, function pointera i
            // inicijalizuje prostor za parametre i lokalne promenljive
            Code.put(Code.enter);

            // format enter instrukcije je enter paramSize, Lsize gde je Lsize ukupan prostor
            // koji zauzimaju

            // prvo ide broj parametara
            Code.put(universeSymbol.getLevel());

            // pa ukupan broj lokalnih simbola(parametri + lokalne promenljive)
            Code.put(universeSymbol.getLocalSymbols().size());

            // ucitamo vrednost parametra u expr stek
            Code.put(Code.load_n);

            // ukoliko je metoda len u pitanju, onda treba pored gore navedenih akcija
            // takodje i dodati instrukciju arraylength, koja ce nam dati duzinu samog niza
            if (universeSymbol.getName().equals("len"))
                Code.put(Code.arraylength);

            // kao i kod x86 jednostavno uklonimo sve lokalne promenljive sa steka
            // i bp(ovde je to fp) vratimo na prethodnu vrednost
            Code.put(Code.exit);

            // narebom return napustamo funkciju  pc = pop();
            Code.put(Code.return_);
        });
    }

    @Override
    public void visit(Program program){
        Tab.closeScope();
    }

    @Override
    public void visit(ProgramName programName){

        // generisanje koda za predefinisane metode
        generateCodeForPredefinedMethods();

        Obj programObj = Tab.find(programName.getProgramName());

        // simboli iz scope-a programa
        Collection<Obj> programSymbols = programObj.getLocalSymbols();

        // otvaranje opsega zarad lakseg nalazenja simbola
        Tab.openScope();

        // dodavanje simbola programa u trenutni opseg
        // moramo da navedemo koliko prostora treba da zauzimaju globalne promenljive, velicina
        // se izrazava u recima(jedna rec je 4 bajta)
        programSymbols.forEach((symbol)->{
            Tab.currentScope().addToLocals(symbol);
            if (symbol.getKind() == Obj.Var)
                Code.dataSize++;
        });
    }


    @Override
    public void visit(MethodName method){
        String methodName = method.getMethodName();

        Obj methodObj = Tab.find(methodName);

        // ukoliko je u pitanju main metoda, moramo navesti koja je njena adresa
        if (methodName.equals(MAIN_METH_NAME)){
            Code.mainPc = Code.pc;
        }

        // namestamo adresu metode na trenutnu vrednost pc
        methodObj.setAdr(Code.pc);

        // broj formalnih parametara
        int paramNum = methodObj.getLevel();

        // ukupan broj lokalnih simbola u metodi( parametri + promenljive)
        int localSymbolNum = methodObj.getLocalSymbols().size();

        // kod za inicijalizaciju metode pri ulasku, instrukcija ima 2 parametra
        // broj parametara i ukupan broj lokalnih simbola
        Code.put(Code.enter);

        // prvo ide broj parametara
        Code.put(paramNum);

        // pa zatim i ukupan broj simbola
        Code.put(localSymbolNum);


        //  otvorimo scope zarad lakse pretrage simbola
        Tab.openScope();

        // dohvatimo sve simbole metode
        Collection<Obj> methodSymbols = methodObj.getLocalSymbols();

        // dodamo ih u scope
        for (Obj methodSymbol : methodSymbols) {
            Tab.currentScope.addToLocals(methodSymbol);
        }

    }

    @Override
    public void visit(MethodDecl methodDecl){
        // dohvatimo ime metode
        String methodName = methodDecl.getMethodName().getMethodName();

        // dohvatimo objekat(simbol) metode
        Obj methodObj = Tab.find(methodName);

        // ciscenje steka od lokalnih simbola
        Code.put(Code.exit);

        // vracanje nazad
        Code.put(Code.return_);
    }

    @Override
    public void visit(Expr expr){

    }


    @Override
    public void visit(Term term){

    }

    @Override
    public void visit(AddopTermList addopTermList){

    }

    @Override
    public void visit(AddopTerm addopTerm){

    }

    @Override
    public void visit(FactorFuncCallOrVar factor){
        Obj designatorObj = factor.getDesignator().obj;


        if (designatorObj.getKind() == Obj.Meth) {
            Code.put(Code.call);
            Code.put2(designatorObj.getAdr());
        }
    }


    @Override
    public void visit(FactorConst factorConst){
        ConstValue constValue = factorConst.getConstValue();

        // objekat konstante
        Obj constObj = null;

        // proverimo kog je tipa konstanta i kreiramo odgovarajuci simbol
        if (constValue instanceof NumConst)
            constObj = new Obj(Obj.Con, "",Tab.intType, ((NumConst) constValue).getNumConst(), 0);
        else if (constValue instanceof  CharConst)
            constObj = new Obj(Obj.Con, "", Tab.charType, ((CharConst) constValue).getCharConst(), 0);
        else if (constValue instanceof  BoolConst)
            constObj = new Obj(Obj.Con, "", SemanticAnalyzer.boolType, ((BoolConst) constValue).getBoolConst() ? 1 : 0, 0 );

        Code.load(constObj);
    }


    @Override
    public void visit(FactorNew factorNew){

    }


    @Override
    public void visit(FactorExpr factorExpr){

    }


    /****************************************************************************
     ****************************************************************************
     *************     VISIT METODE ZA ARITMETICKE OPERACIJE     ****************
     ****************************************************************************
     ****************************************************************************/


    @Override
    public void visit(AddopAddition additionOp){

    }

    @Override
    public void visit(AddopSubtraction subtractionOp){

    }


    @Override
    public void visit(MulopMultiplication multiplicaitonOp){

    }

    @Override
    public void visit(MulopDivision divisionOp){

    }

    @Override
    public void visit(MulopModuo moduoOp){

    }


    /****************************************************************************
     ****************************************************************************
     *************     VISIT METODE ZA PRISTUP PROMENLJIVAMA     ****************
     *****************************    I FUNKCIJAMA    ***************************
     ****************************************************************************/

    @Override
    public void visit(Designator designator){
        // u pitanju je B nivo, tu imamo samo promenljive i nizove
        // nema klasa, stoga ne moramo da imamo neki poseban kod za pristup poljima objekata
        designator.obj = Tab.find(designator.getDesignatorInitialName().getDesignatorName());
    }

    @Override
    public void visit(DesignatorInitialName designatorInitialName){
        String designatorName = designatorInitialName.getDesignatorName();

        designatorObj = Tab.find(designatorName);


        // da li je u pitanju designator statement, ako jeste onda ne treba da ucitavamo
        // vrednost designatora
        boolean isDesignatorStatementAssignPredecessor = designatorInitialName.getParent().getParent() instanceof DesignatorStatementAssign;
        // ako nije klasa samo load-ujemo
        if (designatorObj.getKind() != Obj.Meth && !isDesignatorStatementAssignPredecessor)
            Code.load(designatorObj);
    }

    @Override
    public void visit(DesignatorRepeatExpr designatorArray){

    }


    /****************************************************************************
     ****************************************************************************
     *********************     VISIT METODE ZA NAREDBE     **********************
     ****************************************************************************
     ****************************************************************************/


    @Override
    public void visit(PrintStatement printStatement){
       // Code.put(Code.call);
        Code.put(Code.print);
    }
}