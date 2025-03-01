package org.example.repository.sql;

public class ExistsCwe {

    public static final String SQL = """
        select count(1)
        from parser.cwe
        where id = :id
    """;
}
