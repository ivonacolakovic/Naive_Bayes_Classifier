public class Verjetnost {

    float vrednost;
    String imeRazreda;

    public Verjetnost(String imeRazreda, float vrednost){
        this.imeRazreda = imeRazreda;
        this.vrednost = vrednost;
    }
    public String getImeRazreda(){
        return imeRazreda;
    }
    public float getVrednost(){
        return vrednost;
    }
}
