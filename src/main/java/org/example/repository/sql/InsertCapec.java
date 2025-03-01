package org.example.repository.sql;

public class InsertCapec {

    public static final String SQL = """
        insert into parser.capec (
            id,
            name,
            likelihood
        ) values (
            :id,
            :name,
            :likelihood
        )
    """;
}
