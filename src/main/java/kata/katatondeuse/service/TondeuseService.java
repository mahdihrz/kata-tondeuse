package kata.katatondeuse.service;

import kata.katatondeuse.model.Tondeuse;
import org.springframework.stereotype.Service;

@Service
public class TondeuseService {
    public void processInstructions(Tondeuse tondeuse, String instructions) {
        for (char instruction : instructions.toCharArray()) {
            if (instruction == 'A') {
                tondeuse.avance();
            } else {
                tondeuse.tourne(instruction);
            }
        }
    }
}

