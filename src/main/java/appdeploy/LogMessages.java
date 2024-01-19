package appdeploy;

public enum LogMessages {
	LOAD_WSDL("LOAD_WSDL", "Carregando o WSDL do deploy do serviço"),
	CONNECTION_SERVICE("CONNECTION_SERVICE", "Conectando ao deploy do serviço"),
	CONNECTION_SERVICE_FAULT("CONNECTION_SERVICE_FAULT", "Falha conexão do deploy do serviço"),
	LOGIN_ON_SERVICE("LOGIN_ON_SERVICE", "Logando usuario no do deploy do serviço"),
	DEPLOY_SERVICE_START("DEPLOY_SERVICE_START", "Inicializando deploy do serviço"),
	DEPLOY_SERVICE_FINISH("DEPLOY_SERVICE_FINISH", "Terminado deploy do serviço"),
	DEPLOY_SERVICE_FAULT("DEPLOY_SERVICE_FAULT", "Falha no deploy do serviço"),
	FILE_FOUND("FILE_FOUND", "Ficheiro CAR encontrado"),
	FILE_NOT_FOUND("FILE_NOT_FOUND", "Ficheiro CAR não encontrado");
	
	
	private final String code;
	private final String message;
	
	LogMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	
}
