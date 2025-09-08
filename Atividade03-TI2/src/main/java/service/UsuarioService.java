package service;

import dao.UsuarioDAO;
import spark.Request;
import spark.Response;
import model.Usuario;

public class UsuarioService {
	
	private UsuarioDAO usuarioDAO;
	
	public UsuarioService() {
		usuarioDAO = new UsuarioDAO();
		usuarioDAO.conectar();
	}
	
	// Adiciona um novo usuário no banco de dados.
	public Object add(Request request, Response response) {
	    String login = request.queryParams("login");
	    String senha = request.queryParams("senha");
	    char sexo = request.queryParams("sexo").charAt(0);

	    int id = usuarioDAO.getMaxId() + 1;

	    Usuario usuario = new Usuario(id, login, senha, sexo);

	    usuarioDAO.add(usuario);

	    response.status(201); // 201 Created
	    return "Usuário criado com ID:"+id;
	}

	// Retorna os detalhes de um usuário específico em formato XML.
	public Object get(Request request, Response response) {
	    int id = Integer.parseInt(request.params(":id"));
	    
	    Usuario usuario = (Usuario) usuarioDAO.get(id);
	    
	    if (usuario != null) {
	        response.header("Content-Type", "application/xml");
	        response.header("Content-Encoding", "UTF-8");

	        return "<usuario>\n" + 
	                "\t<id>" + usuario.getCodigo() + "</id>\n" +
	                "\t<login>" + usuario.getLogin() + "</login>\n" +
	                "\t<senha>" + usuario.getSenha() + "</senha>\n" +
	                "\t<sexo>" + usuario.getSexo() + "</sexo>\n" +
	                "</usuario>\n";
	    } else {
	        response.status(404); // 404 Not found
	        return "Usuário " + id + " não encontrado.";
	    }
	}

	// Atualiza os dados de um usuário existente.
	public Object update(Request request, Response response) {
	    int id = Integer.parseInt(request.params(":id"));
	    
	    Usuario usuario = (Usuario) usuarioDAO.get(id);

	    if (usuario != null) {
	        usuario.setLogin(request.queryParams("login"));
	        usuario.setSenha(request.queryParams("senha"));
	        usuario.setSexo(request.queryParams("sexo").charAt(0));

	        usuarioDAO.update(usuario);
	        
	        return "Usuári com ID:"+id +" foi atualizado";
	    } else {
	        response.status(404); // 404 Not found
	        return "Usuário não encontrado.";
	    }
	}

	// Remove um usuário do banco de dados.
	public Object remove(Request request, Response response) {
	    int id = Integer.parseInt(request.params(":id"));

	    Usuario usuario = (Usuario) usuarioDAO.get(id);

	    if (usuario != null) {
	        usuarioDAO.remove(usuario);

	        response.status(200); // success
	        return "Usuári com ID:"+id +" foi removido";
	    } else {
	        response.status(404); // 404 Not found
	        return "Usuário não encontrado.";
	    }
	}

	// Retorna uma lista de todos os usuários em formato XML.
	public Object getAll(Request request, Response response) {
	    StringBuffer returnValue = new StringBuffer("<usuarios type=\"array\">");
	    for (Usuario usuario : usuarioDAO.getAll()) {
	        returnValue.append("\n<usuario>\n" + 
	                "\t<id>" + usuario.getCodigo() + "</id>\n" +
	                "\t<login>" + usuario.getLogin() + "</login>\n" +
	                "\t<senha>" + usuario.getSenha() + "</senha>\n" +
	                "\t<sexo>" + usuario.getSexo() + "</sexo>\n" +
	                "</usuario>\n");
	    }
	    returnValue.append("</usuarios>");
	    response.header("Content-Type", "application/xml");
	    response.header("Content-Encoding", "UTF-8");
	    return returnValue.toString();
	}

}
