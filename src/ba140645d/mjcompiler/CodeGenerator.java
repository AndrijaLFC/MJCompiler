package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import ba140645d.mjcompiler.utilities.SymbolTableVisitor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

import java.util.Collection;
import java.util.HashMap;

public class CodeGenerator extends VisitorAdaptor {


    private static final String ORD_METH_NAME = "ord";

    private static final String CHR_METH_NAME = "chr";

    private static final String LEN_METH_NAME = "len";

    private static final String MAIN_METH_NAME = "main";


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
        programSymbols.forEach((symbol)-> Tab.currentScope().addToLocals(symbol));

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
}