import java.util.*;

public class Principal {

	public static void main(String[] args) {

		DAO dao = new DAO();

		dao.conectar();

		int resposta = -1;
		Scanner scan = new Scanner(System.in);

		while (resposta != 0) {
			System.out.println(
					"Selecione a ação desejada: \n 1-Listar \n 2-Inserir \n 3-Excluir \n 4-Atualizar \n 5-Sair");
			resposta = scan.nextInt();

			switch (resposta) {
			case 5:
				System.out.println("FIM DO PROGRAMA");
				break;
			case 1:
				System.out.println("Você escolheu a opção 1: Listar.");
				Usuario[] usuarios = dao.getUsuarios();
				int len = usuarios.length;
				for (int i = 0; i < len; i++) {
					System.out.println(usuarios[i].toString());
				}
				break;
			case 2:
				System.out.println("Você escolheu a opção 2: Inserir.");

				System.out.println("Digite o código:");
				int codigo = scan.nextInt();
				scan.nextLine();
				System.out.println("Digite o login:");
				String login = scan.nextLine();
				System.out.println("Digite a senha:");
				String senha = scan.nextLine();
				System.out.println("Digite o sexo (M/F):");
				char sexo = scan.nextLine().charAt(0);

				Usuario novoUsuario = new Usuario(codigo, login, senha, sexo);
				dao.inserirUsuario(novoUsuario);
				break;
			case 3:
				System.out.println("Você escolheu a opção 3: Excluir.");
				System.out.println("Digite o código do usuário a ser excluído:");
				int codigoExcluir = scan.nextInt();
				dao.excluirUsuario(codigoExcluir);
				break;
			case 4:
				System.out.println("Você escolheu a opção 4: Atualizar.");
				System.out.println("Digite o código do usuário a ser atualizado:");
				int codigoAtualizado = scan.nextInt();
				scan.nextLine();
				System.out.println("Digite o novo login:");
				String novoLogin = scan.nextLine();
				System.out.println("Digite a nova senha:");
				String novaSenha = scan.nextLine();
				System.out.println("Digite o novo sexo (M/F):");
				char novoSexo = scan.nextLine().charAt(0);

				Usuario usuarioAtualizado = new Usuario(codigoAtualizado, novoLogin, novaSenha, novoSexo);
				dao.atualizarUsuario(usuarioAtualizado);
				break;
			default:
				System.out.println("Opção inválida! Por favor, escolha um número de 1 a 5.");
			}
		}
		
		scan.close();

		dao.close();
	}
}