package com.company;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Math;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.random;

public class Server extends Thread{
    public static int estrazioneEsatta = 0;
    public static List<Integer> RandomNumbers = new ArrayList<Integer>();
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    public static int porta;
    public static int i = 0;
    public static List<Integer> AllClient = new ArrayList<Integer>();
    public static Hashtable<Integer, String> my_dict = new Hashtable<Integer, String>();
    public static int z=0;
    public static int j=0;
    public static int volteEstrazione = 0;

    public Server (Socket socket) {
        this.client = socket;
    }
    public void run() {
        try {
            comunica();
        }catch (Exception e){
            //e.printStackTrace(System.out);
        }
    }


    private void numbers() {
        if(i==1){
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    volteEstrazione++;
                    RandomNumbers.clear();
                    int j=0;
                    while(j<3)
                    {
                        int range = (int) (Math.random() * (90 - 1) + 1) + 1;
                        RandomNumbers.add(range);
                        j++;
                    }
                    System.out.println("Estrazione numero " + volteEstrazione + " : " + RandomNumbers  + '\n');
                }
            }, 0, 30*1000);
        }
    }

    public void comunica() throws Exception {

        estrazioneEsatta = 0;
        numbers();
        inDalClient = new BufferedReader(new InputStreamReader (client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());
        for(;;) {

            stringaRicevuta = inDalClient.readLine();
            my_dict.put(client.getPort(),stringaRicevuta);
            if (stringaRicevuta == null || stringaRicevuta.equals("FINE")) {
                outVersoClient.writeBytes(stringaRicevuta + " (=> server in chiusura...)"+ '\n');
                System.out.println("Echo sul server in chiusura : "+ stringaRicevuta);
                break;
            }else {
                    for (Map.Entry<Integer, String> entry : my_dict.entrySet()) {
                        if(entry.getKey() == client.getPort()){
                            String numeri = inDalClient.readLine();
                            List<String> items = Arrays.asList(numeri.split("\\s*,\\s*"));
                            System.out.println(items+ " sono i numeri scelti dal client " + entry.getValue() + '\n');
                            for(int i =0; i<3;i++){
                                for(int j=0;j<3;j++){

                                    if(items.get(j).equals(RandomNumbers.get(i).toString())){
                                        estrazioneEsatta++;
                                    }
                                }
                            }
                            String esito;
                            if(estrazioneEsatta==3){
                                esito = entry.getValue().toUpperCase() + " hai vinto!!! ";

                            }
                            else{
                                esito = entry.getValue().toUpperCase() + " hai perso!!! " + '\n' + "I numeri estratti erano : " + RandomNumbers;

                            }
                            outVersoClient.writeBytes(esito + '\n');
                            outVersoClient.close();
                            inDalClient.close();
                            //System.out.println("9 - Chiusura socket" + client);
                            client.close();
                        }
                    }
                    z++;
            }
        }
    i++;
    }




    public static class MultiServer{

        public void start() {
            System.out.println("Server avviato");
            try {
                ServerSocket serverSocket = new ServerSocket(6788);
                for (; ; ) {

                    Socket socket = serverSocket.accept();
                    porta = socket.getPort();
                    AllClient.add(Server.porta);
                    //System.out.println(AllClient);
                    i++;
                    Server serverThread = new Server(socket);
                    //System.out.println(socket);
                    serverThread.start();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante l'istanza del server!!!!!");
                System.exit(1);
            }
        }
    }


    public static void main(String args[]) {

        MultiServer tcpServer = new MultiServer();
        tcpServer.start();
    }

}