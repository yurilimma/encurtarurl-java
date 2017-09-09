package controle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import dados.FabricaConexao;
import dados.UrlExampleDAO;
import modelo.UrlExample;

@ManagedBean
@ViewScoped 
public class UrlBean implements Serializable{

	/**
	 * 
	 */
	public static String GOOGLE_URL_SHORTNER = "https://www.googleapis.com/urlshortener/v1/url?";
	public static String API_KEY = "AIzaSyB8qRL-d8tssrJaPSpmLfbPCsRr0io7yK8";
	public static String HTTP = "http://";
	public static String HTTPS = "https://";
	
	/**  Com clicks
	 * GET https://www.googleapis.com/urlshortener/v1/url?shortUrl=https%3A%2F%2Fgoo.gl%2FG9KTSg&projection=ANALYTICS_CLICKS&key={YOUR_API_KEY}
	 * shortUrlClicks param, exemplo: map.get("shortUrlClicks")
	 */
	
	/** Sem clicks
	 * GET https://www.googleapis.com/urlshortener/v1/url?shortUrl=https%3A%2F%2Fgoo.gl%2FG9KTSg&key={YOUR_API_KEY}
	
	 */
	
	private static final long serialVersionUID = 3795770593797960229L;

	private UrlExample url;
	
	public void prepararNovo(){
		try{
			this.url = new UrlExample();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void converterNova(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			if(this.url.getLongUrl().substring(0, 7).equals(HTTP) || this.url.getLongUrl().substring(0, 8).equals(HTTPS)){
				FabricaConexao fabrica = new FabricaConexao();
				Connection conn = fabrica.fazerConexao();
				UrlExampleDAO dao = new UrlExampleDAO(conn);
				
				/** verifica se a url já foi convertida anteriormente para não gerar duplicidade na base */
				if((dao.verificaUrl(this.url.getLongUrl())) == null){
					/** converte a url */
					this.url.setShortUrl(encurtarUrl(this.url.getLongUrl()));
					this.url.setQtdAcesso(0);
					this.url.setCustomAlias(this.url.getShortUrl().substring(15, 21));
					
					dao.converterUrl(this.url);
			        context.addMessage(null, new FacesMessage("Url Gerada com sucesso!",  "Short Url: " + this.url.getShortUrl()));
				}else{
					this.url = dao.verificaUrl(this.url.getLongUrl());
					this.url.setQtdAcesso(Integer.valueOf(buscarQtClicks(this.url.getShortUrl())));
					dao.atualizaUrl(this.url);
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Url já foi gerada anteriormente!",  "Short Url: " + this.url.getShortUrl() + " Qtd Acessos: " + this.url.getQtdAcesso()));
				}
				fabrica.fecharConexao();
			}else{
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "URL INVÁLIDA. " , " "));
			}
				
			}catch(Exception e){
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao gerar URL. " , e.getMessage()));
			}
	}
	public void buscarUrl(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			FabricaConexao fabrica = new FabricaConexao();
			Connection conn = fabrica.fazerConexao();
			UrlExampleDAO dao = new UrlExampleDAO(conn);
			if(!dao.existeUrl(this.getUrl().getShortUrl())){
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "{ERR_CODE: 002, Description:SHORTENED URL NOT FOUND}" , ""));
			}else{
				this.url = dao.recuperarUrl(this.url.getShortUrl());
				this.url.setQtdAcesso(Integer.valueOf(buscarQtClicks(this.url.getShortUrl())));
				dao.atualizaUrl(this.url);
				
		        context.addMessage(null, new FacesMessage("Qtd Acessos: " + this.url.getQtdAcesso(),  "Long Url: " + this.url.getLongUrl() + " CustomAlias: " + this.url.getCustomAlias()));
				fabrica.fecharConexao();
			}
			
		}catch(Exception e){
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "Erro ao buscar URL. " , " "));
		}
	}
	
	/** Converte URL API GOOGLE DEVELOPER SHORTENER */
	public static String encurtarUrl(String urlInteira){
		
		String data = "{\"longUrl\": \""+urlInteira+"\"}";
		try{
			URL url = new URL (GOOGLE_URL_SHORTNER + "key="  + API_KEY );
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "toolbar");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			
			OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
			output.write(data);
			output.flush();
			
			BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String result = "";
			String line;
			while((line= response.readLine())!= null){
				result += line;
			}
			
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(result, Map.class);
			
			output.close();
			response.close();
			return (String) map.get("id");
		}catch(Exception e){
			return urlInteira;
		}
	}
	public static String buscarQtClicks(String shortUrl){
		
		try{
			URL url = new URL (GOOGLE_URL_SHORTNER + "shortUrl=" + shortUrl + "&projection=ANALYTICS_CLICKS" + "&key="  + API_KEY );
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "toolbar");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			
			BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String result = "";
			String line;
			while((line= response.readLine())!= null){
				result += line;
			}
			/** passa de string para json */
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(result, Map.class);
			
			response.close();
			map = (Map) map.get("analytics");
			map = (Map) map.get("allTime");
			return (String) map.get("shortUrlClicks");
		}catch(Exception e){
			return null;
		}
	}
	
	
	public UrlBean(){
		this.url = new UrlExample();
	}

	public UrlExample getUrl() {
		return url;
	}

	public void setUrl(UrlExample url) {
		this.url = url;
	}
	
}


