## BigQuery Query Validation Example

This is an example project showcasing how to perform syntactic and semantic
query validation for BigQuery using [ZetaSQL](https://github.com/google/zetasql)
and the [ZetaSQL Toolkit](https://github.com/GoogleCloudPlatform/professional-services/tree/main/tools/zetasql-helper/zetasql-toolkit-core).

This project is an example-driven guide, where each example explains some aspect
of the proposed solution using ZetaSQL. There are two sections:

1. Syntactic validation
   * [Basic Parsing Example](src/main/java/org/example/bigquery/validation/syntax/A_BasicParserExample.java)
   * [Finding Syntax Errors](src/main/java/org/example/bigquery/validation/syntax/B_FindingSyntaxErrors.java)
   * [Parsing Multi-Statement Queries](src/main/java/org/example/bigquery/validation/syntax/C_ParsingMultiStatementQueries.java)
   * [Processing the parse tree](src/main/java/org/example/bigquery/validation/syntax/D_UsingTheParseTreeVisitor.java)
2. Semantic validation
   * [Semantic Validation Basics](src/main/java/org/example/bigquery/validation/semantic/E_SemanticValidationBasics.java)
   * [Building a Catalog for BigQuery](src/main/java/org/example/bigquery/validation/semantic/F_BuildingACatalogForBigQuery.java)
   * [Validating Queries that include DDL](src/main/java/org/example/bigquery/validation/semantic/G_ValidatingDdl.java)

## Navigating the examples

We recommend going through the examples in the provided order and reading them, 
together with the included javadocs and comments explaining them. They will
walk you through both syntactic validation and semantic validation for queries.

## Building containers for these examples

You can build containers for this examples easily using [Jib](https://github.com/GoogleContainerTools/jib).
See [Jib's quickstart](https://cloud.google.com/java/getting-started/jib).

### Build the container locally

``` bash
mvn compile jib:dockerBuild \
  -Dcontainer.mainClass=org.example.bigquery.validation.syntax.A_BasicParserExample
```

### Build and push to a container registry

``` bash
mvn compile jib:build \
  -Dcontainer.mainClass=org.example.bigquery.validation.syntax.A_BasicParserExample \
  -Dimage=gcr.io/...
```
