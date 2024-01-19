package appdeploy;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.wso2.carbon.application.mgt.stub.upload.CarbonAppUploaderStub;
import org.wso2.carbon.application.mgt.stub.upload.types.carbon.UploadedFileItem;

public class CarbonAppUploaderClient {
	private static final Logger logger = Logger
			.getLogger(CarbonAppUploaderClient.class.getName());
	private CarbonAppUploaderStub st;
	

	public static void main(String[] args) throws Exception {
		String baseUrl = args[0];
		String username = args[1];
		String password = args[2];
		String path = args[3];
		
		File root = new File(path);
		
		String[] directories = root.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
			});
		
		File file = null;
		
		for (int i = 0; i < directories.length; i++) {
			String name = directories[i];
			File subDiretory = new File(path+"/"+name+"/target");
			File[] matches = subDiretory.listFiles(new FilenameFilter()
			{
			  public boolean accept(File dir, String name)
			  {
			     return name.endsWith(".car");
			  }
			});
			
			if(matches != null)
			for (int j = 0; j < matches.length; j++) {
				file = matches[j];
				logger.log(Level.INFO,LogMessages.FILE_FOUND.getMessage());
				break;
			};
		}
		
		String trustStore = args[4];
		String trustStorePassword = args[5];
		
		System.setProperty("javax.net.ssl.trustStore",
				trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

		CarbonAppUploaderClient client = new CarbonAppUploaderClient();
		
		if(file!=null) {
			client.init(baseUrl, username, password);
			client.deploy(file);
		}else {
			logger.log(Level.INFO,LogMessages.FILE_NOT_FOUND.getMessage());
		}
		
	}

	private void init(String baseUrl, String username, String password) throws AxisFault {
		// TODO Auto-generated method stub

		try {
			logger.log(Level.INFO, LogMessages.LOAD_WSDL.getMessage());
			st = new CarbonAppUploaderStub(
					String.format("%s/services/CarbonAppUploader", baseUrl));
			logger.log(Level.INFO, LogMessages.CONNECTION_SERVICE.getMessage());
			ServiceClient client = st._getServiceClient();
			logger.log(Level.INFO, LogMessages.LOGIN_ON_SERVICE.getMessage());
			Options client_options = client.getOptions();
			Authenticator authenticator = new Authenticator();
			authenticator.setUsername(username);
			authenticator.setPassword(password);
			authenticator.setPreemptiveAuthentication(true);
			client_options.setProperty(
					org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
					authenticator);
			client.setOptions(client_options);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, LogMessages.CONNECTION_SERVICE.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private void deploy(File file) throws RemoteException {
		// TODO Auto-generated method stub
		logger.log(Level.INFO, LogMessages.DEPLOY_SERVICE_START.getMessage());

		FileDataSource carDataSource = new FileDataSource(file);
			
		DataHandler dataHandler = new DataHandler(carDataSource);

		UploadedFileItem i = new UploadedFileItem();

		i.setDataHandler(dataHandler);
		i.setFileName(file.getName());
		i.setFileType("jar");

		UploadedFileItem[] ii = new UploadedFileItem[1];
		ii[0] = i;
		try {
			st.uploadApp(ii);
			logger.log(Level.INFO, LogMessages.DEPLOY_SERVICE_FINISH.getMessage());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO,LogMessages.DEPLOY_SERVICE_FAULT.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

}
