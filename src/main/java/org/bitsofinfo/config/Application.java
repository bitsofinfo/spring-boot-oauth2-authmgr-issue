package org.bitsofinfo.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;


@SpringBootApplication(exclude = {
		
								  DataSourceTransactionManagerAutoConfiguration.class, 
								  DataSourceAutoConfiguration.class, 
								  
								  HibernateJpaAutoConfiguration.class,
								  JpaRepositoriesAutoConfiguration.class,
								  SpringDataWebAutoConfiguration.class,
								  
								  JndiConnectionFactoryAutoConfiguration.class,
								  
								  MongoAutoConfiguration.class,
								  MongoDataAutoConfiguration.class,
								  MongoRepositoriesAutoConfiguration.class,
								  
								  JmsAutoConfiguration.class,
								  
								  RedisAutoConfiguration.class,
								  RedisRepositoriesAutoConfiguration.class,
								  
								  JestAutoConfiguration.class,
								  HazelcastAutoConfiguration.class,
								  HazelcastJpaDependencyAutoConfiguration.class,
								  FreeMarkerAutoConfiguration.class,
								  
								  ValidationAutoConfiguration.class,
								  
								  
								  
								  /*
								  SecurityAutoConfiguration.class,
								  SecurityFilterAutoConfiguration.class,
								  */
								  
								  
								  AopAutoConfiguration.class,
								  
								})
@Import({ CoreConfigs.class,
	      AuthorizationServerConfig.class,
	  	  RestServicesConfig.class, 
		  SoapServicesConfig.class, 
		})
public class Application extends org.springframework.boot.web.support.SpringBootServletInitializer {
		
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) throws Exception {		
		
		ClassPathResource xmlFileResource = new ClassPathResource("legacyApplication.xml");
		
		// boot the context
		ApplicationContext context = SpringApplication.run(new Object[] {
				xmlFileResource,
				Application.class
				
		}, args); 
		

		/**
		 * Note now publish our soap-services in CXF...
		 * not all may be available depending on the config XML
		 * we bootstrapped from (i.e. auth vs full mode)
		 */
		try {
			Endpoint ep = (Endpoint)context.getBean("testSoapEndpoint");
			soapActionStripEndpoint(ep);
			ep.publish("/testSoapEndpoint");
		} catch(NoSuchBeanDefinitionException e) {
			logger.warn("Error publishing testsoapendpoint:" + e.getMessage());
		}
		
	}
	
	private static void soapActionStripEndpoint(Endpoint ep) {
		EndpointImpl epi = (EndpointImpl)ep;
		List<Interceptor<? extends Message>> interceptors = new ArrayList<Interceptor<? extends Message>>();
		interceptors.add(new SoapActionStripInterceptor());
		epi.setInInterceptors(interceptors);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		//tomcat.addAdditionalTomcatConnectors(createSslConnector());
		return tomcat;
	}
	
	/*
	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		try {
			File keystore = getKeyStoreFile();
			File truststore = keystore;
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(TOMCAT_SSL_PORT);
			protocol.setSSLEnabled(true);
			protocol.setKeystoreFile(keystore.getAbsolutePath());
			protocol.setKeystorePass(BOF_KEYSTORE_PW);
			
			// This worked in spring boot 1.2.5, but after upgrading we got 
			// this error: https://github.com/spring-projects/spring-boot/issues/7069
			//protocol.setTruststoreFile(truststore.getAbsolutePath());
			//protocol.setTruststorePass(BOF_KEYSTORE_PW);
			
			protocol.setKeyAlias(TOMCAT_SSL_KEY_ALIAS);
			return connector;
		}
		catch (IOException ex) {
			throw new IllegalStateException("cant access keystore: [" + "keystore"
					+ "] or truststore: [" + "keystore" + "]", ex);
		}
	}
	
	private File getKeyStoreFile() throws IOException {
		
		try {
			if (BOF_KEYSTORE_FILE instanceof ClassPathResource) {
				File copyTo = new File(System.getProperty("java.io.tmpdir") + "/" + BOF_KEYSTORE_FILE.getFilename() );
				logger.debug("getKeyStoreFile() copying embedded keystore file FROM: " + BOF_KEYSTORE_FILE + " TO: " + copyTo.getAbsolutePath());
				FileCopyUtils.copy(BOF_KEYSTORE_FILE.getInputStream(), new FileOutputStream(copyTo));
				return copyTo;
			}
			
			return BOF_KEYSTORE_FILE.getFile();
		}
		catch (Exception ex) {
			throw new IOException("Could not load keystore file from "
					+ "Resource["+BOF_KEYSTORE_FILE.toString()+" " + BOF_KEYSTORE_FILE.getFilename()+"]! " + ex.getMessage());
		}
	}
	*/

	
	
}
