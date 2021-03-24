public class Pagina{
    int endLogico;
    int endFisico;
    int r;

    public void setR(int new_r){
        this.r = new_r;
    }

    public Pagina(int endLogico, int endFisico, int r) {
        this.endLogico = endLogico;
        this.endFisico = endFisico;
        this.r = r;
    }
}