package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;

import static lyc.compiler.constants.Constants.STRING_MAX_LENGTH;
import static lyc.compiler.constants.Constants.ID_MAX_LENGTH;
import static lyc.compiler.constants.Constants.INTEGER_MAX_POSITIVE_VALUE;
import static lyc.compiler.constants.Constants.INTEGER_MIN_NEGATIVE_VALUE;
import static lyc.compiler.constants.Constants.FLOAT_MAX_POSITIVE_VALUE;
import static lyc.compiler.constants.Constants.FLOAT_MIN_NEGATIVE_VALUE;


%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}

// Nota: 
/*
  Los profes definieron 2 tipos de funciones para retornar tokens:
  1) symbol(nombre_token) -> Se usa para definir un token estandar que tiene lexema unico. Ej: OP_ASIG, PAR_ABRE, OP_IGUAL
  2) symbol(nombre_token, lexema) -> Se usa para definir tokens como ID o CTE, donde tienen varios lexemas para un mismo token. Luego ese valor del lexema deberia guardarse en la tabla de simbolos

  Adicionalmente, estas funciones añaden automaticamente el nro de linea y el numero de columna en donde se levantó el token (se calcula sólo). 
*/


%{
  //symbol(nombre_token)
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }

  //symbol(nombre_token, lexema)
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

/* ----------------------- CONJUNTOS ÚTILES (se usan para facilitar la creación de otras REGEX) ----------------------- */

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

SpecialCharacters = ["$"|"&"|"+"|","|":"|";"|"!"|"?"|"@"|"#"|"|"|"'"|"."|"^"|"*"|"("|")"|"%"|"!"|"-"|"\""|"/"]
Letter = [a-zA-Z]
Digit = [0-9]


/* ------------------------ PALABRAS RESERVADAS BLOQUES -------------------- */

Si = "si"
Sino = "sino"
Mientras = "mientras"
Init = "init"

/* ------------ PALABRAS RESERVADAS TIPOS DE DATOS ------------------- */

Int = "Int"
Float = "Float"
String = "String"

/* ----------- PALABRAS RESERVADAS ENTRADA / SALIDA ---------------- */

Leer = "leer"
Escribir = "escribir"

/* ----------- PALABRAS RESERVADAS TEMAS ESPECIALES ---------------- */

Triangulo = "triangulo"
BinaryCount = "binaryCount"


/* ----------------------- OPERADORES ARITMETICOS -----------------------*/

Plus = "+"
Mult = "*"
Sub = "-"
Div = "/"
Assig = ":="

/* ------------------------ OPERADORES LOGICOS -------------------------- */

Equal = "=="
NotEqual = "!="
GreaterThan = ">"
GreaterEqualThan = ">="
LessThan = "<"
LessEqual = "<="
And="AND"
Or="OR"
Not="NOT"

/* ------------------------ TOKEN DE BLOQUES Y AGRUPAMIENTO ------------- */

OpenBracket = "("
CloseBracket = ")"
OpenCurlyBracket = "{"
CloseCurlyBracket = "}"
OpenSquareBracket = "["
CloseSquareBracket = "]"
Comma=","
Dot="."
Colon=":"
//Creo que no haria falta definir el punto y coma por lo que muestra la consigna

/* -------------------------  IDENTIFICADOR ------------------------- */

Identifier = {Letter} ({Letter}|{Digit})*

/* ------------------------- CONSTANTES ------------------------- */

IntegerConstant = {Digit}+
RealConstant = {Digit}+ "." {Digit}+
StringConstant = "\""({Letter}|{Digit}|{SpecialCharacters}|{Identation})*"\""


/* ----------------------- Elementos que se detectan pero NO producen TOKENS ------------------- */

WhiteSpace = {LineTerminator} | {Identation}
//Permitir comentarios con cualquier caracter, incluyendo * y -, pero no permitir -* dentro del comentario.
CommentCharacters = {Letter}|{Digit}|{WhiteSpace}|["["|"]"|"$"|"{"|"}"|"_"|"&"|"+"|","|":"|";"|"!"|"?"|"@"|"#"|"|"|"'"|"<"|">"|"."|"^"|"("|")"|"%"|"!"|"\""|"/"|"="]
Comment = "*-"  ((("-"+{CommentCharacters})* | ({CommentCharacters}|"*")* )*)* "-"*  "-*"
%%

/* 
  Nota :

  A partir de aca abajo tenemos que definir la acción léxica asocioda a cada REGEX reconocida por el lexer.
  Dentro de cada acción léxica vamos a validar el token (en caso de ser necesario, como en el caso de los IDs o CTE) y vamos a devolver el token.
  Los tokens que se devuelven tienen que estar definidos SI O SI en el archivo "parser.cup", si no les va a funcionar.

  En caso de que una validación de un token falle (supongamos CTE_ID fuera de rango) arrojamos una excepción y NO retornamos el token.

*/

/* keywords */

