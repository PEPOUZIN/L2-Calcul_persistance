
package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import Message.DemandeInter;
import Message.Distribution;
import Message.Message;
import utils.FileManager;
import worker.Worker;

public class WorkerPC {

	// Le WorkerPC attend comme argument l'adresse du serveur (ex. : java Client
	// 127.0.0.1)
	public static void main(String[] args) throws Exception {
		ArrayList<Worker> workers; // liste des workers

		// booleen pour savoir si les workers ont fini
		boolean one = true; // boolean pour ajouter un a pMIN
		boolean fini = false;
		Distribution distributionInter = null; // recuper l'interval distrib
		DemandeInter di; // Demander interval

		final byte nbWorkers = (byte) Runtime.getRuntime().availableProcessors(); // nombre de coeurs disponibles | byte
																					// => 1 octet alors que int => 4
		BigInteger pMIN; // borne minimale de l'intervalle envoye
		BigInteger pMAX; // borne maximale de l'intervalle envoye
		BigInteger INTERVAL; // taille des intervalles envoyes aux workers
		int port = 2222;
		boolean arreter = false;

		ArrayList<ArrayList<String>> retour; // liste de retour a renvoyer au serveur
		ArrayList<String> listePmin; // retour des interval qu'ils ont fait
		ArrayList<String> listePmax;

		workers = new ArrayList<Worker>();
		retour = new ArrayList<ArrayList<String>>();
		listePmax = new ArrayList<String>();
		listePmin = new ArrayList<String>();

		byte cores; // nombre de coeurs disponibles | byte => 1 octet alors que int => 4
		cores = (byte) Runtime.getRuntime().availableProcessors();

		INTERVAL = new BigInteger("50000"); // interval des worker a calculer
											// 4 - le WorkerPC ouvre une connexion avec le serveur
		Socket socket = new Socket("127.0.0.1", port);

		System.out.println("SOCKET = " + socket);

		/*
		 * 5b - A partir du Socket connectant le serveur au workerPC, le client ouvre 2
		 * flux : 1) un flux entrant afin de recevoir ce que le serveur envoie 2) un
		 * flux sortant (ObjectOutputStream) afin d'envoyer des messages au serveur
		 */
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

		out.flush();

		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		di = new DemandeInter();

		di.setOk(true);// pour recevoir un interval

		while (!arreter) {
			// Appel serveur pour un nouvelle intervalle
			try {
				if (di.getOk() == true) {
					// on fait la demande d'interval
					out.writeObject(di);
					System.out.println("Demande d'intervalle au Serveur");

					try {
						distributionInter = (Distribution) in.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println(" l'intervalle demandé est reçu");

					// on met a false tant qu'on a pas finit de traiter l'interval
					di.setOk(false);

					// lecture message
					System.out.println(distributionInter.getInter());// on reçoit bien l'interval

					// Verif d'arret
					if (distributionInter.getok() == false) {
						break;
					}
					//System.out.print(distributionInter.getMin());
					
					/*pMIN = distributionInter.getMin();

					pMAX =distributionInter.getMax();*/
					
					pMIN = BigInteger.ZERO;

					pMAX = INTERVAL.multiply(BigInteger.valueOf(cores * 2));

					pMIN = distributionInter.getMin();

					pMAX =distributionInter.getMax();
					
					INTERVAL = distributionInter.getInter();
					
					for (int i = 0; i < nbWorkers; i++) {

						// worker avec comme intervalle [ pMIN ; pMAX ]
						workers.add(new Worker(pMIN, pMAX));

						System.out.println("Worker " + workers.get(i).getId() + " has started");

						workers.get(i).start();

						// si on doit ajouter un. On le fait comme ca aucune valeur n'est calculee 2x
						if (one) {
							pMIN = pMIN.add(INTERVAL).add(BigInteger.ONE);
							one = false;
							// sinon on ajoute juste l'intervalle
						} else {
							pMIN = pMIN.add(INTERVAL);
						}

						pMAX = pMAX.add(INTERVAL);

					}

					// on regarde si les worker on finit
					while (!fini) {
						fini = true;
						for (Worker w : workers) {

							// si le thread est en cours d'execution
							if (w.getState() == Thread.State.RUNNABLE)
								fini = false;

							// sinon si il est interrompu
							else if (w.isInterrupted())
								System.out.println("Thread " + w.getId() + " has been interrupted");
						}

					}
					di.setOk(true);// car tous les worker on fini et sont pret a recuperer un interval

					/*
					 * RECUPERER les valeur calcule par les worker et les ajouter a liste de retour
					 */

					// pour tous les workers
					for (int i = 0; i < workers.size(); i++) {

						retour.add(new ArrayList<String>());

						retour.get(i).addAll(workers.get(i).getList());

						listePmin.add(workers.get(i).getMIN().toString());

						listePmax.add(workers.get(i).getMAX().toString());
					}


					System.out.println("une Liste est prete a envoyé");

					Message message = new Message(retour, listePmin, listePmax);
					// les listes sont bien remplit verif faite
					out.writeObject(message);

				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("END"); // message de fermeture

		// 10b - Le client ferme ses flux
		out.close();
		in.close();
		socket.close();
	}

}
