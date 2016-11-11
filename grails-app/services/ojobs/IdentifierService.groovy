package ojobs

//import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

import org.apache.commons.lang.RandomStringUtils
import org.hibernate.ObjectNotFoundException
import groovy.sql.Sql
//import org.pih.warehouse.inventory.Transaction
//import org.pih.warehouse.order.Order
//import org.pih.warehouse.product.Product
//import org.pih.warehouse.requisition.Requisition
//import org.pih.warehouse.shipping.Shipment;

class IdentifierService {
	
	
		boolean transactional = true
		def grailsApplication
		def dataSource
		
		// Characters that should be included with each of the random number types
		static final String RANDOM_IDENTIFIER_ALPHABETIC_CHARACTERS = "ABCDEFGHJKMNPQRSTUVXYZ"
		static final String RANDOM_IDENTIFIER_ALPHANUMERIC_CHARACTERS = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ"
		static final String RANDOM_IDENTIFIER_NUMERIC_CHARACTERS = "0123456789"
	
		// Default random number formats
		static final String DEFAULT_ORDER_NUMBER_FORMAT = "NNNLLL"
		static final String DEFAULT_PRODUCT_NUMBER_FORMAT = "LLNN"
		static final String DEFAULT_REQUISITION_NUMBER_FORMAT = "NNNLLL"
		static final String DEFAULT_SHIPMENT_NUMBER_FORMAT = "NNNLLL"
		static final String DEFAULT_TRANSACTION_NUMBER_FORMAT = "AAA-AAA-AAA"
	   
		/**
		 * A: alphabetic
		 * L: letter
		 * N: numeric
		 * D: digit
		 * 0-9: digit
		 *
		 * @param format
		 * @return
		 */
		def generateIdentifier(String format) {
			if (!format || format.isEmpty()) {
				println "format must be specified"
				throw new IllegalArgumentException("Format pattern string must be specified")
			}
			
			String identifier = ""
			for (int i = 0; i < format.length(); i++) {
				switch(format[i]) {
					case 'N':
						identifier += RandomStringUtils.random(1, RANDOM_IDENTIFIER_NUMERIC_CHARACTERS)
						break;
					case 'D':
						identifier += RandomStringUtils.random(1, RANDOM_IDENTIFIER_NUMERIC_CHARACTERS)
						break;
					case 'L':
						identifier += RandomStringUtils.random(1, RANDOM_IDENTIFIER_ALPHABETIC_CHARACTERS)
						break;
					case 'A':
						identifier += RandomStringUtils.random(1, RANDOM_IDENTIFIER_ALPHANUMERIC_CHARACTERS)
						break;
					default:
						identifier += format[i]
						//throw new IllegalArgumentException("Unsupported format symbol: " + format[i])
					
				}
			}
			
			return identifier
		}
		
		/**
		 * Generate a random identifier of given length using alphanumeric characters.
		 *
		 * @param length
		 */
		def generateIdentifier(int length) {
			return RandomStringUtils.random(length, RANDOM_IDENTIFIER_ALPHANUMERIC_CHARACTERS)
		}
	
		
		/**
		 * @return
		 */
		def generateOrderIdentifier() {
			return generateIdentifier(DEFAULT_ORDER_NUMBER_FORMAT)
		}
	
		/**
		 * @return
		 */
		def generateProductIdentifier() {
			return generateIdentifier(DEFAULT_PRODUCT_NUMBER_FORMAT)
		}
		
		/**
		 * @return
		 */
		def generateRequisitionIdentifier() {
			return generateIdentifier(DEFAULT_REQUISITION_NUMBER_FORMAT)
		}
	
		/**
		 * @return
		 */
		def generateShipmentIdentifier() {
			return generateIdentifier(DEFAULT_SHIPMENT_NUMBER_FORMAT)
		}
	
