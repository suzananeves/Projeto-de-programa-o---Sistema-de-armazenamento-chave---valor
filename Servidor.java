package ep2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

import com.google.gson.*;




public class Servidor {
	static boolean eh_lider=false;
	static int porta_envia=0;
	static int porta_local;
	
	//a classe mensagem, reponsavel por pegar e transformar o json recebido em um objeto java
	public class Estrutura {
		int porta_cliente=0;
		int serv1=0;
		int serv2=0;
		int serv3=0;
		int replica=0;
		String status = null;
	    String valor = null;
	    int    chave = 0;
	    String timestamp=null;

	}
	
	public class Thread_servidor extends Thread{

		private Socket no = null;
		
		public Thread_servidor(Socket node) {
			no = node;
			
		}
		
		static Hashtable<Integer, String> ha1=new Hashtable<>();
		
		public static String envia(String v1) throws Exception{
			//criando um novo socket no ip e porta informados
			Socket S = new Socket("127.0.0.1", porta_envia);

			//escrever informacoes que serao enviadas para outro lado
			OutputStream os = S.getOutputStream();


			DataOutputStream writer = new DataOutputStream(os);
			
			//getinputStream: entrada para ler informacoes enviadas pela saida, o output stream do host remoto 
			InputStreamReader is = new InputStreamReader(S.getInputStream());
			//buffered reader é para ler informacoes dp spcket
			BufferedReader reader = new BufferedReader(is);

			writer.writeBytes(v1 +"\n");
			 //le o socket escrito no outro no
			if(porta_envia==10097 || porta_envia==10098 || porta_envia==10099) {
				String response = reader.readLine();
				System.out.println(response);
				
			}

			
			return " ";
		}
		

		//leitura do json
		public static String confere_gson(String v1) throws Exception {
			Servidor c1=new Servidor();
			Gson gson = new Gson();

			
			Estrutura novo_gson=c1.new Estrutura();
			
			
			//transformando o json em um objeto java

			novo_gson=gson.fromJson(v1, Estrutura.class);

			if(novo_gson.status.equals("put")) {
				
				//funçoes do lider

				if(eh_lider) {

					String retorno;
					String v2=novo_gson.timestamp;
					String v3=novo_gson.valor;
					String v4=v3+","+v2;
					//adiciona valores na tabela hash
					retorno =tabela_hash(novo_gson.chave,v4);
					novo_gson.valor=v4;
					novo_gson.replica=1;
					String n_json=gson.toJson(novo_gson);
					
					//replica o json

					for(int i=0;i<3;i++) {
						if(i==0) {
							if(novo_gson.serv1!=porta_local) {
								porta_envia=novo_gson.serv1;
								envia(n_json);

							}
						}
						if(i==1) {
							if(novo_gson.serv2!=porta_local) {
								porta_envia=novo_gson.serv2;
								envia(n_json);
							}
						}
						if(i==2) {
							if(novo_gson.serv3!=porta_local) {
								porta_envia=novo_gson.serv3;
								envia(n_json);
							}
						}
					}
					
					porta_envia=novo_gson.porta_cliente;
					//retorna o put ok para o cliente
					envia("PUT_OK "+novo_gson.timestamp);


					return retorno;
				}else {
					//item para servidores não lideres adicionarem os valoes nas tabelas locais
					if(novo_gson.replica==1) {
						String retorno;
						retorno=tabela_hash(novo_gson.chave, novo_gson.valor);

						return "REPLICATION_OK";
					}else {
						envia(v1);
					}
				}
				
			}
			//get
			if(novo_gson.status.equals("get")) {
				String retorno;
				retorno = recupera(novo_gson.chave, novo_gson.porta_cliente, novo_gson.timestamp);
				return retorno;
				
			}
			return " ";
			
		}
		//função para tabela hash
		public static String tabela_hash(int chave, String valor) {
			
			ha1.put(chave, valor);
			
			System.out.println(ha1);
			
			
			return " ";
			
		}
		//função complementar ao get
		public static String recupera(int chave, int porta, String timest) throws Exception {
			
			String timestring = ha1.get(chave);
			//System.out.println("timestring "+timestring);

			porta_envia=porta;

			//retorna null se valor não for encontrado
			if(timestring==null) {
				timestring="NULL";
				return timestring;
			}else {
				String[] timestamp1=timestring.split(",");

				String[] timestampcont=timestamp1[1].split("-");

				
				int converte=Integer.parseInt(timestampcont[1]);

				String[] timestnovo=timest.split("-");
				int converte2=Integer.parseInt(timestnovo[1]);

				if(converte>=converte2) {

					return timestring;
					
				}else {

					return "TRY_OTHER_SERVER_OR_LATER.";
				}
			}
			
			
			

			
		}
		
		//run executa a thread
		public void run() {
			try {
				//inputStream: inputstreamreader converte para texto, o getinputstream le os bytes de um socket
				InputStreamReader is = new InputStreamReader(no.getInputStream());
				//bufered reader le informacoes do teclado, arquivos, socket, etc, está lendo o input stream
				BufferedReader reader = new BufferedReader(is);

				
				//escrever informacoes que serao enviadas para outro lado, o getoutputstream recebe um socket e devolve um outputsstream
				//saida para escrever as informacoes que serao lidas pelo entrada do host remoto
				OutputStream os = no.getOutputStream();
				//escrita de dados que poderao ser lidos por um datainputstream
				DataOutputStream writer = new DataOutputStream(os);
				//le o socket escrito no outro no
				String texto = reader.readLine();

				texto = confere_gson(texto);

				writer.writeBytes(texto+"\n");
			} catch (Exception e) {

			}
		}


	}
	
	
	
	public static void main(String[] args) throws Exception {
		//cria o mecanismo para escutar conexoes por determinada porta
		Scanner entrada = new Scanner(System.in);
		System.out.println("Qual o ip desse servidor?");
		String ip_local=entrada.next();
		System.out.println("Qual a porta desse servidor?");
		porta_local=entrada.nextInt();
		System.out.println("Qual o ip do lider?");
		String servidor=entrada.next();
		System.out.println("Qual a porta do lider?");
		int porta_servidor=entrada.nextInt();
		
		
		if(porta_local==porta_servidor) {
			eh_lider=true;

		}else {
			porta_envia=porta_servidor;

		}
		
		ServerSocket serverSocket = new ServerSocket(porta_local);
		
		Servidor c1 = new Servidor();
		
		
		
		while (true) {
			
			System.out.println("Esperando conexao");
			//o método acept cria um novo socket para a comunicacao
			Socket no = serverSocket.accept();
			System.out.println("Conexao aceita");
			

			//construtor new e thread start inicia a thread
			Thread_servidor thread = c1.new Thread_servidor(no);
			thread.start();
			
			
		}
	}


}
