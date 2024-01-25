package kata.katatondeuse;

import kata.katatondeuse.model.Terrain;
import kata.katatondeuse.model.Tondeuse;
import kata.katatondeuse.service.TondeuseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTondeuseService {

    @Test
    public void testProcessInstructions(){

        Terrain terrain = new Terrain(5,5);
        TondeuseService tondeuseService = new TondeuseService();

        Tondeuse tondeuse1 = new Tondeuse(1,2,'N',terrain);
        tondeuseService.processInstructions(tondeuse1, "GAGAGAGAA");

        Tondeuse tondeuse2 = new Tondeuse(3,3,'E',terrain);
        tondeuseService.processInstructions(tondeuse2, "AADAADADDA");

        Assertions.assertEquals("1 3 N",tondeuse1.toString());
        Assertions.assertEquals("5 1 E",tondeuse2.toString());

    }
}
