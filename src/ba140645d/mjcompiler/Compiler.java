package ba140645d.mjcompiler;

import ba140645d.log4j.Log4JUtil;
import ba140645d.mjcompiler.ast.Program;
import ba140645d.mjcompiler.ast.SyntaxNode;
import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

import java.io.*;


public class Compiler{

    /**********************************************
     ********* COMMAND FOR AST GENERATION **********
     * **********************************************/
    // First we need to position ourselves to the folder $PROJ_FOLDER\src
    // That can be done with cd $PROJ_FOLDER\src
    // Then we use the command to generate in directory ast all the nodes of the AST
    // java -jar .\..\lib\cup_v10k.jar -ast ba140645d.mjcompiler.ast -parser Parser  -buildtree ..\spec\mjparser.cup

    static {
        DOMConfigurator.configure(Log4JUtil.instance().findLoggerConfigFile());
        Log4JUtil.instance().prepareLogFile(Logger.getRootLogger());
    }

    private static final SymbolTableVisitor symbolTableVisitor = new ba140645d.mjcompiler.utilities.SymbolTableVisitor();

    public static void tsdump(){
        Tab.dump(symbolTableVisitor);
    }
    private Symbol syntaxCheck(Yylex lexer) throws Exception {
        Parser parser = new Parser(lexer);

        return parser.parse();
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(Compiler.class);
        Reader bufferReader = null;

        try{
            File source = new File("test/testProgram2.mj");
            log.info("Compiling source file :" + source.getAbsolutePath());

            bufferReader = new BufferedReader(new FileReader(source));

            Yylex lexer = new Yylex(bufferReader);

            Parser parser = new Parser(lexer);

            Symbol symbol = parser.parse();

            SyntaxNode root = (SyntaxNode)symbol.value;

            if (!(root instanceof  Program)) {
                log.error("Sintaksna greska! Prevodjenje se ne moze nastaviti");

                return;
            }

            Program program = (Program)root;

            log.info(program.toString(""));

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

            root.traverseBottomUp(semanticAnalyzer);

            if (semanticAnalyzer.isSemanticallyCorrect() == false){
                log.error("Semanticka greska! Prevodjenje se ne moze nastaviti");

                return;
            }

            tsdump();





        } catch (FileNotFoundException e) {

        } finally{
            if (bufferReader != null) try { bufferReader.close(); } catch (IOException exc) { exc.printStackTrace(); }
        }
    }
}