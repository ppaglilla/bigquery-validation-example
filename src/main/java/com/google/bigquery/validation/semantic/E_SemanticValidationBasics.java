package com.google.bigquery.validation.semantic;

import com.google.zetasql.Analyzer;
import com.google.zetasql.AnalyzerOptions;
import com.google.zetasql.Parser;
import com.google.zetasql.SimpleCatalog;
import com.google.zetasql.SimpleColumn;
import com.google.zetasql.SimpleTable;
import com.google.zetasql.SqlException;
import com.google.zetasql.TypeFactory;
import com.google.zetasql.ZetaSQLBuiltinFunctionOptions;
import com.google.zetasql.ZetaSQLType.TypeKind;
import com.google.zetasql.resolvedast.ResolvedNodes.ResolvedStatement;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;
import java.util.List;

/**
 * Basic example showcasing syntactic and semantic SQL validation using ZetaSQL.
 *
 * As shown in the parsing examples, syntactic validation of SQL queries is implemented by the
 * {@link Parser}. The parser catches all errors related to syntax, which are caused by
 * malformed queries.
 *
 * Semantic errors are more nuanced. They include things like querying a table or column which
 * does not exist, calling a function or procedure which does not exist, calling a function
 * with the wrong signature, using an aggregate function without a required GROUP BY clause, etc.
 * Semantic validation is performed by the ZetaSQL {@link Analyzer} and, ultimately, a query
 * passing the analysis phase is a valid query.
 *
 * Running the Analyzer is similar to running the Parser, but requires us to provide information
 * about our catalog. The catalog contains information about the tables, functions and other SQL
 * resources; information which allows the analyzer to perform validations. The Analyzer also
 * performs parsing first, so it will report parsing errors by itself if encountered.
 */
public class E_SemanticValidationBasics {

  public static void main(String[] args) {
    SimpleCatalog catalog = new SimpleCatalog("catalog");
    catalog.addZetaSQLFunctionsAndTypes(new ZetaSQLBuiltinFunctionOptions());

    AnalyzerOptions options = new AnalyzerOptions();
    options.setLanguageOptions(BigQueryLanguageOptions.get());

    // Example 1: Valid query. Analyzing it will succeed.
    String query = "SELECT 1 AS column;";

    System.out.printf("Analyzing query: %s. Will succeed.\n", query);
    ResolvedStatement analyzedStatement = Analyzer.analyzeStatement(query, options, catalog);
    System.out.println(analyzedStatement.debugString());

    // Example 2: Query querying a table which does not exist. Analyzing it will throw a SqlException
    query = "SELECT * FROM `dataset.table` WHERE column1 = 1;";

    try {
      System.out.printf(
          "Analyzing query: %s. Will fail because the table isn't in the catalog.\n", query);
      analyzedStatement = Analyzer.analyzeStatement(query, options, catalog);
      System.out.println(analyzedStatement.debugString());
    } catch(SqlException error) {
      System.out.printf("Invalid query: %s\n\n", error.getMessage());
    }

    // Example 3: Now, we add the previous table to the catalog. Analyzing will succeed again
    catalog.addSimpleTable(
        "dataset.table",
        new SimpleTable(
            "dataset.table",
            List.of(
                new SimpleColumn("dataset.table", "column1", TypeFactory.createSimpleType(TypeKind.TYPE_INT64)),
                new SimpleColumn("dataset.table", "column2", TypeFactory.createSimpleType(TypeKind.TYPE_STRING))
            )
        )
    );

    System.out.printf(
        "Analyzing query: %s. Will succeed because the table is now in the catalog.\n", query);
    analyzedStatement = Analyzer.analyzeStatement(query, options, catalog);
    System.out.println(analyzedStatement.debugString());

    // Example 4: Using an aggregate function without required GROUP BY
    query = "SELECT column2, SUM(column1) FROM `dataset.table`;";

    try {
      System.out.printf("Analyzing query: %s. Will fail because column 2 is not grouped.\n", query);
      analyzedStatement = Analyzer.analyzeStatement(query, options, catalog);
      System.out.println(analyzedStatement.debugString());
    } catch(SqlException error) {
      System.out.printf("Invalid query: %s\n\n", error.getMessage());
    }
  }

}
