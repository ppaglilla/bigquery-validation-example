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

package org.example.bigquery.validation.semantic;

import com.google.zetasql.AnalyzerOptions;
import com.google.zetasql.resolvedast.ResolvedNodes.ResolvedStatement;
import com.google.zetasql.toolkit.ZetaSQLToolkitAnalyzer;
import com.google.zetasql.toolkit.catalog.bigquery.BigQueryCatalog;
import com.google.zetasql.toolkit.options.BigQueryLanguageOptions;
import java.util.Iterator;

/**
 * As shown in the previous examples, the key for performing semantic query validation
 * for BigQuery is using the Analyzer. That requires, however, building the corresponding
 * catalog. The zetasql-toolkit provides utilities to build such a catalog.
 *
 * This examples builds a catalog for analyzing BigQuery queries using the BigQuery API. However,
 * the toolkit is extensible to work in an offline environment if required.
 */
public class F_BuildingACatalogForBigQuery {

  public static void main(String[] args) {
    // Analyzing a query that uses bigquery-public-data tables
    String query =
        "INSERT INTO `bigquery-public-data.samples.wikipedia` (title) VALUES ('random title');\n"
            + "SELECT title, language FROM `bigquery-public-data.samples.wikipedia` WHERE title = 'random title';";

    // Step 1: Create a BigQueryCatalog
    // In this case, we provide the project id where queries are assumed to be running. The catalog
    // will connect to the BigQuery API using application-default credentials to access BigQuery
    // resources.
    // You can also provide your own BigQuery API client or a custom implementation of
    // BigQueryResourceProvider.
    BigQueryCatalog catalog = new BigQueryCatalog("bigquery-public-data");

    // Step 2: Add tables to the catalog before analyzing
    // BigQueryCatalog.addTable will fetch the table metadata and
    // create the table in the catalog.
    // Just as we can add tables and views; we can also add UDFs, TVFs and Procedures from BigQuery.
    // See also: BigQueryCatalog.addAllTablesInDataset and BigQueryCatalog.addAllTablesInProject
    catalog.addTable("bigquery-public-data.samples.wikipedia");

    // Step 3: Define the LanguageOptions and AnalyzerOptions to configure the ZetaSQL analyzer

    // AnalyzerOptions are ZetaSQL's way of customizing the analyzer itself
    // Usually, setting the LanguageOptions is the only configuration required; but they can be
    // customized for more advanced use cases.
    AnalyzerOptions options = new AnalyzerOptions();
    options.setLanguageOptions(BigQueryLanguageOptions.get());

    // Step 4: Use the ZetaSQLToolkitAnalyzer to get an iterator of the ResolvedStatements
    // that result from running the analysis
    ZetaSQLToolkitAnalyzer analyzer = new ZetaSQLToolkitAnalyzer(options);
    Iterator<ResolvedStatement> statementIterator = analyzer.analyzeStatements(query, catalog);

    // Step 5: Consume the previous iterator and use the ResolvedStatements however you need
    statementIterator.forEachRemaining(statement -> System.out.println(statement.debugString()));
  }

}
