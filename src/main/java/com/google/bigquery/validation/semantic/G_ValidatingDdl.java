package com.google.bigquery.validation.semantic;

import com.google.zetasql.AnalyzerOptions;
import com.google.zetasql.SqlException;
import com.google.zetasql.resolvedast.ResolvedNodes.ResolvedStatement;
import com.google.zetasql.toolkit.ZetaSQLToolkitAnalyzer;
import com.google.zetasql.toolkit.catalog.bigquery.BigQueryCatalog;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;
import java.util.Iterator;

/**
 * ZetaSQL can also be used to validate SQL scripts that contain DDL statements, which BigQuery
 * dry runs do not allow. This functionality is built into the zetasql-toolkit.
 *
 * Using the zetasql-toolkit, analyzing a DDL statement will perform semantic validation and
 * update the catalog accordingly. This allows validating queries that, for example, create a temp
 * table and later query it.
 */
public class G_ValidatingDdl {

  public static void main(String[] args) {
    // This SQL script creates a temp table and later queries it.
    // The first SELECT statement is valid and will be analyzed successfully.
    // The second SELECT statement filters on column2, which does not exist in the table.
    // Analyzing the second statement will throw the corresponding SqlException.
    String query =
        "CREATE TEMP TABLE t AS (SELECT 1 AS column UNION ALL SELECT 2 AS column);\n"
            + "SELECT * FROM t WHERE column = 4;\n"
            + "SELECT * FROM t WHERE column2 = 5;";

    BigQueryCatalog catalog = new BigQueryCatalog("bigquery-public-data");

    AnalyzerOptions options = new AnalyzerOptions();
    options.setLanguageOptions(BigQueryLanguageOptions.get());

    ZetaSQLToolkitAnalyzer analyzer = new ZetaSQLToolkitAnalyzer(options);
    Iterator<ResolvedStatement> statementIterator = analyzer.analyzeStatements(query, catalog);

    // Iterating the analyzed statements will lazily run the analyzer and update the catalog
    // when necessary
    try {
      statementIterator.forEachRemaining(statement -> System.out.println(statement.debugString()));
    } catch(SqlException error) {
      System.out.printf("Invalid script: %s\n\n", error.getMessage());
    }

  }

}
