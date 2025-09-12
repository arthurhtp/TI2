package app;

import static spark.Spark.*;
import service.UsuarioService;

public class Principal {
	
	private static UsuarioService usuarioService = new UsuarioService();

	public static void main(String[] args) {
		port(3333);
		
		staticFiles.location("/public");
		
		get("/", (request, response) -> {
			response.redirect("/usuario/list");
			return null;
		});
		
		get("/usuario/list", (request, response) -> usuarioService.getAll(request, response));

		get("/usuario/:id", (request, response) -> usuarioService.get(request, response));
		
		post("/usuario/insert", (request, response) -> usuarioService.add(request, response));

		get("/usuario/update/:id", (request, response) -> usuarioService.getToUpdate(request, response));
		
		post("/usuario/update/:id", (request, response) -> usuarioService.update(request, response));
		
		get("/usuario/delete/:id", (request, response) -> usuarioService.remove(request, response));
	}
}