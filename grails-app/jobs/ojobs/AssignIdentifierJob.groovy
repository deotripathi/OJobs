package ojobs



class AssignIdentifierJob {
   def identifierService

    static triggers = {
        simple startDelay: 60000, repeatInterval: 300000l   // run every 5 minutes
	}


	
	
	def execute() {
		println "JOB Started"
        identifierService.assignProductIdentifiers()
        identifierService.assignShipmentIdentifiers()
        identifierService.assignRequisitionIdentifiers()
        identifierService.assignTransactionIdentifiers()
		identifierService.assignOrderIdentifiers()
	}
}