<YYINITIAL> {

  /* ------------------------ PALABRAS RESERVADAS (van SIEMPRE arriba de ID o CTE porque tienen mayor prioridad de ser detectadas primero) ------------- */

  {Si}                                    { return symbol(ParserSym.SI); }
  {Sino}                                  { return symbol(ParserSym.SINO); }
  {Mientras}                              { return symbol(ParserSym.MIENTRAS); }
  {Init}                                  { return symbol(ParserSym.INIT); }

  /* ------------------------ PALABRAS RESERVADAS TIPOS DE DATOS ------------------- */

  {Int}                                   { return symbol(ParserSym.INT); }
  {Float}                                 { return symbol(ParserSym.FLOAT); }
  {String}                                { return symbol(ParserSym.STRING); }


  /* ----------------------- PALABRAS RESERVADAS ENTRADA / SALIDA ------------------------ */

  {Leer}                                  { return symbol(ParserSym.LEER); }
  {Escribir}                              { return symbol(ParserSym.ESCRIBIR); }

  /* --------------------- PALABRAS RESERVADAS TEMAS ESPECIALES ---------------- */

  {Triangulo} 							{ return symbol(ParserSym.TRIANGULO); }
  {BinaryCount}							{ return symbol(ParserSym.BINARY_COUNT); }

  /* -----------------------  OPERADORES ARITMETICOS -----------------------*/
  {Plus}                                { return symbol(ParserSym.PLUS); }
  {Sub}                                 { return symbol(ParserSym.SUB); }
  {Mult}                                { return symbol(ParserSym.MULT); }
  {Div}                                 { return symbol(ParserSym.DIV); }
  {Assig}                               { return symbol(ParserSym.ASSIG); }

  /* ------------------------ OPERADORES LOGICOS -------------------------- */
  {Equal}                               { return symbol(ParserSym.EQUAL); }
  {NotEqual}                            { return symbol(ParserSym.NOT_EQUAL); }
  {GreaterThan}                         { return symbol(ParserSym.GREATER_THAN); }
  {GreaterEqualThan}                    { return symbol(ParserSym.GREATER_THAN_EQUAL); }
  {LessThan}                            { return symbol(ParserSym.LESS_THAN); }
  {LessEqual}                           { return symbol(ParserSym.LESS_THAN_EQUAL); }
  {And}                                	{ return symbol(ParserSym.AND); }
  {Or}                                	{ return symbol(ParserSym.OR); }
  {Not}                                	{ return symbol(ParserSym.NOT); }


  /* ------------------------ TOKEN DE BLOQUES Y AGRUPAMIENTO ------------- */
  {OpenBracket}                         { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                        { return symbol(ParserSym.CLOSE_BRACKET); }
  {OpenCurlyBracket}                    { return symbol(ParserSym.OPEN_CURLY_BRACKET); }
  {CloseCurlyBracket}                   { return symbol(ParserSym.CLOSE_CURLY_BRACKET); }
  {OpenSquareBracket}                   { return symbol(ParserSym.OPEN_SQUARE_BRACKET); }
  {CloseSquareBracket}                  { return symbol(ParserSym.CLOSE_SQUARE_BRACKET); }
  {Comma}                     			{ return symbol(ParserSym.COMMA); }
  {Dot}                     			{ return symbol(ParserSym.DOT); }
  {Colon}                   			{ return symbol(ParserSym.COLON); }

	/* -------------------------  IDENTIFICADOR ------------------------- */
  {Identifier}                           {
											int largoCadena = yytext().length();
											if(largoCadena > ID_MAX_LENGTH){
												throw new InvalidLengthException("El largo máximo de una Identificador es 30. Largo del identificador: " + largoCadena);
											}

											return symbol(ParserSym.IDENTIFIER, yytext());
										}

  /* ------------------------- CONSTANTES ------------------------- */
  {IntegerConstant}                      {
											long valorEntero = Long.parseLong(yytext());
											if(valorEntero < INTEGER_MIN_NEGATIVE_VALUE){
												throw new InvalidIntegerException("El valor mínimo de una constante Int es " + INTEGER_MIN_NEGATIVE_VALUE + ". Valor de la constante: " +  valorEntero);
											}

											if(valorEntero > INTEGER_MAX_POSITIVE_VALUE){
												throw new InvalidIntegerException("El valor máximo de una constante Int es " + INTEGER_MAX_POSITIVE_VALUE + ". Valor de la constante: " +  valorEntero);
											}
											return symbol(ParserSym.INTEGER_CONSTANT, yytext());
										}

  {RealConstant}                        {
										  double valorFlotante = Double.parseDouble(yytext());

										  if(valorFlotante < FLOAT_MIN_NEGATIVE_VALUE){
											  throw new InvalidFloatException("El valor mínimo de una constante Float es " + FLOAT_MIN_NEGATIVE_VALUE + ". Valor de la constante: " +  valorFlotante);
										  }

										  if(valorFlotante > FLOAT_MAX_POSITIVE_VALUE){
											  throw new InvalidFloatException("El valor máximo de una constante Float es " + FLOAT_MAX_POSITIVE_VALUE + ". Valor de la constante: " +  valorFlotante);
										  }

										  return symbol(ParserSym.FLOAT_CONSTANT, yytext());
										 }
  {StringConstant}                       {

											  int largoCadena = yytext().length();
											  if(largoCadena > STRING_MAX_LENGTH){
												  throw new InvalidLengthException("El largo máximo de una constante String es 40. Largo de la cadena: " + largoCadena);
											  }

											  return symbol(ParserSym.STRING_CONSTANT, yytext());
										  }


  /* ----------------------- Elementos que se detectan pero NO producen TOKENS ------------------- */
  {WhiteSpace}                   		{ /* ignore */ }
  {Comment}                   			{ /* ignore */ }
}


/* error fallback */
[^]                              		{ throw new UnknownCharacterException(yytext()); }
