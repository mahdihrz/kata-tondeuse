package kata.katatondeuse.model;

public class Tondeuse {

    private int x;
    private int y;
    private char orientation;
    private final Terrain terrain; //N W E S

    public Tondeuse(int x, int y, char orientation, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.terrain = terrain;
    }

    public void tourne(char direction){
        if (direction == 'D') {
            switch (orientation) {
                case 'N': orientation = 'E'; break;
                case 'E': orientation = 'S'; break;
                case 'S': orientation = 'W'; break;
                case 'W': orientation = 'N'; break;
            }
        } else if (direction == 'G') {
            switch (orientation) {
                case 'N': orientation = 'W'; break;
                case 'W': orientation = 'S'; break;
                case 'S': orientation = 'E'; break;
                case 'E': orientation = 'N'; break;
            }
        }
    }

    public void avance(){
        switch (orientation) {
            case 'N': if (terrain.estDansLeTerrain(x, y + 1)) y++; break;
            case 'E': if (terrain.estDansLeTerrain(x + 1, y)) x++; break;
            case 'S': if (terrain.estDansLeTerrain(x, y - 1)) y--; break;
            case 'W': if (terrain.estDansLeTerrain(x - 1, y)) x--; break;
        }
    }

    @Override
    public String toString(){
        return(String.format("%d %d %c", x,y,orientation));
    }
}
