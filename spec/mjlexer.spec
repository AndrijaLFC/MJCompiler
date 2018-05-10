package ba140645d.mjcompiler;

import java_cup.runtime.Symbol;

%%

%{

    private Symbol newSymbol(int type){
        return new Symbol(type, yyline + 1, yycolumn);
    }


    private Symbol newSymbol(int type, Object value){
        return new Symbol(type, yyline + 1, yycolumn, value);
    }


%}
%cup
%line
%column

%xstate COMMENT_STATE


%eofval{
    return newSymbol(sym.EOF);
%eofval}

%%

" "     { }
"\b"    { }
"\t"    { }
"\r\n"  { }
"\f"    { }

"program"   { return newSymbol(sym.PROGRAM, yytext());                  }
"break"     { return newSymbol(sym.BREAK, yytext());                    }
"class"     { return newSymbol(sym.CLASS, yytext());                    }
"else"      { return newSymbol(sym.ELSE, yytext());                     }
"if"        { return newSymbol(sym.IF, yytext());                       }
"new"       { return newSymbol(sym.NEW, yytext());                      }
"print"     { return newSymbol(sym.PRINT, yytext());                    }
"read"      { return newSymbol(sym.READ, yytext());                     }
"return"    { return newSymbol(sym.RETURN, yytext());                   }
"void"      { return newSymbol(sym.VOID, yytext());                     }
"do"        { return newSymbol(sym.DO, yytext());                       }
"while"     { return newSymbol(sym.WHILE, yytext());                    }
"extends"   { return newSymbol(sym.EXTENDS, yytext());                  }
"continue"  { return newSymbol(sym.CONTINUE, yytext());                 }
"true"      { return newSymbol(sym.BOOL_CONST, new Boolean(true));      }
"false"     { return newSymbol(sym.BOOL_CONST, new Boolean(false));     }
"const"     { return newSymbol(sym.CONST, yytext());                    }


"+"     { return newSymbol(sym.OP_ADDITION, yytext());              }
"-"     { return newSymbol(sym.OP_SUBTRACTION, yytext());           }
"*"     { return newSymbol(sym.OP_MULTIPLICATION, yytext());        }
"/"     { return newSymbol(sym.OP_DIVISION, yytext());              }
"%"     { return newSymbol(sym.OP_MODUO, yytext());                 }
"=="    { return newSymbol(sym.OP_EQUALS, yytext());                }
"!="    { return newSymbol(sym.OP_NOT_EQUALS, yytext());            }
">"     { return newSymbol(sym.OP_GREATER, yytext());               }
">="    { return newSymbol(sym.OP_GREATER_EQUAL, yytext());         }
"<"     { return newSymbol(sym.OP_LESSER, yytext());                }
"<="    { return newSymbol(sym.OP_LESSER_EQUAL, yytext());          }
"&&"    { return newSymbol(sym.OP_LOGIC_AND, yytext());             }
"||"    { return newSymbol(sym.OP_LOGIC_OR, yytext());              }
"="     { return newSymbol(sym.OP_EQUAL, yytext());                 }
"++"    { return newSymbol(sym.OP_INCREMENT, yytext());             }
"--"    { return newSymbol(sym.OP_DECREMENT, yytext());             }
";"     { return newSymbol(sym.OP_SEMICOLN, yytext());              }
","     { return newSymbol(sym.OP_COMMA, yytext());                 }
"("     { return newSymbol(sym.OP_L_PARENTHESES, yytext());         }
")"     { return newSymbol(sym.OP_R_PARENTHESES, yytext());         }
"["     { return newSymbol(sym.OP_L_SQUARED_PARENTHESES, yytext()); }
"]"     { return newSymbol(sym.OP_R_SQUARED_PARENTHESES, yytext()); }
"{"     { return newSymbol(sym.OP_L_BRACE_PARENTHESES, yytext());   }
"}"     { return newSymbol(sym.OP_R_BRACE_PARENTHESES, yytext());   }
"."     { return newSymbol(sym.OP_DOT, yytext());                   }



[0-9]+                      { return newSymbol(sym.NUM_CONST, new Integer(yytext()));               }
([a-zA-Z])([a-zA-Z0-9]|_)*  { return newSymbol(sym.IDENTIFIKATOR, yytext());                        }
\".\"                       { return newSymbol(sym.CHAR_CONST, new Character(yytext().charAt(1)));  }


"//"                    { yybegin(COMMENT_STATE);   }
<COMMENT_STATE> "\r\n"  { yybegin(YYINITIAL);       }
<COMMENT_STATE> .       { yybegin(COMMENT_STATE);   }


. { System.err.println("Leksicka greska (" + yytext() + ") u liniji " + (yyline + 1) + ")"); }
