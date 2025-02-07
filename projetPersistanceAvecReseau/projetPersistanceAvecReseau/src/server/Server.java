package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import Message.DemandeInter;
import Message.Distribution;
import Message.Message;
import utils.FileManager;
import worker.Worker;

public class Server {

	/*
	 * Hastable ayant comme key un BigInteger ( nombre max de l'intervalle envoye
	 * aux workers ) et ayant comme value une Hashtable ayant comme key le nombre et
	 * comme value la persistance de ce nombre
	 */

	// Creation du serveur



	// OK
	// fonction qui actualise le nombre d'occurrences par valeur de la persistance

	// Pour utiliser un autre port pour le serveur, l'exécuter avec la commande :
	// java ServeurMC 8081
	public static void main(String[] args) throws Exception {
		final int maxWorkerPC = 50;
		
		int numWorkerPC = 0;
		
		int port = 2222;
		
		// 1 - Ouverture du ServerSocket par le serveur
		
		ServerSocket s = new ServerSocket(port);
		
		System.out.println("SOCKET ECOUTE CREE => " + s);
		
		
		byte cores; // nombre de coeurs disponibles | byte => 1 octet alors que int => 4
		cores = (byte) Runtime.getRuntime().availableProcessors();

		BigInteger INTERVAL = new BigInteger("50000");
		
		Distribution distrib; // Interval a donner au workerPC
		distrib = new Distribution(BigInteger.ZERO, INTERVAL.multiply(BigInteger.valueOf(cores * 2)), INTERVAL  );
		
		while (numWorkerPC < maxWorkerPC) {
			/*
			 * 2 - Attente d'une connexion worker pc (la méthode s.accept() est bloquante
			 * tant qu'un client ne se connecte pas)
			 */
			Socket soc = s.accept();
			/*
			 * 3 - Pour gérer plusieurs clients simultanément, le serveur attend que les
			 * clients se connectent, et dédie un thread à chacun d'entre eux afin de le
			 * gérer indépendamment des autres clients
			 */

			ConnexionWorkerPC cc = new ConnexionWorkerPC(numWorkerPC, soc, distrib);
			System.out.println("NOUVELLE CONNEXION - SOCKET => " + soc);
			numWorkerPC++;
			cc.start();
		}
		s.close();

	}

}

class ConnexionWorkerPC extends Thread {

	private int id;
	
	private boolean arret = false;
	
	private Socket s;
	
	private Message messagein;
	
	private Distribution distributionInter;
	
	private ObjectOutputStream out;
	
	private ObjectInputStream in;
	
	private String file = "";
	
	private DemandeInter di;


	private Hashtable<Byte, BigInteger> occurences; // Byte au lieu de Integer car byte => 1 octet alors que int => 4

	private ArrayList<String> highestPers; // liste des nombres avec la plus grande persistance

	private byte cores; // nombre de coeurs disponibles | byte => 1 octet alors que int => 4
	private byte perstMax; // persistance max trouvee | byte car en theorie perstMax < 20

	private float median; // mediane des persistances

	private final BigInteger INTERVAL; // taille des intervalles envoyes aux workers
	
	private final BigInteger MIN; // minimum de l'intervalle total
	
	private final BigInteger MAX; // maximum de l'intervalle total
	
	private BigInteger nbElem; // nombre de nombres calcules | utilise pour les stats

	private BigDecimal sum; // somme de toute les persistances | utilise pour les stats

	private BigDecimal average; // moyenne de toutes les persistances | utilise pour les stats

	private FileManager fileHandler; // pour gerer les fichiers et dossiers des logs

	private String logsPath; // chemin de la log principiale

	

