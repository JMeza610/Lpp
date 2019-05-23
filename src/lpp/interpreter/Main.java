package lpp.interpreter;

import lpp.parser.StreamTokenizer;
import lpp.parser.*;
import lpp.parser.ast.Prog;
import lpp.visitors.evaluation.Eval;
import lpp.visitors.evaluation.EvaluatorException;
import lpp.visitors.typechecking.TypeCheck;
import lpp.visitors.typechecking.TypecheckerException;

import java.io.*;

import static java.lang.System.err;

public class Main {

  private static String fileIn = null;
  private static String fileOut = null;
  private static boolean typeChecking = true;

  public static void main(String[] args) {

    readArgs(args);

    BufferedReader bufferedReader = tryOpenBR(fileIn);
    PrintWriter printWriter = tryOpenPW(fileOut);

    try (Tokenizer tokenizer = new StreamTokenizer(bufferedReader)) {
      Parser parser = new MyParser(tokenizer);
      Prog prog = parser.parseProg();
      if (typeChecking) prog.accept(new TypeCheck());
      prog.accept(new Eval(printWriter));
    } catch (TokenizerException e) {
      err.println("Tokenizer error: " + e.getMessage());
    } catch (ParserException e) {
      err.println("Syntax error: " + e.getMessage());
    } catch (TypecheckerException e) {
      err.println("Static error: " + e.getMessage());
    } catch (EvaluatorException e) {
      err.println("Dynamic error: " + e.getMessage());
    } catch (Throwable e) {
      err.println("Unexpected error.");
      e.printStackTrace();
    }
  }

  private static void readArgs(String[] args) {
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-i":
          if (i + 1 < args.length) {
            fileIn = args[i + 1];
          }
          break;
        case "-o":
          if (i + 1 < args.length)
            fileOut = args[i + 1];
          break;
        case "-ntc":
          typeChecking = false;
          break;
      }
    }
  }

  /**
   * Tries opening the given file.
   *
   * @param filename Filename given by the user at launch.
   * @return A {@link BufferedReader} containing either the file or {@code System.out}.
   */
  private static BufferedReader tryOpenBR(String filename) {
    if (filename != null && !filename.equals("-i") && !filename.equals("-o")) {
      try {
        return new BufferedReader(new FileReader(filename));
      } catch (FileNotFoundException e) {
        System.out.println("Cannot open " + filename + "\nUsing stdin.");
      }
    }
    return new BufferedReader(new InputStreamReader(System.in));
  }

  /**
   * Tries opening the given file.
   *
   * @param filename Filename given by the user at launch.
   * @return A {@link PrintWriter} containing either the file or {@code System.out}.
   */
  private static PrintWriter tryOpenPW(String filename) {
    if (filename != null && !filename.equals("-i") && !filename.equals("-o")) {
      try {
        return new PrintWriter(new FileWriter(filename), true);
      } catch (IOException e) {
        System.out.println("Cannot open " + filename + "\nUsing stdout");
      }
    }
    return new PrintWriter(System.out, true);
  }
}
