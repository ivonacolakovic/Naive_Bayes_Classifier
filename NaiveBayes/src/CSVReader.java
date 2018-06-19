import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class CSVReader {

    public static void main(String[] args) {
        String fileName = "breastcancer_ucna.csv"; // TODO: nastavi na null
        String fileNameTest = "breastcancer_testna.csv"; //TODO: nastavi na null
        //String fileName = null;
        //String fileNameTest = null;
        //String pz = "";
        String line = "";
        BufferedReader brU = null;
        BufferedReader brT = null;
        ArrayList<String[]> tabelaPodatkov = new ArrayList<String[]>(); // ucna mnozica
        ArrayList<String[]> tabelaTestna = new ArrayList<String[]>(); // testna mnozica
        int stAtributov = 0; // stevilo podatkov v eni vrstici



/*
        Options options = new Options();
        options.addOption("i", false, "podrobnejsi izpis");
        options.addOption("t", true, "Ucna mnozica");
        options.addOption("T", true, "Testna mnozica");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(cmd.hasOption("i")){
            System.out.println("Imamo -i!!");
        }
        String ucna = cmd.getOptionValue("t");
        if(ucna != null){
            fileName = ucna;
            System.out.println("Imamo -t" +ucna);
        }
        String testna = cmd.getOptionValue("T");
        if(testna != null){
            fileNameTest = testna;
            System.out.println("Imamo -T" +testna);
        }*/
        File fileUcna = new File(fileName);
        File fileTestna = new File(fileNameTest);
        try {
            brU = new BufferedReader(new FileReader(fileUcna));
            System.out.println();
            while ((line = brU.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] readDataUcna = line.split(",");

                    tabelaPodatkov.add(readDataUcna);

                }
            }
            brU.close();
            brT = new BufferedReader((new FileReader(fileTestna)));
            System.out.println();
            while((line = brT.readLine()) != null){
                if(!line.isEmpty()){
                    String[] readDataTestna = line.split(",");

                    tabelaTestna.add(readDataTestna);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> razredi = new ArrayList<String>();
        for (int i = 1; i < tabelaPodatkov.size(); i++) {
            String[] vrstica = tabelaPodatkov.get(i);
            stAtributov = vrstica.length;
            String vrednost = vrstica[vrstica.length - 1];
            if (!razredi.contains(vrednost)) {
                razredi.add(vrednost);
            }
        }
        float[][] matrikaZmede = new float[razredi.size()][razredi.size()];
        //System.out.println("Broj razreda: " + razredi.size());

        int dolzina = tabelaPodatkov.get(0).length;
        for(int y = 1; y < tabelaTestna.size(); y++){
            ArrayList<String[]> vrstica = new ArrayList<String[]>();
            vrstica.add(tabelaTestna.get(y));
            String napovedanRazred = vrstica.get(0)[dolzina-1];
            String dejanskiRazred = klasifikujVrsticu(tabelaPodatkov, vrstica, dolzina, razredi);


            if(dejanskiRazred.equals(napovedanRazred)){
                int index = razredi.indexOf((dejanskiRazred));
                matrikaZmede[index][index] +=1;
               /* System.out.println("Dejanski razred: "+dejanskiRazred);
                System.out.println("Napovedan razred: "+napovedanRazred);
                System.out.println("Dejanski razred - index : "+razredi.indexOf(dejanskiRazred));
                System.out.println("Napovedan razred - index: "+razredi.indexOf(napovedanRazred));*/
            }
            else{
               /* if(y<10){
                    System.out.println("Dejanski razred: "+dejanskiRazred);
                    System.out.println("Napovedan razred: "+napovedanRazred);
                    System.out.println("Dejanski razred - index : "+razredi.indexOf(dejanskiRazred));
                    System.out.println("Napovedan razred - index: "+razredi.indexOf(napovedanRazred));
                }*/
                //rasporedi u ostala nedijagonalna polja
                matrikaZmede[razredi.indexOf(dejanskiRazred)][razredi.indexOf(napovedanRazred)] += 1;
                //indexOf(dejanskiRazred) je broj reda indexOf(napovedanRazred) je broj kolone
            }
        }
      /*  matrikaZmede[0][0] = 1207;
        matrikaZmede[0][1] = 3;
        matrikaZmede[0][2] = 0;
        matrikaZmede[0][3] = 0;
        matrikaZmede[1][0] = 34;
        matrikaZmede[1][1] = 350;
        matrikaZmede[1][2] = 0;
        matrikaZmede[1][3] = 0;
        matrikaZmede[2][0] = 0;
        matrikaZmede[2][1] = 56;
        matrikaZmede[2][2] = 13;
        matrikaZmede[2][3] = 0;
        matrikaZmede[3][0] = 0;
        matrikaZmede[3][1] = 19;
        matrikaZmede[3][2] = 0;
        matrikaZmede[3][3] = 46;*/


        float velikost = tabelaTestna.size();
        // izracunaj merila kakovosti
        float tocnost = IzracunMerilaKakovosti.izracunTocnosti(matrikaZmede);
        float fMeraRazreda[] = IzracunMerilaKakovosti.izracunFMere(matrikaZmede);
        float fMera = 0;

            for (int y = 0; y < razredi.size(); y++) {
                try {
                float stTegRazreda = prestejVse(tabelaPodatkov, razredi.get(y), stAtributov - 1);
                /*System.out.println("F: " + fMeraRazreda[y]);
                System.out.println("V: "+velikost);
                System.out.println("str "+stTegRazreda);*/
                fMera += stTegRazreda / velikost * fMeraRazreda[y];
                }
                catch(java.lang.ArithmeticException e){
                    System.out.println("Napaka pri: " +y);
                }
            }

        float precision[] = IzracunMerilaKakovosti.izracunPrecision(matrikaZmede);
        float recall[] = IzracunMerilaKakovosti.izracunRecall(matrikaZmede);


        //izpis prvog reda - razreda
        for (String razred : razredi) {
            System.out.format("%-15s", razred);
        }
        System.out.print("<-- Klasificiran kot");
        System.out.println();
        //izpis matrike zmede
        for(int m = 0; m < razredi.size(); m++) {
            for (int k = 0; k < razredi.size(); k++) {
                System.out.format("%-15f", matrikaZmede[m][k]);
            }
            System.out.print(razredi.get(m));
            System.out.println();
        }



        System.out.println("Metrike: ");
        System.out.println("Tocnost: " +tocnost);
        System.out.println("F-score: " +fMera);

        float vsota = 0, vsota1 = 0;
        System.out.println("Precision: ");
        for (String razred : razredi) {
            System.out.format("%-15s", razred);
        }
        System.out.println();
        for(int h = 0; h < razredi.size(); h++) {
            float pr = precision[h];
            vsota+=pr;
            System.out.format("%-15f", pr);
        }
        System.out.println();
        System.out.println("Precision average: "+vsota/razredi.size());

        System.out.println("Recall: ");
        for(int g = 0; g < razredi.size(); g++) {
            float rc = recall[g];
            vsota1+=rc;
            System.out.format("%-15f", rc);
        }
        System.out.println();
        System.out.println("Recall average: "+vsota1/razredi.size());
    }


    public static String klasifikujVrsticu(ArrayList<String[]> tabelaPodatkov, ArrayList<String[]> tabelaTestna, int dolzina, ArrayList<String> razredi ) {
        //List tabelaTestna = Arrays.asList(vrstica);
        float velikost = tabelaPodatkov.size();
        ArrayList<Float> xevi = new ArrayList<>();
        ArrayList<Float> yoni = new ArrayList<>();
        //System.out.println("Velikost: " + velikost);
        //presteje razlicne razrede
        float st = 0;
        for (int e = 0; e < razredi.size(); e++) {
            //for(int r = 0; r < tabelaPodatkov.size(); r++){
            st = prestejVse(tabelaPodatkov, razredi.get(e), dolzina - 1);
            //}
            xevi.add(st / velikost);
            yoni.add(velikost / st);
            //System.out.println("razred: " + razredi.get(e) + " stevilo: " + st);

        }

        ArrayList<Float> jednoList = new ArrayList<>();
        ArrayList<Float> obaList = new ArrayList<>();
        float jedno = 0, oba = 0;
        int to = tabelaPodatkov.get(0).length;
        for (int brojRazreda = 0; brojRazreda < razredi.size(); brojRazreda++) {
            //System.out.println("razred: " + razredi.get(brojRazreda));
            for (int stAtributov = 0; stAtributov < dolzina - 1; stAtributov++) {
                for (int nesto = 0; nesto < tabelaTestna.size(); nesto++) {
                    //System.out.print(tabelaTestna.get(0)[stAtributov] + " ");
                    jedno = prestejVse(tabelaPodatkov, tabelaTestna.get(nesto)[stAtributov], stAtributov);
                    //System.out.println("mesto u testnoj tabeli " +(to-1));
                    oba = prestejDolocene(tabelaPodatkov, tabelaTestna.get(nesto)[stAtributov], stAtributov, razredi.get(brojRazreda), to - 1);
                    //System.out.println("presteto: " + oba + " / " + jedno);
                    jednoList.add(jedno);
                    obaList.add(oba);
                }
            }
        }
        ArrayList<Verjetnost> seznamP = new ArrayList<>();
        Verjetnost max = null;
        for (int ups = 0; ups < razredi.size(); ups++) {
            //System.out.println("Razred konec :" + razredi.get(ups));
            float x = xevi.get(ups);
            float y = yoni.get(ups);
            float p = x;
            for (int hop = 0; hop < jednoList.size() / razredi.size(); hop++) {
                // namesti da u drugom foru cita 5 6 7 8 element ane prva 4 opet
                //System.out.println("Probaj ovo "+(jednoList.size()/razredi.size()-1));
                //System.out.println("Oba: " + obaList.get(hop + jednoList.size() / razredi.size() * ups) + " / " + jednoList.get(hop + jednoList.size() / razredi.size() * ups));
                p = p * (obaList.get(hop + jednoList.size() / razredi.size() * ups) / jednoList.get(hop + jednoList.size() / razredi.size() * ups)) * y;
            }
            //System.out.println("P: " + p);
            seznamP.add(new Verjetnost(razredi.get(ups), p));
            max = seznamP.get(0);
            for (int i = 0; i < seznamP.size(); i++) {
                if (max.getVrednost() < seznamP.get(i).getVrednost()) {
                    max = seznamP.get(i);
                }
            }
            //System.out.println("klasifikuj kot: " + max.getImeRazreda() + " verjetnost: " + max.getVrednost());
        }
        return max.getImeRazreda();
    }


    public static Verjetnost izracunajP(String ime, int velikost, int stTegRazreda, ArrayList<Integer> vsiTeVrednosti, ArrayList<Integer> vsiTeVrednostiInRazreda){
        float p = (float)vsiTeVrednostiInRazreda.get(0) / velikost;
        float x = (float)velikost / stTegRazreda;
        for(int i = 0; i < vsiTeVrednosti.size(); i++){
            p = p * vsiTeVrednostiInRazreda.get(i) / vsiTeVrednosti.get(i) * x;
        }
        Verjetnost koncnoP = new Verjetnost(ime, p);
        return koncnoP;
    }

    public static Verjetnost primerjaj(ArrayList<Verjetnost> seznam){
        Verjetnost max = seznam.get(0);
        for(int i = 0 ; i < seznam.size(); i++){
            if( max.getVrednost() < seznam.get(i).getVrednost()){
                max = seznam.get(i);
            }
        }
        return max;
    }


    /*public static int prestejDolocene(ArrayList<String[]> tabelaPodatkov, String vrednost1, int kolona, String vrednost2, int kolona1){
        int stevilo = 0;
        //steje vse vrstice s enom vrednosti v datoj koloni in istega razreda
        for (int i = 0; i < tabelaPodatkov.size(); i++) {
            String[] vrstica = tabelaPodatkov.get(i);
            String vrednost = vrstica[kolona];
            String vrednost3 = vrstica[kolona1];
            if (vrednost.equals(vrednost1) && vrednost3.equals(vrednost2)) {
                stevilo++;
            }
        }
        return stevilo;
    }
    public static int prestejVse(ArrayList<String[]> tabelaPodatkov, String vrednost1, int kolona) {
        int stevilo = 0;
        //steje vse vrstice s enom vrednosti v datoj koloni
        for (int i = 0; i < tabelaPodatkov.size(); i++) {
            String[] vrstica = tabelaPodatkov.get(i);
            String vrednost = vrstica[kolona - 1];
            if (vrednost.equals(vrednost1)) {
                stevilo++;
            }
        }
        return stevilo;
    }*/
    public static int prestejDolocene(ArrayList<String[]> tabelaPodatkov, String vrednost1, int kolona, String vrednost2, int kolona1){
        int stevilo = 0;
        //steje vse vrstice s enom vrednosti v datoj koloni in istega razreda
        for (int i = 0; i < tabelaPodatkov.size(); i++) {
            String[] vrstica = tabelaPodatkov.get(i);
            String vrednost = vrstica[kolona];
            String vrednost3 = vrstica[kolona1];
            if (vrednost.equals(vrednost1) && vrednost3.equals(vrednost2)) {
                stevilo++;
            }
        }
        return stevilo;
    }
    public static int prestejVse(ArrayList<String[]> tabelaPodatkov, String vrednost1, int kolona) {
        int stevilo = 0;
        //steje vse vrstice s enom vrednosti v datoj koloni
        for (int i = 0; i < tabelaPodatkov.size(); i++) {
            String[] vrstica = tabelaPodatkov.get(i);
            String vrednost = vrstica[kolona];
            if (vrednost.equals(vrednost1)) {
                stevilo++;
            }
        }
        return stevilo;
    }
}

