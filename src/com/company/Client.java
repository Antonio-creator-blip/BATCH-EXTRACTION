package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    String nomeServer = "localhost";
    int portaServer = 6788;
    Socket mioSocket;
    BufferedReader tastiera;
    String stringaUtente;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    public static int i=0;
    public static int z=0;

    public Socket connetti() {
        //System.out.println("2 CLIENT partito in esecuzione ...");

        try {
            // per l'input da tastiera
            tastiera = new BufferedReader(new InputStreamReader(System.in));

            // creo un socket
            mioSocket = new Socket(nomeServer, portaServer);

            // associo due oggetti al socket per effettuare la scrittura e la lettura
            outVersoServer = new DataOutputStream(mioSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));

        }
        catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        }
        catch (Exception e) {
            //System.out.println(e.getMessage());
            //System.out.println("Errore durante la connessione");
            System.exit(1);
        }

        return mioSocket;
    }

    public void comunica() {
        for(;;)
            try {
                if(i==0){
                    System.out.println("Inserisci il tuo nome identificativo: " );
                    stringaUtente = tastiera.readLine();
                }
                i++;
                // la spedisco al server
                //System.out.println("5 - invio la stringa al server e attendo ...");
                outVersoServer.writeBytes(stringaUtente + '\n');


                // leggo la risposta dal server
                if(z == 0){
                    System.out.println("Inserisci i tre numeri ");
                    String numeri = tastiera.readLine();
                    outVersoServer.writeBytes(numeri + '\n');
                    z++;
                }

                System.out.println(inDalServer.readLine());
                System.exit(1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //System.out.println("Errore durante la comunicazione col server!");
                System.exit(1);
            }
    }
    public static void main(String[] args) {
        Client cliente = new Client();
        cliente.connetti();
        cliente.comunica();

    }

}
