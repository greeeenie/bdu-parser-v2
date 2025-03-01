package org.example.repository.sql;

public class InsertCweCapecRelation {

    public static final String SQL = """
        insert into parser.cwe_capec (
            cwe_id,
            capec_id
        ) values (
            :cwe_id,
            :capec_id
        )
    """;
}
