package org.example.repository.sql;

public class InsertScanReportBdu {

    public static final String SQL = """
        insert into parser.scan_report (
            bdu_id,
            full_bdu_ids,
            description
        ) values (
            :bdu_id,
            :full_bdu_ids,
            :description
        )
    """;
}
