package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Bdu;
import org.example.model.Capec;
import org.example.model.Cwe;
import org.example.model.ScanReportBdu;
import org.example.repository.sql.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MainRepository {

    private final JdbcClient jdbcClient;

    public void updateCapecNameRu(Integer id, String name) {
        jdbcClient.sql(UpdateCapecNameRu.SQL)
                .param("id", id)
                .param("name_ru", name)
                .update();
    }

    public List<Capec> getCapecs() {
        return jdbcClient.sql(SelectCapecsWithoutNameRu.SQL)
                .query(Capec.class)
                .list();
    }

    public boolean existsCwe(Integer id) {
        return jdbcClient.sql(ExistsCwe.SQL)
                       .param("id", id)
                       .query(Integer.class)
                       .single() > 0;
    }

    public boolean existsCapec(Integer id) {
        return jdbcClient.sql(ExistsCapec.SQL)
                       .param("id", id)
                       .query(Integer.class)
                       .single() > 0;
    }

    public void save(Bdu bdu) {
        jdbcClient.sql(InsertBdu.SQL)
                .param("id", bdu.getId())
                .param("name", bdu.getName())
                .param("description", bdu.getDescription())
                .update();
    }

    public void save(Cwe cwe) {
        jdbcClient.sql(InsertCwe.SQL)
                .param("id", cwe.getId())
                .param("name", cwe.getName())
                .update();
    }

    public void save(Capec capec) {
        jdbcClient.sql(InsertCapec.SQL)
                .param("id", capec.getId())
                .param("name", capec.getName())
                .param("likelihood", capec.getLikelihood())
                .update();
    }

    public void saveRelation(String bduId, Integer cweId) {
        jdbcClient.sql(InsertBduCweRelation.SQL)
                .param("bdu_id", bduId)
                .param("cwe_id", cweId)
                .update();
    }

    public void saveRelation(Integer cweId, Integer capecId) {
        jdbcClient.sql(InsertCweCapecRelation.SQL)
                .param("cwe_id", cweId)
                .param("capec_id", capecId)
                .update();
    }

    public void saveScanReportBdu(ScanReportBdu scanReportBdu) {
        jdbcClient.sql(InsertScanReportBdu.SQL)
                .param("bdu_id", scanReportBdu.getBduId())
                .param("full_bdu_ids", scanReportBdu.getFullBduIds())
                .param("description", scanReportBdu.getDescription())
                .update();
    }

    public void truncateScanReport() {
        jdbcClient.sql(TruncateScanReport.SQL).update();
    }
}
