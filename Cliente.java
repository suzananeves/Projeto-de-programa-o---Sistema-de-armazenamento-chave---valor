package ep2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;





public class Cliente {
	//criando uma variavel para guardar o primeiro IP
	static String ip1;
	//criando variaveis para guardar as portas dos e servidores servidor
	static int ps1;
	static int ps2;
	static int ps3;
	//porta que será enviada a menagem
	static int envio;
	//contador para o timestamp
	static int cont=0;
	//porta desse servidor
	static int minha_porta;
	//tabela hash local para guardar o chave e o timestamp
	static Hashtable<Integer, String> guarda_timestamp=new Hashtable<>();
	//thread que enviara mensagens
	public class Thread_servidor extends Thread{

	public void run() {
		while(true) {
			try {

				ServerSocket serverSocket = new ServerSocket(minha_porta);
				//o método acept cria um novo socket para a comunicacao
				Socket no = serverSocket.accept();
				
				//inputStream: inputstreamreader converte para texto, o getinputstream lê os bytes de um socket
				InputStreamReader is = new InputStreamReader(no.getInputStream());
				//bufered reader lê informacoes do teclado, arquivos, socket, etc, está lendo o input stream
				BufferedReader reader = new BufferedReader(is);

					
				OutputStream os = no.getOutputStream();
				//escrita de dados que poderao ser lidos por um datainputstream
				DataOutputStream writer = new DataOutputStream(os);
				String texto = reader.readLine();
				//imprime o texto recebido
				System.out.println(texto);
					
			

		} catch (Exception e) {

		}
	}
		}
		
	}

	
	
	public static void envia(String valor1) throws Exception{
		
		//criando um novo socket no ip e porta informados
		Socket S = new Socket(ip1, envio);

		//escrever informacoes que serao enviadas para outro lado
		OutputStream os = S.getOutputStream();


		DataOutputStream writer = new DataOutputStream(os);
		
		//getinputStream: entrada para ler informacoes enviadas pela saida, o output stream do host remoto 
		InputStreamReader is = new InputStreamReader(S.getInputStream());
		//buffered reader é para ler informacoes dp spcket
		BufferedReader reader = new BufferedReader(is);

		writer.writeBytes(valor1 +"\n");
		
		//lê e imprime resposta do servidor

		String response = reader.readLine();
		System.out.println(response);

}
	
	//cria o timestamp e adiciona na tabela hash local
	public static String new_timestamp(int chave1) {
		cont=cont+1;
		String timestamp2=minha_porta+"-"+cont;
		
		guarda_timestamp.put(chave1, timestamp2);
		return timestamp2;
		
	}
	
//sorteia qual servidor será enviada a mensagem
	public static void E_envio() {
		
		Random sorteio=new Random();
		
		int s=sorteio.nextInt(3)+1;
		
		if(s==1) {
			envio=ps1;
		}else {
			if(s==2) {
				envio=ps2;
			}else {
				if(s==3) {
					envio=ps3;
				}
			}
		}
		//System.out.println("o envio será para "+envio);
		
	}

	//função init que pega o ip e e as portas dos servidores
	public static void init() {
		Scanner entrada=new Scanner(System.in);
		
		System.out.println("Digite o IP:");
		ip1=entrada.next();
		
		System.out.println("Digite a porta do primeiro servidor");
		ps1=entrada.nextInt();
		
		System.out.println("Digite a porta do segundo servidor:");
		ps2=entrada.nextInt();
		System.out.println("Digite a porta do terceiro servidor");
		ps3=entrada.nextInt();
		E_envio();
		

	}
	//função put que pega os valores a serem armazenados
	public static void put() throws Exception {
		Scanner entrada=new Scanner(System.in);
		System.out.println("Qual a chave a ser armazenada?");
		int chave=entrada.nextInt();
		System.out.println("Qual o valor a ser armazenado?");
		String valor=entrada.next();
		
		String times=new_timestamp(chave);
		//String json = "{\"porta_cliente\":"+minha_porta+",\"serv1\":"+ps1+",\"serv2\":"+ps2+",\"serv3\":"+ps3+",\"replica\":0,\"status\":\"put\",\"chave\":"+chave+", \"valor\":"+valor+",\"timestamp\":"+times+"}";
		//criação do json que será a mensagem enviada
		String json = "{\"porta_cliente\":"+minha_porta+",\"serv1\":"+ps1+",\"serv2\":"+ps2+",\"serv3\":"+ps3+",\"replica\":0,\"status\":\"put\",\"chave\":"+chave+", \"valor\":"+valor+",\"timestamp\":"+times+"}";
		
		envia(json);

	}
	//inicia o get para pegar as informaçoes para enviar o get ao servidor
	public static void get() throws Exception{
		Scanner entrada=new Scanner(System.in);
		E_envio();
		System.out.println("Qual a key a ser recuperada?");
		int chave=entrada.nextInt();
		String timesta=guarda_timestamp.get(chave);
		String json = "{\"porta_cliente\":"+minha_porta+",\"serv1\":"+ps1+",\"serv2\":"+ps2+",\"serv3\":"+ps3+",\"replica\":0,\"status\":\"get\",\"chave\":"+chave+", \"valor\":\"nulo\",\"timestamp\":"+timesta+"}";
		
		
		envia(json);
	}
	
	//thread que mantem o menu
	public class Thread_menu extends Thread{
		public void run() {
			Scanner entrada = new Scanner(System.in);
			String tipo = " ";
			
			
			while(true) {
				
				System.out.println("Escolha entre as opções: INIT, PUT e GET \n");
				tipo=entrada.next();
				
				
				if(tipo.equalsIgnoreCase("INIT")) {
					init();
					
				}
				
				if(tipo.equalsIgnoreCase("PUT")) {
					try {
						put();
					} catch (Exception e) {

					}
					
				}
				
				if(tipo.equalsIgnoreCase("GET")) {
					try {
						get();
					} catch (Exception e) {

					}
					
				}
				
			}
		}
	}


	//main
	public static void main(String[] args) throws Exception {
		//inicia o sorteio do servidor
		Random sorteio=new Random();
		//faz um sorteio para ter um valor entre 2001 e 3000 como porta para o servidor
		minha_porta=sorteio.nextInt(1000)+2001;
		
		
		Cliente c1 = new Cliente();
		
		
		
		//inicialização das threads
		

			Thread_servidor thread = c1.new Thread_servidor();

			Thread_menu thread_2 = c1.new Thread_menu();

			
			thread_2.start();

			thread.start();

			
			

			
		
		

	}

}
