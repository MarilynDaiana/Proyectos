package lyc.compiler;

import lyc.compiler.factories.LexerFactory;
import lyc.compiler.model.CompilerException;
import lyc.compiler.model.InvalidIntegerException;
import lyc.compiler.model.InvalidLengthException;
import lyc.compiler.model.UnknownCharacterException;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static lyc.compiler.constants.Constants.STRING_MAX_LENGTH;

import lyc.compiler.model.InvalidFloatException;

// Quitar la linea de abajo para habilitar los tests antes de compilar (por defecto están deshabilitados hasta que los hagamos funcionar)
//@Disabled
public class LexerTest {

  private Lexer lexer;


  @Test
  public void comment() throws Exception{
    scan("*-this is a comment-*");
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  @Test
  public void invalidStringConstantLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan("\"%s\"".formatted(getRandomString()));
      nextToken();
    });
  }


  @Test
  public void invalidIdLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan(getRandomString());
      nextToken();
    });
  }

  @Test
  public void invalidPositiveIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(9223372036854775807L));
      nextToken();
    });
  }

  //TO DO: VER DE ARREGLAR CUANDO SE PUEDA DETECTAR ENTEROS NEGATIVOS
  @Disabled
  @Test
  public void invalidNegativeIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(-9223372036854775807L));
      nextToken();
    });
  }

  @Test
  public void assignmentWithExpressions() throws Exception {
    scan("c:=d*(e-21)/4");
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.ASSIG);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.MULT);
    assertThat(nextToken()).isEqualTo(ParserSym.OPEN_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.SUB);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.CLOSE_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.DIV);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  @Test
  public void unknownCharacter() {
    assertThrows(UnknownCharacterException.class, () -> {
      scan("#");
      nextToken();
    });
  }

  /* ---------------------------- TESTS NUESTROS ---------------------------- */

  @Test
  public void invalidPositiveFloatConstantValue() {
    assertThrows(InvalidFloatException.class, () -> {
      scan("%f".formatted(10000000000000000.5).replace(",", ".")); //Se agrega un replace porque en algunas computadoras devuelve flotantes con coma en vez de punto
      nextToken();
    });
  }

  //TO DO: VER DE ARREGLAR CUANDO SE PUEDA DETECTAR FLOTANTES NEGATIVOS
  @Disabled
  @Test
  public void invalidNegativeFloatConstantValue() {
    assertThrows(InvalidFloatException.class, () -> {
      scan("%f".formatted(-10000000000000000.5).replace(",", ".")); //Se agrega un replace porque en algunas computadoras devuelve flotantes con coma en vez de punto
      nextToken();
    });
  }

  @Test
  public void stringConstant() throws Exception{
    scan("\"mi string 123456789 !#$%&/()@\"");
    assertThat(nextToken()).isEqualTo(ParserSym.STRING_CONSTANT);
  }

  @Test
  public void initBlock() throws Exception{
    scan("init {\n" +
            "    a1, b1 : Float\n" +
            "    variable1 : Int\n" +
            "    p1, p2, p3 : String\n" +
            "    c, d, e : Int\n" +
            "}");
    assertThat(nextToken()).isEqualTo(ParserSym.INIT);
    assertThat(nextToken()).isEqualTo(ParserSym.OPEN_CURLY_BRACKET);

    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COMMA);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COLON);
    assertThat(nextToken()).isEqualTo(ParserSym.FLOAT);

    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COLON);
    assertThat(nextToken()).isEqualTo(ParserSym.INT);

    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COMMA);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COMMA);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COLON);
    assertThat(nextToken()).isEqualTo(ParserSym.STRING);

    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COMMA);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.COMMA);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);

    assertThat(nextToken()).isEqualTo(ParserSym.COLON);
    assertThat(nextToken()).isEqualTo(ParserSym.INT);


    assertThat(nextToken()).isEqualTo(ParserSym.CLOSE_CURLY_BRACKET);
  }


  @AfterEach
  public void resetLexer() {
    lexer = null;
  }

  private void scan(String input) {
    lexer = LexerFactory.create(input);
  }

  private int nextToken() throws IOException, CompilerException {
    return lexer.next_token().sym;
  }

  private static String getRandomString() {
    return new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS)
            .withinRange('a', 'z')
            .build().generate(STRING_MAX_LENGTH * 2);
  }

}
