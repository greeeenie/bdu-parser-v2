package org.example.repository.sql;

public class InsertCwe {

    public static final String SQL = """
        insert into parser.cwe (
            id,
            name
        ) values (
            :id,
            :name
        )
    """;
}
