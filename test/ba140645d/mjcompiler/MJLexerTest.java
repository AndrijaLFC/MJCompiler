package ba140645d.mjcompiler;


import ba140645d.log4j.Log4JUtil;
import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;

public class MJLexerTest{
    static {
        DOMConfigurator.configure(Log4JUtil.instance().findLoggerConfigFile());
        Log4JUtil.instance().prepareLogFile(Logger.getRootLogger());
    }
    public static void main(String[] args) throws IOException
    {
        Logger log = Logger.getLogger(MJLexerTest.class);
        Reader bufferReader = null;

        try{
            File source = new File(args[0]);
            log.info("Compiling source file :" + source.getAbsolutePath());

            bufferReader = new BufferedReader(new FileReader(source));

            Yylex lexer = new Yylex(bufferReader);

            Symbol token = null;

            while((token = lexer.next_token()).sym != sym.EOF){
                if (token != null)
                    log.info(token.toString() + " " +  token.value.toString());
            }

        } catch (FileNotFoundException e) {

        } finally{
            if (bufferReader != null) try { bufferReader.close(); } catch (IOException exc) { exc.printStackTrace(); }
        }

    }
}