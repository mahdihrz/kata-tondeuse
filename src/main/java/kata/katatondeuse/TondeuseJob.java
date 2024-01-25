package kata.katatondeuse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TondeuseJob implements Job {

    private JobRepository jobRepository;
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
        System.out.println("processing tondeuse from file " + inputFile);

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String firstLine, tondeuseLine, instructionLine;
            if ((firstLine = br.readLine()) != null){
                //init terrain
                logger.info(firstLine);
            }

            while ((tondeuseLine = br.readLine()) != null && (instructionLine = br.readLine()) != null) {
                // init tondeuse & execute instructions
                logger.info(tondeuseLine);
                logger.info(instructionLine);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        this.jobRepository.update(execution);

    }
}
