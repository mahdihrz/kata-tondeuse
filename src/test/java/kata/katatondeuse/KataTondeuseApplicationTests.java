package kata.katatondeuse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class KataTondeuseApplicationTests {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    void setUp() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJobExecution(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file", getClass().getClassLoader().getResource("testInput.txt").getPath())
                .toJobParameters();

        // when
        // ** Update the following line:
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("1 3 N 5 1 E"));
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void testNotFoundFileJobExecution(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file", "whatever.txt")
                .toJobParameters();

        // when
        // ** Update the following line:
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("whatever.txt (Le fichier spécifié est introuvable)"));
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());
    }

    @Test
    void testEmptyInputExecution(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file", "")
                .toJobParameters();

        // when
        // ** Update the following line:
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("input file path empty"));
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());
    }
}
