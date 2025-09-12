package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO extends DAO {

	public UsuarioDAO() {
		super();
		conectar();
	}
	
	public void finalize() {
		close();
	}
	
	public boolean insert(Usuario usuario) {
		boolean status = false;
		try {
			String sql = "INSERT INTO usuario (codigo, login, senha, sexo) VALUES (?, ?, ?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, usuario.getCodigo());
			st.setString(2, usuario.getLogin());
			st.setString(3, usuario.getSenha());
			st.setString(4, String.valueOf(usuario.getSexo()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public Usuario get(int codigo) {
		Usuario usuario = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario WHERE codigo = " + codigo;
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				usuario = new Usuario(rs.getInt("codigo"), rs.getString("login"),
						rs.getString("senha"), rs.getString("sexo").charAt(0));
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuario;
	}

	public List<Usuario> get() {
		return get("");
	}
	
	public List<Usuario> getOrderByLogin() {
		return get("login");
	}

	private List<Usuario> get(String orderBy) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Usuario u = new Usuario(rs.getInt("codigo"), rs.getString("login"), rs.getString("senha"),
						rs.getString("sexo").charAt(0));
				usuarios.add(u);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return usuarios;
	}

	public boolean update(Usuario usuario) {
		boolean status = false;
		try {
			String sql = "UPDATE usuario SET login = ?, senha = ?, sexo = ? WHERE codigo = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, usuario.getLogin());
			st.setString(2, usuario.getSenha());
			st.setString(3, String.valueOf(usuario.getSexo()));
			st.setInt(4, usuario.getCodigo());
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public boolean delete(int codigo) {
		boolean status = false;
		try {
			String sql = "DELETE FROM usuario WHERE codigo = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, codigo);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public int getMaxId() {
		int maxId = 0;
		try {
			Statement st = conexao.createStatement();
			ResultSet rs = st.executeQuery("SELECT MAX(codigo) FROM usuario");
			if (rs.next()) {
				maxId = rs.getInt(1);
			}
			st.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return maxId;
	}
}