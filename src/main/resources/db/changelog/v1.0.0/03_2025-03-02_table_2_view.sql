create or replace view parser.table_2_view
as
select row_number() OVER () as "№",
       replace(sp.full_bdu_ids, ' ', E'\n') as "BDU",
       sp.description as "Наименование уязвимости"
from parser.scan_report sp;

