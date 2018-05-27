######################################################
############# Skripta za generisanje AST #############
######################################################


#relativna putanja paketa
$PACKAGE_REL_PATH = 'src\ba140645d\mjcompiler'

#naziv paketa
$PACKAGE_NAME = 'ba140645d.mjcompiler'

#naziv ast direktorijuma i paketa
$AST_NAME = 'ast'

#radni direktorijum
$WORKING_DIR = 'C:\Users\Andrija Boricic\Desktop\ETF SI\PP\MJCompiler'

#zeljeni naziv Parsera
$PARSER_NAME = 'Parser'

#naziv ast paketa
$AST_PACKAGE_NAME = $PACKAGE_NAME + '.' + $AST_NAME

#prebacujemo se na radni direktorijum
pushd $WORKING_DIR\src

echo "AST Package name : ".$AST_PACKAGE_NAME."\n"


java -jar $WORKING_DIR/lib/cup_v10k.jar -ast $AST_PACKAGE_NAME -parser $PARSER_NAME -buildtree $WORKING_DIR/spec/mjparser.cup

if (Test-Path $WORKING_DIR\$PACKAGE_REL_PATH\$PARSER_NAME.java){
    echo "Removing old $PARSER_NAME.java";
    Remove-Item -Path $WORKING_DIR\$PACKAGE_REL_PATH\$PARSER_NAME.java
    }

if (Test-Path $WORKING_DIR\$PACKAGE_REL_PATH\sym.java){
    echo "Removing old sym.java";
    Remove-Item -Path $WORKING_DIR\$PACKAGE_REL_PATH\sym.java
    }

if (Test-Path $pwd\$PARSER_NAME.java){
    echo "Moving $PARSER_NAME.java"
    Move-Item -Path $pwd\$PARSER_NAME.java -Destination $WORKING_DIR\$PACKAGE_REL_PATH
    }

if (Test-Path $pwd\sym.java){
    echo "Moving sym.java"
    Move-Item -Path $pwd\sym.java -Destination $WORKING_DIR\$PACKAGE_REL_PATH
    }

#vratimo se na prethodni direktorijum
popd