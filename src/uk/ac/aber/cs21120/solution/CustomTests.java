package uk.ac.aber.cs21120.solution;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class contains som custom tests to check the correctness of Simulator's and Job's methods.
 *
 * @author Matuesz Ziobrowski(maz26@aber.ac.uk)
 *
 */
public class CustomTests {

    /**
     * Tests if one job finishes after its duration time.
     */
    @Test
    public void timingTest() {
        Simulator s = new Simulator(4);

        s.add(new Job(1,1,2)); // adds new job to the simulator that has the duration equal 2

        // after 3 ticks the job should finish its work
        // 1 tick - to move job form the waiting jobs queue to the running jobs array list
        // 2 ticks - to finish the jobs work
        for(int i=0;i<=2;i++) s.tick();

        // checks if the simulator has finished its work
        Assertions.assertEquals(true, s.allDone());
    }

    /**
     * Tests if average time of two different jobs with the same priority is correct with predicted.
     */
    @Test
    public void testGetAverageJobCompletionTime() {
        Simulator s = new Simulator(4);

        s.add(new Job(1,1,4));
        s.add(new Job(2,1,5));

        // add one more to check if the hash maps with priorities total time and amount of jobs are filled in a correct way
        s.add(new Job(3,2,4));

        for(int i=0;i<=5;i++) s.tick();

        // for 2 jobs with priority 1 the total time is 9, so the average job completion time for the priority 1 should be 9 / 2 = 4.5
        Assertions.assertEquals(4.5 , s.getAverageJobCompletionTime(1));
    }

    /**
     * Tests if two different jobs are placed in the priority queue in the right order.
     */
    @Test
    public void testRightPriorityOrder() {
        Simulator s = new Simulator(1);

        s.add(new Job(1,2,4));
        s.add(new Job(2,1,5));

        // puts only the job with the higher priority, in this case it is the second job
        s.tick();

        // Id of the first job in the running jobs has to be equals 2 because it has the highest priority
        Assertions.assertEquals(2, s.getRunningJobs().toArray()[0]);
    }

    /**
     * Tests the simulator by adding different configurations of jobs and ambulances and compares their times to the predicted when they finish.
     */
    @Test
    public void testAddingJobsInDifferentConfigurations() {

        // 2 ambulances, and 2 jobs: .allDone() should return true after the longest jobs duration in this case it is 5 ticks and + 1 to remove the last element
        Simulator s = new Simulator(2);

        s.add(new Job(1,2,4));
        s.add(new Job(2,1,5));

        for(int i=0;i<5;i++) s.tick();

        Assertions.assertEquals(false, s.allDone());

        //add last tick to finish the clear the running jobs
        s.tick();
        Assertions.assertEquals(true, s.allDone());

        // 1 ambulance and 2 jobs: allDone() should return true after 9 ticks because here is only one ambulance and total time for 2 jobs is 9
        s = new Simulator(1);


        s.add(new Job(1,2,4));
        s.add(new Job(2,1,5));

        for(int i=0;i<9;i++) s.tick();

        Assertions.assertEquals(false, s.allDone());

        s.tick();
        Assertions.assertEquals(true, s.allDone());
    }

    /**
     *  This method is just to run task3 method in the Simulator class.
     */
    @Test
    public void runTask3() {
        Simulator.task3();
    }

    /**
     * This method is just to run task4 method in the Simulator class.
     */
    @Test
    public void runTask4() {
        Simulator.task4();
    }
}