	public ConnexionWorkerPC(int id, Socket s2, Distribution distribu) {
		this.occurences = new Hashtable<>();
		
		this.highestPers = new ArrayList<String>();
		
		this.cores = (byte) Runtime.getRuntime().availableProcessors();
		
		this.perstMax = 0;
		
		this.median = 0;
		
		this.INTERVAL = new BigInteger("50000");
		
		this.MIN =distribu.getMin() ;
		
		this.MAX =distribu.getMax() ;
		
		this.nbElem = BigInteger.ZERO;
		
		this.sum = BigDecimal.ZERO;
		
		this.average = BigDecimal.ZERO;
		
		this.fileHandler = new FileManager();
		
		this.logsPath = "Logs/Logs_" + this.MIN.toString() + "-" + this.MAX.toString() + "/";
		
		this.distributionInter = distribu;
		
		this.id = id;
		
		this.s = s2;

		try {
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Serveur a cree les flux");

	}

	public Message getMessageIN() {
		return messagein;
	}

	public void run() {
		try {
			while (true) {

				di = (DemandeInter) in.readObject();
				System.out.println("Demande d'interval reçu");
				if (di.getOk() == true) { // on envoie quand il y a une demande

					try {
						
						out.writeObject(distributionInter);// envoie l'intervale
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Serveur a envoye interval:");
				}
				
					try {
						messagein = (Message) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // lecture message

				
				System.out.println("Serveur a reçu les Arrayliste de retour ");

				if (messagein != null) {
					for (int i = 0; i < messagein.getList().size(); i++) {

						
							gatherAverage(messagein.getList().get(i));

							gatherOccurrences(messagein.getList().get(i));

							gatherPerstMax(messagein.getList().get(i));

							gatherMedian();
							
							// on cree un string avec un path de la forme

							file = this.fileHandler.checkFile(this.logsPath, messagein.getListeMin().get(i) + "-" + messagein.getListMax().get(i) );

							// on serialise la liste des resultats dans file
							this.fileHandler.serializeList(file, messagein.getList().get(i));

						
					}
					System.out.println("Le serveur a tout ecrit dans les fichiers avec les resultats de retour");

				}

				if (distributionInter.getok()==false) {// si on a aucun interval a envoyer
					System.out.println("Aucun intervalle a envoyer");
					break;
				}

			}

			// 12a - Le serveur ferme ses flux
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public float returnMedian() {
		return this.median;
	}

	public BigDecimal returnAverage() {
		return this.average;
	}

	public byte returnPerstMax() {
		return this.perstMax;
	}

	public void gatherOccurrences(ArrayList<String> list) {

		// variable pour stocker le resultat
		byte resInter;

		// pour tout les String de la list
		for (String s : list) {

			// on recupere la persistance
			resInter = (byte) Integer.parseInt(s.split(":")[1]);

			// si on a deja rencontre cette persistance
			if (this.occurences.containsKey(resInter)) {

				// on incremente la valeur associe
				this.occurences.replace(resInter, this.occurences.get(resInter).add(BigInteger.ONE));

			} else {

				// on ajoute une "ligne" ayant pour key la persistance et value 1
				this.occurences.put(resInter, BigInteger.ONE);

			}

		}

	}

	// OK
	// fonction pour imprimer la Hashtable this.occurences

	public void printOccurences() {

		for (int i = 0; i < this.occurences.size(); i++) {

			System.out.println(
					"The persistence : " + i + " has been found for " + this.occurences.get((byte) i) + " numbers.");

		}

	}

	// OK
	// fonction qui retourne une arraylist d'un intervalle de resultats
	public ArrayList<String> getResultsInterv(final BigInteger pMIN, final BigInteger pMAX) {

		// on retourne une ArrayList<String> de resultats renvoyee par la fonction
		// getResFromFiles
		return this.fileHandler.getResFromFiles(pMIN, pMAX, this.INTERVAL, this.logsPath);

	}

	// OK
	// fonction qui met dans highestPers les nombres ayant la plus grande
	// persistance
	public void gatherPerstMax(ArrayList<String> pList) {

		// pour tout les string de pList
		for (String s : pList) {

			// si la persistance du nombre est = a this.perstMax
			if (Integer.parseInt(s.split(":")[1]) == this.perstMax) {

				// on ajoute le nombre dans la liste des plus grandes persistances
				this.highestPers.add(s);

				// si la persistance du nombre est > a this.perstMax
			} else if (Integer.parseInt(s.split(":")[1]) > this.perstMax) {

				// on vide la liste des plus grandes persistances
				this.highestPers.clear();

				// on change la valeur de la plus grande persistance
				this.perstMax = (byte) Integer.parseInt(s.split(":")[1]);

				// on ajoute le nombre dans la liste des plus grandes persistances
				this.highestPers.add(s);

			}
		}
	}

	// OK
	// fonction pour imprimer la Hashtable this.occurences
	public void printHighestPerst() {

		for (int i = 0; i < this.highestPers.size(); i++) {

			System.out.println("Persistence : " + this.highestPers.get(i).split(":")[1] + " for the number : "
					+ this.highestPers.get(i).split(":")[0]);

		}

	}

	// OK
	// fonction qui retourne la moyenne des persistances
	public void gatherAverage(ArrayList<String> list) {

		// pour tout les string de list
		for (int i = 0; i < (list.size() - 1); i++) {

			// on augmente le nombre d'element constituant la moyenne
			this.nbElem = this.nbElem.add(BigInteger.ONE);

			// on ajoute la persistance a la somme en separant la persistance du nombre
			this.sum = this.sum.add(BigDecimal.valueOf(Integer.parseInt(list.get(i).split(":")[1])));

			// on divise la somme par le nombre d'elements ( le diviseur , la precision de
			// resultat , le mode d'arrondissement )
			this.average = this.sum.divide(new BigDecimal(this.nbElem), 2, RoundingMode.CEILING);

		}

	}

	// OK
	// fonction qui retourne la mediane des persistances
	public void gatherMedian() {

		// si le nombre de persistances diferentes est pair
		if (this.occurences.size() % 2 == 0) {

			// la mediane vaut la moyenne des valeurs de rang this.occurences.size() / 2.0
			// et ( this.occurences.size() / 2.0 + 1 )
			this.median = (float) (((this.occurences.size() / 2.0) + (this.occurences.size() / 2.0 + 1)) / 2.0);

		} else {

			// sinon la mediane vaut la valeur se trouvant au centre
			this.median = this.occurences.size() / 2;

		}

	}

	// OK
	// fonction qui retourne la persistance d'un nombre
	public int getPerstNb(final BigInteger pNb) {

		// on return seulement la persistance en separant le string donne par la
		// fonction
		return Integer.parseInt(this.fileHandler.getSingleResFromFile(pNb, this.INTERVAL, this.logsPath).split(":")[1]);

	}
}
