package kata.katatondeuse;

import kata.katatondeuse.exception.TondeuseException;
import kata.katatondeuse.model.Terrain;
import kata.katatondeuse.model.Tondeuse;
import kata.katatondeuse.service.TondeuseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TondeuseJob implements Job {

    final private JobRepository jobRepository;
    private static final Logger logger = LoggerFactory.getLogger(TondeuseJob.class);

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

        Terrain terrain;
        List<Tondeuse> tondeuses = new ArrayList<>();
        TondeuseService tondeuseService = new TondeuseService();


        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String firstLine, tondeuseLine, instructionLine;
            if ((firstLine = br.readLine()) != null) {
                String[] terrainCoordinates = firstLine.split(" ");
                terrain = new Terrain(Integer.parseInt(terrainCoordinates[0]), Integer.parseInt(terrainCoordinates[1]));
                logger.debug(terrain.toString());

            } else {
                throw new TondeuseException("no lines in file");
            }

            while ((tondeuseLine = br.readLine()) != null && (instructionLine = br.readLine()) != null) {
                String[] tondeuseCoordinates = tondeuseLine.split(" ");
                Tondeuse tondeuse = new Tondeuse(Integer.parseInt(tondeuseCoordinates[0]), Integer.parseInt(tondeuseCoordinates[1]), tondeuseCoordinates[2].charAt(0), terrain);
                tondeuses.add(tondeuse);
                logger.debug("Tondeuse init:" + tondeuse);
                tondeuseService.processInstructions(tondeuse, instructionLine);
            }

            StringBuilder sb = new StringBuilder();
            tondeuses.forEach(s -> sb.append(s).append(" "));
            logger.info(sb.toString());

        } catch (IOException | TondeuseException e) {
            logger.error(e.getMessage());
        }


        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        this.jobRepository.update(execution);

    }
}
