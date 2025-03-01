package org.example.repository.sql;

public class InsertBdu {

    public static final String SQL = """
        insert into parser.bdu (
            id,
            name,
            description
        ) values (
            :id,
            :name,
            :description
        )
    """;
}
