create or replace view parser.table_3_view
as
select row_number() OVER () as "№",
       'CAPEC-' || cp.id    as "CAPEC",
       cp.name_ru           as "Наименование атаки"
from parser.scan_report sp
         left join parser.bdu b on b.id = sp.bdu_id
         left join parser.bdu_cwe bc on b.id = bc.bdu_id
         left join parser.cwe c on bc.cwe_id = c.id
         left join parser.cwe_capec cc on c.id = cc.cwe_id
         left join parser.capec cp on cc.capec_id = cp.id
where cp.id is not null
group by cp.id, cp.name_ru;
