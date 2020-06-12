package com.my.agents.utils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;

@SuppressWarnings("deprecation")
public class TemplateFactory {
	public static RestTemplate getTemplate() throws KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {
		return getTemplate(false);
	}

	public static RestTemplate getTemplate(boolean jsononly)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
				new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] certificate,
							String authType) {
						return true;
					}
				}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(cm).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				httpclient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		boolean found = false;
		if (jsononly) {
			for (int i = restTemplate.getMessageConverters().size() - 1; i >= 0; i--) {
				HttpMessageConverter<?> httpMessageConverter = restTemplate
						.getMessageConverters().get(i);
				if (httpMessageConverter.getSupportedMediaTypes().indexOf(
						MediaType.TEXT_HTML) >= 0) {
					restTemplate.getMessageConverters().remove(i);
				}
			}
		}
		for (HttpMessageConverter<?> httpMessageConverter : restTemplate
				.getMessageConverters()) {

			if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
				if (jsononly) {
					List<MediaType> mediaList = new ArrayList<MediaType>();
					mediaList.add(MediaType.APPLICATION_JSON);
					mediaList.add(MediaType.TEXT_HTML);
					((MappingJackson2HttpMessageConverter) httpMessageConverter)
							.setSupportedMediaTypes(mediaList);
				}
				MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
				jacksonConverter.getObjectMapper().disable(
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				found = true;
				break;
			}
		}
		if (!found) {
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converter.getObjectMapper().disable(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			restTemplate.getMessageConverters().add(converter);
		}

		return restTemplate;
	}

}
