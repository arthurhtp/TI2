package somador;

import java.util.*;

public class SomaDoisNumeros {
	
	public static void main (String args[]){
		//Abertura do Scanner
		Scanner scan = new Scanner(System.in);
		//Variáveis
		int n1, n2;
		
		System.out.println("Digite um número");
		n1 = scan.nextInt();
		System.out.println("Digite outro número");
		n2 = scan.nextInt();
		scan.close();
		//Mostrar na tela
		System.out.println("Soma:" + (n1+n2));
		}
}
