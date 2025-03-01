package org.example.repository.sql;

public class InsertBduCweRelation {

    public static final String SQL = """
        insert into parser.bdu_cwe (
            bdu_id,
            cwe_id
        ) values (
            :bdu_id,
            :cwe_id
        )
    """;
}
