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

import com.google.common.collect.ImmutableList;
import com.google.zetasql.LanguageOptions;
import com.google.zetasql.ParseResumeLocation;
import com.google.zetasql.Parser;
import com.google.zetasql.parser.ASTNodes.ASTScript;
import com.google.zetasql.parser.ASTNodes.ASTStatement;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;

/**
 * Example showing how to parse multi-statement queries using the ZetaSQL parser
 *
 * There are two ways of achieving this:
 *  1. Using {@link Parser#parseScript(String, LanguageOptions)}, which returns an
 *    {@link ASTScript} containing all statements
 *  2. Using {@link Parser#parseNextStatement(ParseResumeLocation, LanguageOptions)} or
 *    {@link Parser#parseNextScriptStatement(ParseResumeLocation, LanguageOptions)}. These
 *    parse one statement at a time.
 */
public class C_ParsingMultiStatementQueries {

  public static void main(String[] args) {
    String query = "CREATE TABLE table1 (joincolumn STRING, column1 STRING);";
    query += "CREATE TABLE table2 (joincolumn STRING, column2 INT64);";

    query += "SELECT t1.column1, t2.column2 FROM table1 AS t1"
        + " INNER JOIN table2 AS t2 USING(joincolumn);";

    // Option 1: Parsing the whole script using Parser.parseScript
    ASTScript parsedScript = Parser.parseScript(query, BigQueryLanguageOptions.get());
    ImmutableList<ASTStatement> parsedStatements =
        parsedScript.getStatementListNode().getStatementList();

    parsedStatements.forEach(System.out::println);

    // Option 2: Parsing statements one by one using Parser.parseNextStatement
    ParseResumeLocation parseResumeLocation = new ParseResumeLocation(query);

    // While we're not at the end of the script
    while(parseResumeLocation.getBytePosition() < query.getBytes().length) {
      ASTStatement nextStatement =
          Parser.parseNextScriptStatement(parseResumeLocation, BigQueryLanguageOptions.get());
      System.out.println(nextStatement);
    }

  }

}
