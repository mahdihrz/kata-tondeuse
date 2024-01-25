package kata.katatondeuse.model;

public class Terrain {

    final private int largeur;
    final private int longueur;

    public Terrain(int width, int height) {
        this.largeur = width;
        this.longueur = height;
    }

    public boolean estDansLeTerrain(int x, int y) {
        return x >= 0 && x <= largeur && y >= 0 && y <= longueur;
    }
}
