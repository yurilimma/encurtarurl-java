package modelo;

import java.io.Serializable;

public class UrlExample implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8704937383113200564L;
	
	private int id;
	private String longUrl;
	private String shortUrl;
	private String customAlias;
	private int qtdAcesso;
	
	
	public UrlExample(){
		
	}
	public UrlExample(int _id, String _longUrl, String _shortUrl, String _customAlias, int _qtdAcesso){
		this.id = _id;
		this.longUrl = _longUrl;
		this.shortUrl = _shortUrl;
		this.customAlias = _customAlias;
		this.qtdAcesso = _qtdAcesso;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLongUrl() {
		return longUrl;
	}
	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	public int getQtdAcesso() {
		return qtdAcesso;
	}
	public void setQtdAcesso(int qtdAcesso) {
		this.qtdAcesso = qtdAcesso;
	}
	public String getCustomAlias() {
		return customAlias;
	}
	public void setCustomAlias(String customAlias) {
		this.customAlias = customAlias;
	}
	

}
