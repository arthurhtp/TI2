package dao;

import java.sql.*;
import model.Usuario;

public class UsuarioDAO {
	private Connection connection;
	
	public UsuarioDAO() {
		connection = null;
	}
	
	// Método para conectar ao banco de dados PostgreSQL
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String url = "jdbc:postgresql://localhost:5432/teste";
		String username = "postgres";
		String password = "2004";
		
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Conexão efetuada com o postgres!");
			return true;
		} catch (ClassNotFoundException e) {
			System.err.println("Driver não encontrado. " + e.getMessage());
			return false;
		} catch (SQLException e) {
			System.err.println("Erro na conexão. " + e.getMessage());
			return false;
		}
	}
	
	// Método para fechar a conexão
	public boolean close() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			System.err.println("Erro ao fechar a conexão. " + e.getMessage());
			return false;
		}
	}
	
	// Método para adicionar um usuário
	public void add(Usuario usuario) {
		String sql = "INSERT INTO usuario (codigo, login, senha, sexo) VALUES (" +
					 usuario.getCodigo() + ", '" + usuario.getLogin() + "', '" +
					 usuario.getSenha() + "', '" + usuario.getSexo() + "');";
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			System.err.println("Erro ao adicionar usuário: " + e.getMessage());
		}
	}
	
	// Método para obter um usuário pelo código
	public Usuario get(int codigo) {
		Usuario usuario = null;
		String sql = "SELECT * FROM usuario WHERE codigo = " + codigo;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				usuario = new Usuario(rs.getInt("codigo"), rs.getString("login"),
									  rs.getString("senha"), rs.getString("sexo").charAt(0));
			}
			st.close();
		} catch (SQLException e) {
			System.err.println("Erro ao obter usuário: " + e.getMessage());
		}
		return usuario;
	}
	
	// Método para atualizar um usuário
	public void update(Usuario usuario) {
		String sql = "UPDATE usuario SET login = '" + usuario.getLogin() + "', senha = '" +
					 usuario.getSenha() + "', sexo = '" + usuario.getSexo() + "' WHERE codigo = " +
					 usuario.getCodigo();
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			System.err.println("Erro ao atualizar usuário: " + e.getMessage());
		}
	}
	
	// Método para remover um usuário
	public void remove(Usuario usuario) {
		String sql = "DELETE FROM usuario WHERE codigo = " + usuario.getCodigo();
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			System.err.println("Erro ao remover usuário: " + e.getMessage());
		}
	}
	
	// Método para obter todos os usuários
	public Usuario[] getAll() {
		Usuario[] usuarios = null;
		try {
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM usuario ORDER BY codigo");
			if (rs.next()) {
				rs.last();
				usuarios = new Usuario[rs.getRow()];
				rs.beforeFirst();
				for (int i = 0; rs.next(); i++) {
					usuarios[i] = new Usuario(rs.getInt("codigo"), rs.getString("login"),
											  rs.getString("senha"), rs.getString("sexo").charAt(0));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Erro ao listar usuários: " + e.getMessage());
		}
		return usuarios;
	}
	
	// Método para pegar o maior ID
	public int getMaxId() {
		int maxId = 0;
		String sql = "SELECT MAX(codigo) AS max_id FROM usuario";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				maxId = rs.getInt("max_id");
			}
			st.close();
		} catch (SQLException e) {
			System.err.println("Erro ao obter o maior ID: " + e.getMessage());
		}
		return maxId;
	}
}