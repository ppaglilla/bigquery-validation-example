package com.google.bigquery.validation.syntax;

import com.google.zetasql.LanguageOptions;
import com.google.zetasql.Parser;
import com.google.zetasql.SqlException;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;

/**
 * Example showing how the parser can catch and report syntax errors.
 *
 * When ZetaSQL parses a SQL query, it will perform syntactic validation on it. Any syntax error
 * found will be thrown as a {@link com.google.zetasql.SqlException} with a descriptive error
 * message.
 *
 * {@link Parser#parseScript(String, LanguageOptions)} returns the error locations (line and column
 * of the syntax error) together with the error message. We're working on providing that
 * information on other Parser methods as well.
 */
public class B_FindingSyntaxErrors {

  public static void main(String[] args) {
    String query = "SELECT FROM table";  // Syntax error: SELECT list must not be empty

    // Will throw SqlException
    try {
      Parser.parseScript(query, BigQueryLanguageOptions.get());
    } catch (SqlException error) {
      System.out.printf("Failed to parse query: %s - %s", query, error.getMessage());
    }
  }

}
