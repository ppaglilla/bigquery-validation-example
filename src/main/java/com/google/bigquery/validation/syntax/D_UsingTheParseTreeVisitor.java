package com.google.bigquery.validation.syntax;

import com.google.zetasql.AnalyzerOptions;
import com.google.zetasql.Parser;
import com.google.zetasql.SimpleCatalog;
import com.google.zetasql.parser.ASTJoinEnums.JoinType;
import com.google.zetasql.parser.ASTNodes.ASTJoin;
import com.google.zetasql.parser.ASTNodes.ASTStatement;
import com.google.zetasql.parser.ParseTreeVisitor;
import com.google.zetasql.toolkit.ZetaSQLToolkitAnalyzer;
import com.google.zetasql.toolkit.catalog.bigquery.BigQueryCatalog;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;

/**
 * Example of how to use a {@link ParseTreeVisitor} to traverse the parse tree.
 *
 * A ParseTreeVisitor allows us to traverse the parse tree and, for example, apply extra validations
 * to queries. Let's say we wanted to disallow CROSS JOINs, we could create a ParseTreeVisitor that
 * checks if a CROSS JOIN is performed somewhere in the query and handle it accordingly.
 * This example implements such use case.
 *
 */
public class D_UsingTheParseTreeVisitor {

  /**
   * Example {@link ParseTreeVisitor} that checks if the query performs a CROSS JOIN
   */
  private static class DetectCrossJoins extends ParseTreeVisitor {

    private boolean _foundCrossJoin = false;

    public boolean foundCrossJoin() {
      return _foundCrossJoin;
    }

    @Override
    public void visit(ASTJoin join) {
      super.visit(join);

      JoinType joinType = join.getJoinType();

      if(joinType.equals(JoinType.CROSS) || joinType.equals(JoinType.COMMA)) {
        this._foundCrossJoin = true;
      }

    }

  }

  public static void main(String[] args) {
    String query = "SELECT t1.column1, t2.column2 FROM table1 AS t1 CROSS JOIN table2;";

    ASTStatement parsedQuery = Parser.parseStatement(query, BigQueryLanguageOptions.get());

    DetectCrossJoins detectCrossJoins = new DetectCrossJoins();

    parsedQuery.accept(detectCrossJoins);

    System.out.printf("Found cross joins: %s\n", detectCrossJoins.foundCrossJoin());
  }

}
