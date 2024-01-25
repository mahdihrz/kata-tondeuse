package kata.katatondeuse;

import kata.katatondeuse.exception.TondeuseException;
import kata.katatondeuse.model.Terrain;
import kata.katatondeuse.model.Tondeuse;
import kata.katatondeuse.service.TondeuseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TondeuseJob implements Job {

    final private JobRepository jobRepository;
    private static final Logger logger = LoggerFactory.getLogger(TondeuseJob.class);
    @Autowired
    private TondeuseService tondeuseService;

    public TondeuseJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public String getName() {
        return "TondeuseJob";
    }

    @Override
    public void execute(JobExecution execution) {
        JobParameters jobParameters = execution.getJobParameters();
        String inputFile = jobParameters.getString("input.file");
        logger.debug("processing tondeuse from file " + inputFile);

        if (inputFile == null || inputFile.isEmpty()) {
            errorOccurs(execution, "input file path empty");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

            var terrain = initTerrain(br);
            var tondeuses = bougeLesTondeuses(br, terrain);
            afficheLeResultat(tondeuses);

        } catch (IOException | TondeuseException e) {
            errorOccurs(execution, e.getMessage());
            return;
        }

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        this.jobRepository.update(execution);

    }

    private void errorOccurs(JobExecution execution, String error) {
        logger.error(error);
        execution.setStatus(BatchStatus.FAILED);
        execution.setExitStatus(ExitStatus.FAILED);
        this.jobRepository.update(execution);
    }

    private static void afficheLeResultat(List<Tondeuse> tondeuses) {
        StringBuilder sb = new StringBuilder();
        tondeuses.forEach(s -> sb.append(s).append(" "));
        logger.info(sb.toString());
    }

    private List<Tondeuse> bougeLesTondeuses(BufferedReader br, Terrain terrain) throws IOException {
        List<Tondeuse> tondeuses = new ArrayList<>();
        String tondeuseLine;
        String instructionLine;
        while ((tondeuseLine = br.readLine()) != null && (instructionLine = br.readLine()) != null) {
            String[] tondeuseCoordinates = tondeuseLine.split(" ");
            Tondeuse tondeuse = new Tondeuse(Integer.parseInt(tondeuseCoordinates[0]), Integer.parseInt(tondeuseCoordinates[1]), tondeuseCoordinates[2].charAt(0), terrain);
            tondeuses.add(tondeuse);
            logger.debug("Tondeuse init:" + tondeuse);
            tondeuseService.processInstructions(tondeuse, instructionLine);
        }
        return tondeuses;
    }

    private static Terrain initTerrain(BufferedReader br) throws IOException, TondeuseException {
        String firstLine;
        Terrain terrain;
        if ((firstLine = br.readLine()) != null) {
            String[] terrainCoordinates = firstLine.split(" ");
            terrain = new Terrain(Integer.parseInt(terrainCoordinates[0]), Integer.parseInt(terrainCoordinates[1]));
            logger.debug(terrain.toString());

        } else {
            throw new TondeuseException("no lines in file");
        }
        return terrain;
    }
}
