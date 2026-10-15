
-- Selección de Empresas Registradas Activas
SELECT cid, crfc, ccode, cname, cdate, cupdt, sapversion FROM companies WHERE cid IN(1,2,3,4,5,6,7,43) AND clock = 'N';

+-----+--------------+-------+--------------------------------------+---------------------+---------------------+------------+
| cid | crfc         | ccode | cname                                | cdate               | cupdt               | sapversion |
+-----+--------------+-------+--------------------------------------+---------------------+---------------------+------------+
|   1 | CCM080101IL8 | 051   | CERVECERIA CUAUHTEMOC MOCTEZUMA      | 2023-04-21 09:57:39 | 2024-10-03 19:24:52 | ECC        |
|   2 | CCM010710UU1 | 083   | CERVEZAS CUAUHTEMOC MOCTEZUMA        | 2023-04-24 15:20:32 | 2024-10-03 17:41:31 | ECC        |
|   3 | CNC820428674 | 169   | CONTROLADORA DE NEGOCIOS COMERCIALES | 2023-04-24 15:22:07 | 2024-10-03 19:26:11 | S4H        |
|   4 | CBC540330LE0 | 049   | CARTA BLANCA DE CIUDAD JUAREZ        | 2023-04-25 10:23:57 | 2023-04-25 10:23:57 | ECC        |
|   5 | SAI960101SG9 | 055   | SISTEMA AMBIENTAL INDUSTRIAL         | 2023-04-26 15:55:03 | 2025-01-29 17:59:14 | ECC        |
|   6 | BRC960101LD7 | 059   | BIENES RAICES CERMOC                 | 2023-04-26 15:56:52 | 2025-01-29 18:07:07 | ECC        |
|   7 | CBO8408017Q5 | 065   | CARTA BLANCA DE OCCIDENTE            | 2023-04-26 16:14:31 | 2023-04-26 16:14:31 | ECC        |
|  43 | TBO1203142Q6 | 560   | EXPINS                               | 2023-04-27 12:28:02 | 2024-10-03 19:29:16 | ECC        |
+-----+--------------+-------+--------------------------------------+---------------------+---------------------+------------+

