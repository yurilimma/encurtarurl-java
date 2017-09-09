package dados;

import java.sql.Connection;
import java.sql.DriverManager;

public class FabricaConexao {
	private Connection conexao;
	
	public Connection fazerConexao(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conexao = DriverManager.getConnection("jdbc:mysql://localhost/sakila", "root","0123456");
		}catch (Exception e){
			e.printStackTrace();
		}
		return conexao;
	}
	
	public void fecharConexao(){
		try{
			this.conexao.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
