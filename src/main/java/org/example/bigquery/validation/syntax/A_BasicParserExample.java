/*
 * Copyright 2023 Pablo Paglilla All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.bigquery.validation.syntax;

import com.google.zetasql.LanguageOptions;
import com.google.zetasql.Parser;
import com.google.zetasql.parser.ASTNode;
import com.google.zetasql.parser.ASTNodes.ASTScript;
import com.google.zetasql.parser.ASTNodes.ASTStatement;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;

/**
 * Basic usage example for the ZetaSQL Parser (available from ZetaSQL 2023.03.2 onwards)
 *
 * Running the parser returns an {@link ASTNode}, either an {@link ASTStatement}
 * or an {@link ASTScript}. Parsing a statement performs syntax validation and will throw
 * a {@link com.google.zetasql.SqlException} upon finding invalid syntax, including a description
 * for the error.
 *
 * When running the Parser, we need to configure the set of {@link LanguageOptions} we'll use,
 * which allow us to customize the SQL language. The zetasql-toolkit include the
 * {@link BigQueryLanguageOptions}, which implement BigQuery's feature set.
 *
 */
public class A_BasicParserExample {

  public static void main(String[] args) {
    String query = "SELECT t1.column1, t2.column2 FROM table1 AS t1"
        + " INNER JOIN table2 AS t2 USING(joincolumn);";

    ASTStatement parsedQuery = Parser.parseStatement(query, BigQueryLanguageOptions.get());

    System.out.println(parsedQuery);
  }

}