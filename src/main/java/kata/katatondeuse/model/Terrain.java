package kata.katatondeuse.model;

public class Terrain {

    final private int largeur;
    final private int longueur;

    public Terrain(int largeur, int longueur) {
        this.largeur = largeur;
        this.longueur = longueur;
    }

    public boolean estDansLeTerrain(int x, int y) {
        return x >= 0 && x <= largeur && y >= 0 && y <= longueur;
    }

    @Override
    public String toString() {
        return "Terrain:" + largeur + longueur;
    }
}
