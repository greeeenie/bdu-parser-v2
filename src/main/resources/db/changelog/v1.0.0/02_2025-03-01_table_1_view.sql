create or replace view parser.table_1_view
as
select row_number() OVER ()                                               as "№",
       replace(sp.full_bdu_ids, ' ', E'\n')                               as "BDU",
       string_agg(distinct 'CWE-' || c.id, E'\n')                         as "CWE",
       string_agg(distinct (select 'CAPEC-' || icp.id
                            from parser.capec icp
                            where icp.id = cp.id
                              and icp.likelihood = 'High'), E'\n')        as "CAPEC High",
       string_agg(distinct (select 'CAPEC-' || icp.id
                            from parser.capec icp
                            where icp.id = cp.id
                              and icp.likelihood = 'Medium'), E'\n')      as "CAPEC Medium",
       string_agg(distinct (select 'CAPEC-' || icp.id
                            from parser.capec icp
                            where icp.id = cp.id
                              and icp.likelihood = 'Low'), E'\n')         as "CAPEC Low",
       string_agg(distinct (select 'CAPEC-' || icp.id
                            from parser.capec icp
                            where icp.id = cp.id
                              and icp.likelihood = 'Unspecified'), E'\n') as "No chance"
from parser.scan_report sp
         left join parser.bdu b on b.id = sp.bdu_id
         left join parser.bdu_cwe bc on b.id = bc.bdu_id
         left join parser.cwe c on bc.cwe_id = c.id
         left join parser.cwe_capec cc on c.id = cc.cwe_id
         left join parser.capec cp on cc.capec_id = cp.id
group by sp.id;
