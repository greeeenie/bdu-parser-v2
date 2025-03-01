package org.example.repository.sql;

public class SelectCapecsWithoutNameRu {
    public static final String SQL = """
        select id,
               name,
               likelihood
        from parser.capec
        where name_ru is null
    """;
}
