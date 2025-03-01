package org.example.repository.sql;

public class TruncateScanReport {

    public static final String SQL = """
        truncate table parser.scan_report cascade;
    """;
}