-- Simulación de Solicitudes de Aceptación de Cancelación de CFDIs de Proveedores de Heineken mediante PAC Connexial
-- CA1100 - Se recibío la respuesta de la petición de forma exitosa.
-- CA1101 - No existen peticiones para el RFC Receptor.
-- Sociedad, Código, Tipo, Detalle, Mensaje.
-- SELECT @rowid AS id_request;
call portalcfdi.crequest_insert(1, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(2, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(3, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(4, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(5, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(6, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(7, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);
call portalcfdi.crequest_insert(43, 1100, 'success', null, 'Pruebas Unitarias de Integración SAP ECC y S4H', @rowid);

SELECT * from portalcfdi.crequests WHERE message = 'Pruebas Unitarias de Integración SAP ECC y S4H';

+---------+---------+--------+-------------------------------------------------+------------+------------+---------------------+
| request | cmpnyid | detail | message                                         | statusCode | statusType | created             |
+---------+---------+--------+-------------------------------------------------+------------+------------+---------------------+
|  574226 |       1 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:21:59 |
|  574227 |       2 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:16 |
|  574228 |       3 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:24 |
|  574229 |       4 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:33 |
|  574230 |       5 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:40 |
|  574231 |       6 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:47 |
|  574232 |       7 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:22:57 |
|  574233 |      43 | NULL   | Pruebas Unitarias de Integración SAP ECC y S4H  |       1100 | success    | 2026-09-07 16:23:05 |
+---------+---------+--------+-------------------------------------------------+------------+------------+---------------------+

-- Vincular Petición con Solicitud
call portalcfdi.cancellation_insert(574226, 'DA25EB46-D0CA-4897-A518-4130EA71AC89');
call portalcfdi.cancellation_insert(574226, '3D946B69-0103-4092-A49A-5571511576AC');

call portalcfdi.cancellation_insert(574227, '2EBFFBA1-CA53-55D4-BC0F-F90B67CCA4F4');
call portalcfdi.cancellation_insert(574227, '433FCFED-182B-472A-A5B1-2E8AC19AE60F');
call portalcfdi.cancellation_insert(574227, '62441C36-384C-44CF-9504-66ED6ED9C414');
call portalcfdi.cancellation_insert(574227, '7F85E9B4-56DD-4F5F-8BFA-55E277AAD2A1');
call portalcfdi.cancellation_insert(574227, '7D6C60FE-869B-4212-9801-95618CFCBE2D');
call portalcfdi.cancellation_insert(574227, 'B0064CEB-772E-468B-B7FF-00B25747FBB9');

call portalcfdi.cancellation_insert(574228, 'DB554FD3-60FF-4B1D-854E-061FE254906D');
call portalcfdi.cancellation_insert(574228, '98041E3B-6628-4AB2-805C-BB4D8C419580');
call portalcfdi.cancellation_insert(574228, '5ED40425-35D6-4CEF-9638-6E5F58162365');
call portalcfdi.cancellation_insert(574228, '84B87446-DB31-4FD8-804A-CBAB88C94E56');
call portalcfdi.cancellation_insert(574228, '0302EC1D-66D3-4E82-B9A1-BE9448811C67');

call portalcfdi.cancellation_insert(574230, 'D830FA19-69BE-42AD-9CEB-E548B99C9AAA');
call portalcfdi.cancellation_insert(574230, '1C1A305C-63F8-4D2B-87E6-F6F214EE526A');

call portalcfdi.cancellation_insert(574233, '5EA660A8-F541-4FE3-8806-BD423FF81AED');

-- Procesar e Insertar Resultados de Konesh con el Siguiente Formato:
-- UUID|RFC-Receptor|RFC-Emisor|Tipo-Proceso|Número-Pedido|Resultado-Final|Estatus|Mensaje|Moneda|Total|Tipo-Documento|Serie|Folio|OriginID|DataID|docDate
-- <uuid>|<rfcReceptor>|<rfcEmisor>|<tipoProceso>|<numPedido>|<resultadoFinal>|<estatus>|<mensaje>|<moneda>|<total>|<tipoDocumento>|<serie>|<folio>|0|null|<fechaDoc>
-- call  portalcfdi.koneshrequests_save(); Reprocesar
call portalcfdi.koneshData_save(11830, 'DA25EB46-D0CA-4897-A518-4130EA71AC89|CCM080101IL8|LCA110919CR8|01|4403137705|ACEPTADO|CREADO|000010352765 - |MXN|136631.57|I|OI|2605114|0|null|20260513|3D946B69-0103-4092-A49A-5571511576AC|CCM080101IL8|FME971022Q44|01|4402714027|ACEPTADO|CREADO|000010106821 - |MXN|101213.60|I|FXXE|4274366|0|null|20260312');

call portalcfdi.koneshData_save(11831, '2EBFFBA1-CA53-55D4-BC0F-F90B67CCA4F4|CCM010710UU1|PRI21111964A|01|4403061280|ACEPTADO|CREADO|000010508087 - |MXN|98286.800000|I|FC|9884|0|null|20260626|433FCFED-182B-472A-A5B1-2E8AC19AE60F|CCM010710UU1|OORE860703AMA|01|4403254912|ACEPTADO|CREADO|000010776911 - |MXN|43679.800000|I||1747|0|null|20260827|62441C36-384C-44CF-9504-66ED6ED9C414|CCM010710UU1|OORE860703AMA|01|4403254912|ACEPTADO|CREADO|000010776733 - |MXN|77952.000000|I||1746|0|null|20260827|7F85E9B4-56DD-4F5F-8BFA-55E277AAD2A1|CCM010710UU1|OORE860703AMA|01|4403254912|ACEPTADO|CREADO|000010776578 - |MXN|92510.000000|I||1745|0|null|20260827|7D6C60FE-869B-4212-9801-95618CFCBE2D|CCM010710UU1|GNO110824HG3|01|6170708540|ACEPTADO|CREADO|000010772441 - |MXN|15776.000000|I|A|8955|0|null|20260810|B0064CEB-772E-468B-B7FF-00B25747FBB9|CCM010710UU1|AEA080108LN1|01|6170657573|ACEPTADO|BLOQUEADO|000010647942 - 083 51002170282026|MXN|157180.000000|I|E|1927|0|null|20260727');

call portalcfdi.koneshData_save(11832, 'DB554FD3-60FF-4B1D-854E-061FE254906D|CNC820428674|SIR220112ES3|01|4403217843|ACEPTADO|CANCELADO|000010657372 - 169 51000050292026|MXN|78262.370000|I|A|709|0|null|20260806|98041E3B-6628-4AB2-805C-BB4D8C419580|CNC820428674|ROCL810925DL3|01|6170707631|ACEPTADO|CREADO|000010771399 - |MXN|21000.000000|I|A|179|0|null|20260804|5ED40425-35D6-4CEF-9638-6E5F58162365|CNC820428674|SOIA5607118XA|10|4700075968|ACEPTADO|CONTABILIZADO|5200273961|MXN|8494.240000|I|||0|null|20260806|84B87446-DB31-4FD8-804A-CBAB88C94E56|||||NA||||0||||0|null||0302EC1D-66D3-4E82-B9A1-BE9448811C67|||||NA||||0||||0|null|');

call portalcfdi.koneshData_save(11833, 'D830FA19-69BE-42AD-9CEB-E548B99C9AAA|SAI960101SG9|ORH100325SY1|01|6170515812|ACEPTADO|CONTABILIZADO|000009871836 - 055 51000064002026|MXN|6217.600000|I|J|187813|0|null|20260122|1C1A305C-63F8-4D2B-87E6-F6F214EE526A|SAI960101SG9|ORH100325SY1|01|6170534567|ACEPTADO|CREADO|000009871796 - 51069190822026|MXN|1952.570000|I|J|187291|0|null|20260109');

call portalcfdi.koneshData_save(11834, '5EA660A8-F541-4FE3-8806-BD423FF81AED|TBO1203142Q6|TMO601122G75|01|4403260792|ACEPTADO|CREADO|000010790747 - |MXN|5590.30000|I|C|396993|0|null|20260820');

-- Metadatos SAT
-- ID-Descarga, Estatus, UUID, Nombre-Emisor, Nombre-Receptor, RFC-Emisor, RFC-Receptor, satCertDate, fecha, total, sello, serie, folio, moneda, tipo, fecha-cancelado
call portalcfdi.metadata_insert(58770, 1, '84B87446-DB31-4FD8-804A-CBAB88C94E56', 'MA. ANTONIA RIOJAS RODRIGUEZ', 'CONTROLADORA DE NEGOCIOS COMERCIALES', 'RIRA5001261M2', 'CNC820428674', '2023-11-15 10:56:45', '2023-11-15 10:56:21', 11167.97, 'ZTrcTA==', null, null, 'MXN', 'I', null);

-- SELECT * FROM portalcfdi.cancellations WHERE cfdiuuid IN('DA25EB46-D0CA-4897-A518-4130EA71AC89', '3D946B69-0103-4092-A49A-5571511576AC', '2EBFFBA1-CA53-55D4-BC0F-F90B67CCA4F4', '433FCFED-182B-472A-A5B1-2E8AC19AE60F', '62441C36-384C-44CF-9504-66ED6ED9C414', '7F85E9B4-56DD-4F5F-8BFA-55E277AAD2A1', '7D6C60FE-869B-4212-9801-95618CFCBE2D', 'B0064CEB-772E-468B-B7FF-00B25747FBB9', 'DB554FD3-60FF-4B1D-854E-061FE254906D', '98041E3B-6628-4AB2-805C-BB4D8C419580', '5ED40425-35D6-4CEF-9638-6E5F58162365', '84B87446-DB31-4FD8-804A-CBAB88C94E56', '0302EC1D-66D3-4E82-B9A1-BE9448811C67', 'D830FA19-69BE-42AD-9CEB-E548B99C9AAA', '1C1A305C-63F8-4D2B-87E6-F6F214EE526A', '5EA660A8-F541-4FE3-8806-BD423FF81AED');

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

select * from portalcfdi.crequests where request > 574224;
select * from portalcfdi.cancellations where requestp > 574224;
SELECT dataId, originId, requestId, receiverId, UUID, finalResult, status, procType, execDate FROM koneshdata WHERE requestId > 11830;

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Replicación y Debug para identificar el error.
-- Simulación de una nueva petición y se cambió el último caractér por una X para simular nuevos UUIDs
call portalcfdi.crequest_insert(3, 1100, 'success', null, 'Debug UnitTest SAP ECC S4H Integration Rule 5', @rowid);

call portalcfdi.cancellation_insert(574234, 'DB554FD3-60FF-4B1D-854E-061FE254906X');
call portalcfdi.cancellation_insert(574234, '98041E3B-6628-4AB2-805C-BB4D8C41958X');
call portalcfdi.cancellation_insert(574234, '5ED40425-35D6-4CEF-9638-6E5F5816236X');
call portalcfdi.cancellation_insert(574234, '84B87446-DB31-4FD8-804A-CBAB88C94E5X');
call portalcfdi.cancellation_insert(574234, '0302EC1D-66D3-4E82-B9A1-BE9448811C6X');

-- El formato de los datos es el siguiente:
-- UUID|RFC-Receptor|RFC-Emisor|Tipo-Proceso|Número-Pedido|Resultado-Final|Estatus|Mensaje|Moneda|Total|Tipo-Documento|Serie|Folio|OriginID|DataID|docDate
call portalcfdi.koneshData_save(11835, 'DB554FD3-60FF-4B1D-854E-061FE254906X|CNC820428674|SIR220112ES3|01|4403217843|ACEPTADO|CANCELADO|000010657372 - 169 51000050292026|MXN|78262.370000|I|A|709|0|null|20260806|98041E3B-6628-4AB2-805C-BB4D8C41958X|CNC820428674|ROCL810925DL3|01|6170707631|ACEPTADO|CREADO|000010771399 - |MXN|21000.000000|I|A|179|0|null|20260804|5ED40425-35D6-4CEF-9638-6E5F5816236X|CNC820428674|SOIA5607118X|10|4700075968|ACEPTADO|CONTABILIZADO|5200273961|MXN|8494.240000|I|||0|null|20260806|84B87446-DB31-4FD8-804A-CBAB88C94E5X|||||NA||||0||||0|null||0302EC1D-66D3-4E82-B9A1-BE9448811C6X|||||NA||||0||||0|null|');

SELECT dataId, originId, requestId, receiverId, UUID, finalResult, status, procType, execDate FROM koneshdata WHERE requestId = 11835;
-- Al veririfcar el procedimiento portalcfdi.koneshData_save, el campo OriginID debe ser el ID de cada documento, y se envió 0(Cero), por lo tanto no podía hacer la relación para actualizarlo con la respuesta. No afectó a los demás porque ya estaban predeterminados a llegar a evaluarse hasta obtener información de SAP, y lo mismo sucedió con este registro que no debía llegar a esta etapa/fase. Detal modo que por eso se vió afectado el resultado.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Se creo una nueva solicitud y documentos.
call portalcfdi.crequest_insert(3, 1100, 'success', null, 'Debug2 UnitTest SAP ECC S4H Integration Rule 5', @rowid);


call portalcfdi.cancellation_insert(574235, 'DB554FD3-60FF-4B1D-854E-061FE25490XX');
call portalcfdi.cancellation_insert(574235, '98041E3B-6628-4AB2-805C-BB4D8C4195XX');
call portalcfdi.cancellation_insert(574235, '5ED40425-35D6-4CEF-9638-6E5F581623XX');
call portalcfdi.cancellation_insert(574235, '84B87446-DB31-4FD8-804A-CBAB88C94EXX');
call portalcfdi.cancellation_insert(574235, '0302EC1D-66D3-4E82-B9A1-BE9448811CXX');

-- Esta vez se asignó el OriginID correspondiente, lo cual se hace de manera automática en un proceso de la aplicación integral. Finalmente se obtuvo el resultado esperado.
call portalcfdi.koneshData_save(11835, 'DB554FD3-60FF-4B1D-854E-061FE25490XX|CNC820428674|SIR220112ES3|01|4403217843|ACEPTADO|CANCELADO|000010657372 - 169 51000050292026|MXN|78262.370000|I|A|709|191151|null|20260806|98041E3B-6628-4AB2-805C-BB4D8C4195XX|CNC820428674|ROCL810925DL3|01|6170707631|ACEPTADO|CREADO|000010771399 - |MXN|21000.000000|I|A|179|191152|null|20260804|5ED40425-35D6-4CEF-9638-6E5F581623XX|CNC820428674|SOIA5607118X|10|4700075968|ACEPTADO|CONTABILIZADO|5200273961|MXN|8494.240000|I|||191153|null|20260806|84B87446-DB31-4FD8-804A-CBAB88C94EXX|||||NA||||0||||191154|null||0302EC1D-66D3-4E82-B9A1-BE9448811CXX|||||NA||||0||||191155|null|');


