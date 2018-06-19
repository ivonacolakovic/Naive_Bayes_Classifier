public class IzracunMerilaKakovosti {

    //calculate accuracy
    public static float izracunTocnosti(float[][] matrikaZmede){
        float tocnost = 0;
        float vsota = 0;
        float pravilni = 0;
        for(int i = 0; i < matrikaZmede.length; i++){
            for(int j = 0; j < matrikaZmede.length; j++){
                if(i == j){
                    pravilni += matrikaZmede[i][j];
                }
                vsota += matrikaZmede[i][j];
            }
        }
        tocnost = pravilni / vsota;
        return tocnost;
    }

    // dodaj i shrani razrede za katere izracuna precions i recall


    //calculate precision
    public static float[] izracunPrecision(float[][] matrikaZmede){
        int j;
        //System.out.println("M: "+matrikaZmede.length);
        float[] poljePrecisona = new float[matrikaZmede.length];
        for(int i = 0; i < matrikaZmede.length; i++) {
            float tocni = 0;
            float vsi = 0;
            for (j = 0; j < matrikaZmede.length; j++) {
                if (i == j) {
                    tocni += matrikaZmede[j][i];
                    vsi+=matrikaZmede[j][i];
                    //System.out.println("Tocnih: "+tocni);
                }
                else {
                    vsi += matrikaZmede[j][i];
                }
            }
            //System.out.println("Vsi: "+vsi);
            poljePrecisona[i] = tocni / vsi;
        }
        return poljePrecisona;
    }

    //calculate recall
    public static float[] izracunRecall(float[][] matrikaZmede){
        float vsi = 0;
        float tocni = 0;
        float[] poljeRecall = new float[matrikaZmede.length];
        for(int i = 0; i < matrikaZmede.length; i++) {
            for (int j = 0; j < matrikaZmede.length; j++) {
                if(i == j){
                    tocni += matrikaZmede[i][j];
                }
                vsi += matrikaZmede[i][j];
                poljeRecall[i] = tocni / vsi;
            }
        }
        return poljeRecall;
    }
    //calculate F-score
    public static float[] izracunFMere(float[][] matrikaZmede){
        float fMeraRazreda[] = new float[matrikaZmede[0].length];
        float[] precisonPolje = IzracunMerilaKakovosti.izracunPrecision(matrikaZmede);
        float[] recallPolje = IzracunMerilaKakovosti.izracunRecall(matrikaZmede);
        for(int i = 0; i < matrikaZmede[0].length; i++) {
            float precison = precisonPolje[i];
            //System.out.println("Precision: "+precisonPolje[i]);
            float recall = recallPolje[i];
            //System.out.println("Recall: "+recallPolje[i]);
            fMeraRazreda[i] =  2 * (precison * recall) / (precison + recall);
            if(Float.isNaN(fMeraRazreda[i])){
               System.out.println("NaN pri i=: "+i);
            }
        }
        return fMeraRazreda;
    }



}
