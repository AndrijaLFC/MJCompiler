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
$AST_PACKAGE__NAME = $PACKAGE_NAME + '.' + $AST_DIR_NAME

#prebacujemo se na radni direktorijum
pushd $WORKING_DIR\src

java -jar $WORKING_DIR/lib/cup_v10k.jar -ast $AST_PACKAGE_NAME -parser $PARSER_NAME -buildtree $WORKING_DIR/spec/mjparser.cup


Remove-Item -Path $WORKING_DIR\$PACKAGE_REL_PATH\MJParser.java
Remove-Item -Path $WORKING_DIR\$PACKAGE_REL_PATH\sym.java

Move-Item -Path $pwd\MJParser.java -Destination $WORKING_DIR\$PACKAGE_REL_PATH
Move-Item -Path $pwd\sym.java -Destination $WORKING_DIR\$PACKAGE_REL_PATH

#vratimo se na prethodni direktorijum
popd