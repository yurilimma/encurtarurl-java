package dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.UrlExample;

public class UrlExampleDAO implements InterfaceUrlExampleDAO{
	Connection conexao;
	public UrlExampleDAO(Connection conexao){
		this.conexao = conexao;
	}
	
	@Override
	public void converterUrl(UrlExample longUrl)throws SQLException{

		String comando = "insert into urlexample(longurl,shorturl,customalias,qtdacesso) values (?, ?, ?, ?)";
		PreparedStatement ps = this.conexao.prepareStatement(comando);
		ps.setString(1,longUrl.getLongUrl());
		ps.setString(2, longUrl.getShortUrl());
		ps.setString(3, longUrl.getCustomAlias());
		ps.setInt(4, longUrl.getQtdAcesso());	// Caso tenha Date ps.setDate(3, new Date(p.getdtCadastro().getTime()));
		ps.execute();
		
	}
	
	@Override
	public UrlExample recuperarUrl(String shortUrl) throws SQLException{
		
		String comando = "select * from urlexample where shorturl = ?";
		PreparedStatement ps = this.conexao.prepareStatement(comando);
		ps.setString(1,shortUrl);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			int idUrl = rs.getInt(1);
			String longUrl = rs.getString(2);
			String customAliasUrl = rs.getString(4);
			int qtdAcessoUrl = rs.getInt(5);
			
			
			
			UrlExample url = new UrlExample(idUrl,longUrl,shortUrl,customAliasUrl,qtdAcessoUrl);
			return url;
		}
		return null;
	}
	@Override
	public boolean existeUrl(String shortUrl) throws SQLException{
		
		String comando = "select * from urlexample where shorturl = ?";
		PreparedStatement ps = this.conexao.prepareStatement(comando);
		ps.setString(1,shortUrl);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			
			return true;
		}
		return false;
	}
	
	@Override
	public void atualizaUrl(UrlExample url) throws SQLException{
		String comando = "update urlexample set qtdacesso=? where shorturl = ?";
		PreparedStatement ps = this.conexao.prepareStatement(comando);
		ps.setInt(1,url.getQtdAcesso());
		ps.setString(2, url.getShortUrl());
		ps.execute();
	}
	
	@Override
	public UrlExample verificaUrl(String longUrl) throws SQLException{
		
		String comando = "select * from urlexample where longurl = ?";
		PreparedStatement ps = this.conexao.prepareStatement(comando);
		ps.setString(1,longUrl);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			int idUrl = rs.getInt(1);
			String shortUrl = rs.getString(3);
			String customAliasUrl = rs.getString(4);
			int qtdAcessoUrl = rs.getInt(5);
			
			
			
			UrlExample url = new UrlExample(idUrl,longUrl,shortUrl,customAliasUrl,qtdAcessoUrl);
			return url;
		}
		return null;
	}
	
}
