package dados;

import java.sql.SQLException;

import modelo.UrlExample;

public interface InterfaceUrlExampleDAO {

	void converterUrl(UrlExample url) throws SQLException;

	UrlExample recuperarUrl(String shortUrl) throws SQLException;

	UrlExample verificaUrl(String longUrl) throws SQLException;

	void atualizaUrl(UrlExample url) throws SQLException;

	boolean existeUrl(String shortUrl) throws SQLException;
	
	

}
