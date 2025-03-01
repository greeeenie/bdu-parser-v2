package org.example.repository.sql;

public class UpdateCapecNameRu {

    public static final String SQL = """
        update parser.capec
        set name_ru = :name_ru
        where id = :id
    """;
}
