DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE cancellation_save_complete
(
	p_document INT UNSIGNED,
	p_konesh_eval VARCHAR(10),
	p_new_status INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		    GET DIAGNOSTICS CONDITION 1
		        @errno = MYSQL_ERRNO,
		        @sqlstate = RETURNED_SQLSTATE,
		        @text = MESSAGE_TEXT;
		
		RESIGNAL;
	END;
			
	UPDATE
		cancellations can
	SET
		can.status = p_new_status,
		can.response = p_konesh_eval,
		can.curStage = 'PAC'
	WHERE
		can.document = p_document;
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE konesh_claim_pending_uuid
(
    p_number_of_records INT
)
BEGIN
	SELECT 
		can.document,
		can.cfdiuuid,
		kdt.dataId
	FROM cancellations can
		LEFT JOIN koneshdata kdt
			ON can.document = kdt.originId 
	WHERE can.status IN (0, 3, 4, 5, 6)
	LIMIT p_number_of_records;
		-- 1=ENCONTRADO KONESH
		-- 2=NO ENCONTRADO KONESH
	    
		-- 3=PENDIENTE KONESH
		-- 4=ACEPTADO PARCIAL
		-- 5=ACEPTADO MANUAL
		-- 6=ERROR
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE konesh_claim_uuid
(
	p_uuid VARCHAR(36),
	OUT o_claimed BIT
)
BEGIN
	DECLARE V_PROCESSING INT DEFAULT -1000;
	
	UPDATE 
		cancellations
	SET
		status = V_PROCESSING
	WHERE
		cfdiuuid = p_uuid
	AND status <> V_PROCESSING;
	
	SET o_claimed = IFNULL(ROW_COUNT(), 0);
		
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE konesh_request_complete
(
	  p_data_id       INT UNSIGNED
	, p_receiver_id  INT
    , p_UUID         VARCHAR(36)
    , p_rec_rfc       VARCHAR(13)
    , p_emi_rfc       VARCHAR(13)
    
    , p_proc_type     VARCHAR(6)
    , p_order_number  VARCHAR(10)
    , p_final_result  VARCHAR(10)
    , p_status       VARCHAR(50)
    , p_message      VARCHAR(500)
    
    , p_currency     VARCHAR(5)
    , p_total        VARCHAR(25)
    , p_doc_type      VARCHAR(45)
    , p_serie        VARCHAR(45)
    , p_folio        VARCHAR(40)
    
    , p_origin_id     INT UNSIGNED
    , p_doc_date      VARCHAR(8)
    
    , OUT o_konesh_eval VARCHAR(10)
    , OUT o_new_status INT
)
BEGIN
	-- DECLARE v_data_id INT UNSIGNED;
    DECLARE v_konesh_eval INT;
    DECLARE v_new_status INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		    GET DIAGNOSTICS CONDITION 1
		        @errno = MYSQL_ERRNO,
		        @sqlstate = RETURNED_SQLSTATE,
		        @text = MESSAGE_TEXT;
		
		RESIGNAL;
	END;
	
	SET p_total     = IF(TRIM(p_total) = ''   OR  ISNULL(p_total), '0.0', p_total );
	
	
	SET v_konesh_eval = 0;  -- 0 = Ir a siguiente nivel de evaluación
	SET v_new_status  = fn_getNewStatus( p_proc_type, LEFT(p_final_result, 2), p_status);
	
 	IF ( v_new_status = 1 ) THEN -- 1=ENCONTRADO KONESH
		SET v_konesh_eval = fn_KoneshEval( p_final_result, p_status, p_proc_type);
	END IF;
 	
 	
 	SET o_new_status = v_new_status;
 	
	SET o_konesh_eval = 
		IF (v_konesh_eval = 1, 'Aceptacion', 
 			IF (v_konesh_eval = 2, 'Rechazo', 
	 			IF (v_konesh_eval = 3, 'Manual', 
	 				'Pendiente')
	 			)
		); -- El último caso es 0
	
	IF (p_data_id IS NULL) THEN
		INSERT INTO koneshdata(
			receiverId,
			execDate,
			UUID,
			recRFC,
			emiRFC,
			
			procType,
			orderNumber,
			finalResult,
			status,
			message,
			
			currency,
			total,
			docType,
			serie,
			folio,
			
			originId,
			docDate
		)VALUES(
			p_receiver_id,
			CURRENT_TIMESTAMP(),
			p_UUID,
			p_rec_rfc,
			p_emi_rfc,
			
			p_proc_type,
			p_order_number,
			p_final_result,
			p_status,
			p_message,
			
			p_currency,
			p_total,
			p_doc_type,
			p_serie,
			p_folio,
			
			p_origin_id,
			p_doc_date
		);
	
	ELSE
	
		UPDATE
			koneshdata
		SET
			execDate     = CURRENT_TIMESTAMP(),
			UUID         = p_UUID,
			recRFC       = p_rec_rfc,
			emiRFC       = p_emi_rfc,
			procType     = p_proc_type,
			orderNumber  = p_order_number,
			finalResult  = p_final_result,
			status       = p_status,
			message      = p_message,
			currency     = p_currency,
			total        = p_total,
			docType      = p_doc_type,
			serie        = p_serie,
			folio        = p_folio,
			-- originId     = p_origin_id,
			docDate      = p_doc_date
		WHERE
			dataId = p_data_id;
	END IF;
END $$
DELIMITER ;
