package service;

import dao.UsuarioDAO;
import spark.Request;
import spark.Response;
import model.Usuario;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    
    public UsuarioService() {
        usuarioDAO = new UsuarioDAO();
    }
    

public String makeForm(int tipo, Usuario usuario, String msg) {
    String nomeArquivo = "formulario.html";
    form = "";
    try{
        Scanner entrada = new Scanner(new File(nomeArquivo), "UTF-8");
        while(entrada.hasNext()){
            form += (entrada.nextLine() + "\n");
        }
        entrada.close();
    }  catch (Exception e) {
        System.out.println("Erro ao ler 'formulario.html': " + e.getMessage());
    }
    
    String umUsuario = "";

    if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
        String url, titulo, buttonLabel;

        if (tipo == FORM_INSERT) {
            url = "/usuario/insert";
            titulo = "Adicionar Novo Usuário";
            buttonLabel = "Adicionar";
        } else {
            url = "/usuario/update/" + usuario.getCodigo();
            titulo = "Atualizar Usuário (Código " + usuario.getCodigo() + ")";
            buttonLabel = "Atualizar";
        }

        umUsuario += "\t<form action=\"" + url + "\" method=\"post\">";
        umUsuario += "\t<div class=\"form-header\">"; 
        umUsuario += "\t    <h3>" + titulo + "</h3>";
        
        if (tipo == FORM_UPDATE) {
            umUsuario += "\t    <a href=\"/usuario/list\" class=\"back-button\">Voltar</a>";
        }

        umUsuario += "\t</div>"; 
        umUsuario += "\t<div class=\"form-container\">";
        umUsuario += "\t    <div class=\"form-group\">";
        umUsuario += "\t        <label for=\"login\">Login:</label>";
        umUsuario += "\t        <input type=\"text\" name=\"login\" id=\"login\" value=\""+ usuario.getLogin() +"\">";
        umUsuario += "\t    </div>";
        umUsuario += "\t    <div class=\"form-group password-group\">";
        umUsuario += "\t        <label for=\"senha-input\">Senha:</label>";
        umUsuario += "\t        <input type=\"password\" name=\"senha\" id=\"senha-input\" value=\""+ usuario.getSenha() +"\">";
        umUsuario += "\t        <button type=\"button\" id=\"toggle-btn\" onclick=\"togglePasswordVisibility()\">Mostrar</button>";
        umUsuario += "\t    </div>";
        umUsuario += "\t    <div class=\"form-group\">";
        umUsuario += "\t        <label for=\"sexo\">Sexo:</label>";
        umUsuario += "\t        <input type=\"text\" name=\"sexo\" id=\"sexo\" maxlength=\"1\" value=\""+ (usuario.getSexo() == '\0' ? "" : usuario.getSexo()) +"\">";
        umUsuario += "\t    </div>";
        umUsuario += "\t    <div class=\"form-group\">";
        umUsuario += "\t        <input type=\"submit\" value=\"" + buttonLabel + "\">";
        umUsuario += "\t    </div>";
        umUsuario += "\t</div>";
        umUsuario += "\t</form>";

    } 
    else if (tipo == FORM_DETAIL) {
        umUsuario += "<div class=\"form-header\">";
        umUsuario += "    <h2>Detalhes do Usuário</h2>";
        umUsuario += "    <a href=\"/usuario/list\" class=\"back-button\">Voltar</a>"; // Adicionado botão voltar aqui também
        umUsuario += "</div>";
        umUsuario += "<ul>\n" +
                     "    <li><b>Código:</b> " + usuario.getCodigo() + "</li>\n" +
                     "    <li><b>Login:</b> " + usuario.getLogin() + "</li>\n" +
                     "    <li><b>Senha:</b> " + usuario.getSenha() + "</li>\n" +
                     "    <li><b>Sexo:</b> " + usuario.getSexo() + "</li>\n" +
                     "</ul>\n";
    }

    form = form.replaceFirst("<UM-USUARIO>", umUsuario);
    
    String listaUsuarios = "<table class=\"table table-striped\">" +
                           "<thead><tr><th>Código</th><th>Login</th><th>Detalhar</th><th>Editar</th><th>Excluir</th></tr></thead>" +
                           "<tbody>";

    List<Usuario> usuarios = usuarioDAO.get(); 
    
    for (Usuario u : usuarios) {
        listaUsuarios += "<tr>" +
                         "<td>" + u.getCodigo() + "</td>" +
                         "<td>" + u.getLogin() + "</td>" +
                         "<td><a href=\"/usuario/" + u.getCodigo() + "\">Detalhar</a></td>" +
                         "<td><a href=\"/usuario/update/" + u.getCodigo() + "\">Editar</a></td>" +
                         "<td><a href=\"javascript:confirmarDeleteUsuario('" + u.getCodigo() + "', '" + u.getLogin() + "');\">Excluir</a></td>" +
                         "</tr>";
    }
    
    listaUsuarios += "</tbody></table>";
    form = form.replaceFirst("<LISTAR-USUARIO>", listaUsuarios);
    
    form = form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ msg +"\">");
    
    return form;
}


    public Object add(Request request, Response response) {
        String login = request.queryParams("login");
        String senha = request.queryParams("senha");
        char sexo = request.queryParams("sexo").charAt(0);
    
        int id = usuarioDAO.getMaxId() + 1;
    
        Usuario usuario = new Usuario(id, login, senha, sexo);
        usuarioDAO.insert(usuario);
    
        response.status(201);
        return makeForm(FORM_INSERT, new Usuario(), "Usuário '" + login + "' criado!");
    }
    
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = (Usuario) usuarioDAO.get(id);

        if (usuario != null) {
            response.status(200);
            return makeForm(FORM_DETAIL, usuario, ""); 
        } else {
            response.status(404);
            return makeForm(FORM_INSERT, new Usuario(), "Usuário " + id + " não encontrado!");
        }
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = (Usuario) usuarioDAO.get(id);

        if (usuario != null) {
            response.status(200);
            return makeForm(FORM_UPDATE, usuario, ""); 
        } else {
            response.status(404);
            return makeForm(FORM_INSERT, new Usuario(), "Usuário " + id + " não encontrado!");
        }
    }
    
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);
        String msg;
    
        if (usuario != null) {
            usuario.setLogin(request.queryParams("login"));
            usuario.setSenha(request.queryParams("senha"));
            usuario.setSexo(request.queryParams("sexo").charAt(0));
            usuarioDAO.update(usuario);
            response.status(200); 
            msg = "Usuário (ID " + usuario.getCodigo() + ") atualizado!";
        } else {
            response.status(404); 
            msg = "Usuário (ID " + id + ") não encontrado!";
        }
        return makeForm(FORM_INSERT, new Usuario(), msg);
    }
    
    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        usuarioDAO.delete(id);
        return makeForm(FORM_INSERT, new Usuario(), "Usuário (" + id + ") excluído!");
    }
    
    public Object getAll(Request request, Response response) {
        return makeForm(FORM_INSERT, new Usuario(), "");
    }
}