		/**
		 * @return
		 */
		def generateTransactionIdentifier() {
			return generateIdentifier(DEFAULT_TRANSACTION_NUMBER_FORMAT)
		}
	
	
	
	
		void assignTransactionIdentifiers() {
			def sql = new Sql(dataSource)
			def rows = sql.rows("SELECT id FROM transaction where transaction_number IS NULL OR transaction_number = ''")
			rows.each { row ->
				try {
				String transactionCode = generateTransactionIdentifier()
				def transactionWithtransactionNumber = sql.firstRow("SELECT id FROM transaction where transaction_number = '" + transactionCode + "'")
				if (!transactionWithtransactionNumber) {
					sql.execute("UPDATE transaction SET transaction_number='"+transactionCode+"' where id='"+row.id+"'")
					sql.commit()
					}
				}
				catch (ObjectNotFoundException e) {
					println("Unable to assign identifier to transaction with ID " + row?.id + ": " + e.message)
	
				} catch (Exception e) {
					println("Unable to assign identifier to transaction with ID " + row?.id + ": " + e.message)
				}
			}
			sql.close()
		}
	
	
		void assignProductIdentifiers() {
			
			def sql = new Sql(dataSource)
			def rows = sql.rows("SELECT id FROM product where product_code IS NULL OR product_code = ''")
				rows.each { row ->
				try {
				String productCode = generateProductIdentifier()
					def productWithproductCode = sql.firstRow("SELECT id FROM product where product_code = '" + productCode + "'")
					if (!productWithproductCode) {
					sql.execute("UPDATE product SET product_code='"+productCode+"' where id='"+row.id+"'")
					sql.commit()
					}
				}
				catch (Exception e) {
				println "Inside product exception" + e
									}
			}
			sql.close()
		}
	
		void assignShipmentIdentifiers() {
			def sql = new Sql(dataSource)
			def rows = sql.rows("SELECT id FROM shipment where shipment_number IS NULL OR shipment_number = ''")
			rows.each { row ->
				try {
				String shipmentCode = generateShipmentIdentifier()
				def shipmentWithshipmentNumber = sql.firstRow("SELECT id FROM shipment where shipment_number = '" + shipmentCode + "'")
				if (!shipmentWithshipmentNumber) {
					sql.execute("UPDATE shipment SET shipment_number='"+shipmentCode+"' where id='"+row.id+"'")
					sql.commit()
					}
				}
				catch (Exception e) {
				println "Inside shipmentCode exception" + e
									}
			}
			sql.close()
		}
	
		void assignRequisitionIdentifiers() {
			def sql = new Sql(dataSource)
			def rows = sql.rows("SELECT id FROM requisition where request_number IS NULL OR request_number = ''")
			rows.each { row ->
				try {
				String requestCode = generateRequisitionIdentifier()
				def requestWithrequestNumber = sql.firstRow("SELECT id FROM requisition where request_number = '" + requestCode + "'")
				if (!requestWithrequestNumber) {
					sql.execute("UPDATE requisition SET request_number='"+requestCode+"' where id='"+row.id+"'")
					sql.commit()
					}
				}
				catch (Exception e) {
				println "Inside order exception" + e
									}
			}
			sql.close()
		}
	
		void assignOrderIdentifiers() {
			println "Inside Order "
			def sql = new Sql(dataSource)
			def rows = sql.rows("SELECT id FROM `order` where order_number IS NULL OR order_number = ''")
			println "Inside Order " + rows
			rows.each { row ->
				try {
				String orderCode = generateOrderIdentifier()
				def orderWithorderNumber = sql.firstRow("SELECT id FROM `order` where order_number = '" + orderCode + "'")
				if (!orderWithorderNumber) {
					sql.execute("UPDATE `order` SET order_number='"+orderCode+"' where id='"+row.id+"'")
					sql.commit()
					}
				}
				catch (Exception e) {
				println "Inside order exception" + e
									}
			}
			sql.close()
		}
	
	
	
	
	
}
