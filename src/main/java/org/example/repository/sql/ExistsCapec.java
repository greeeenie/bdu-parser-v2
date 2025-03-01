package org.example.repository.sql;

public class ExistsCapec {

    public static final String SQL = """
        select count(1)
        from parser.capec
        where id = :id
    """;
}
