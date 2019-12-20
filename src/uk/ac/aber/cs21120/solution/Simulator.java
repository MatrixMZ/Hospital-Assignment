package uk.ac.aber.cs21120.solution;

import uk.ac.aber.cs21120.hospital.IJob;
import uk.ac.aber.cs21120.hospital.ISimulator;
import uk.ac.aber.cs21120.hospital.JobDisplay;
import uk.ac.aber.cs21120.hospital.RandomPriorityGenerator;

import java.util.*;

/**
 * This class contains implementation of ISimulator interface.
 *
 * @author Matuesz Ziobrowski(maz26@aber.ac.uk)
 *
 */
public class Simulator implements ISimulator {
    private ArrayList<IJob> runningJobs = new ArrayList<>();
    private PriorityQueue<IJob> waitingJobs = new PriorityQueue();
    private int currentTime = 0;
    private int freeAmbulances;
    private Map<Integer, Integer> totalTimes = new HashMap<>();
    private Map<Integer, Integer> totalJobs = new HashMap<>();

    /**
     * Initializes the Simulator with the given amount of free ambulances
     *
     * @param freeAmbulances
     */
    public Simulator(int freeAmbulances) {
        this.freeAmbulances = freeAmbulances;
    }

    /**
     * Add a job to the simulator but do not start it running. Instead, the job
     * should be waiting for resources.
     *
     * @param j
     */
    public void add(IJob j) {
        j.setSubmitTime(currentTime);
        waitingJobs.add(j);
    }

    /**
     * Update the simulator: run update ticks on all the jobs which are running,
     * deal with jobs which have completed in those update ticks, and start as many jobs running
     * as resources will permit. Finally, increment the tick number.
     */
    public void tick() {
        // iterates on the running jobs array list
        for(int i = runningJobs.size() - 1; i >= 0; i--)  {
            // running tick on every job
            IJob job = runningJobs.get(i);
            job.tick();

            // checks if current job has finished its work
            if (job.isDone()) {
                // if the job is done increments the variable that stores the set of key that is priority
                // and value that is amount of finished jobs for the given priority
                totalJobs.put(job.getPriority(), totalJobs.getOrDefault(job.getPriority(), 0) + 1);

                // also the total time for specified priority is updated (new time is added to the previous)
                totalTimes.put(job.getPriority(), totalTimes.getOrDefault(job.getPriority() , 0) + job.getTimeSinceSubmit(currentTime));


                // removes job because it has finished its work and obviously iterates the number of ambulances
                // because there is new one free ambulance
                runningJobs.remove(job);
                freeAmbulances++;
            }
        }

        int size = waitingJobs.size();

        // this for loop tries to take the top element from the stack of PriorityQueue with the waiting jobs
        // until there is any free ambulance left to put the new job into the running jobs,
        // peek method is taking the first element (copying it), and the poll method cuts the job from the top of the stack
        // and puts it int the running jobs array
        // finally decrements the number of free ambulances to be sure
        // that in the next iteration there is or is not any free ambulance that can be used
        for (int i = 0; i < size; i++) {
            if(freeAmbulances > 0 && waitingJobs.peek() != null) {
                runningJobs.add(waitingJobs.poll());
                freeAmbulances--;
            }
        }

        // updates the simulator's clock
        currentTime++;
    }

    /**
     * return the current time in ticks, i.e. how many times tick() has been called.
     *
     * @return
     */
    public int getTime() {
        return currentTime;
    }

    /**
     * Returns whether all jobs have been completed and none are waiting.
     *
     * @return true if all jobs have been completed and none are waiting.
     */
    public boolean allDone() {
        return waitingJobs.size() == 0 && runningJobs.size() == 0;
    }

    /**
     * Return the IDs of the running jobs, but not those which are waiting to run.
     *
     * @return a Set of integers of running jobs.
     */
    public Set<Integer> getRunningJobs() {
        // creates the temporary variable to store the data
        Set<Integer> result = new HashSet();

        // in the for loop we put into the temporary variable the ids of jobs that are inside the running jobs array list
        for(IJob job: runningJobs) {
            result.add(job.getID());
        }

        // finally returns the temporary variable
        return result;
    }

    /**
     * Return the average time from submission to the simulator to completion of all jobs so
     * far at a given priority level. Note: NOT the time between when the job starts running
     * and when it completes (which will just be the average job duration).
     *
     * @param priority the priority level for which we wish find the average completion time.
     * @return the average completion time as a double precision float.
     */
    public double getAverageJobCompletionTime(int priority) {

        // to create the average we have to divide the total time for given priority by the amount of
        // jobs that has finished its work that has the priority that is given
        // important thing is that we have to cast it because both methods returns integers but we need
        // variable that is floating point type on number so it can be for example float or double for mor precision
        // for example: (int) 9 / (int) 2 = 4 | (double) 9 / (double) 2 = 4.5 | double is more precised
        return (double)totalTimes.get(priority) / (double)totalJobs.get(priority);
    }

    /**
     * Helper method, iterates 10000 times on the given Simulator and randomly puts the jobs
     * to the simulator, on the end, works up to the time when the last job has finished its
     * work.
     *
     * @param s Simulator
     */
    private void runSimulator(Simulator s) {
        JobDisplay jd = new JobDisplay();
        Random random = new Random();
        RandomPriorityGenerator randomPriority = new RandomPriorityGenerator();

        // iterates 10000 times
        for(int i = 0; i < 10000; i++) {
            // creates new job with a chance of 1:3 with the random priority and the random duration between 10 and 20
            if(random.nextInt(3) == 0){
                s.add(new Job(i, randomPriority.next(), random.nextInt(11) + 10));
            }
            // ticks the simulator to add new job to the running jobs if there is any ine the PriorityQueue
            s.tick();

            // add the current state of simulator to the job display class
            // it has to be added after every tick
            jd.add(s);
        }

        // executes the ticks on the simulator up to the moment when the last Job finishes its work
        while (!s.allDone()) {
            s.tick();
            jd.add(s);
        }

        // prints out the visualisation of the every tick that has been executed in the simulator
        jd.show();

    }

    /**
     * Creates only one instance of simulator with 4 ambulances and prints out the average job completion time for each priority
     */
    public static void task3 () {
        Simulator s = new Simulator(4);
        // uses helper method to avoid the code duplication in the task3() and task4() methods
        s.runSimulator(s);


        System.err.println("Ambulances: 4");

        // prints out the average job completion time for each priority
        for(int priority: s.totalTimes.keySet()) {
            System.out.println("Priority: " + priority + " | Average: " + s.getAverageJobCompletionTime(priority));
        }
    }

    /**
     * This simulator is checking the
     */
    public static void task4 () {

        //*********************
        // Tip: You can comment the line 179 with jd.show(); to clearly see the results with averages for each simulator
        //      jd.show() makes mess in the terminals output
        //*********************

        // this 2 variables are used to my own algorithm that checks the differences between the simulators
        // with different amount of ambulances to return the amount of ambulances that has the best efficiency for this hospital
        //
        // This algorithm sums the each average value for each priority and multiplies
        // it by number of ambulances to get the weight value.
        // On the and it compares the weight with every simulator and stores the best value which is the smallest weight,

        int bestEfficiency = 0; // best amount of ambulances
        double lastBest = -1; // weight value for the best efficiency


        // creates 16 different simulators with the amount of ambulances between 4 and 20
        for(int ambulances = 4; ambulances < 21; ambulances++) {
            System.err.println("Ambulances: " + ambulances);
            Simulator s = new Simulator(ambulances);
            s.runSimulator(s);

            // algorithm: sums the averages
            double totalAverage = 0;

            for(int priority: s.totalTimes.keySet()) {
                System.out.println("Priority: " + priority + " | Average: " + s.getAverageJobCompletionTime(priority));
                totalAverage += s.getAverageJobCompletionTime(priority);
            }

            // algorithm: compares the weight (sum of averages * ambulances ) of the current simulator with the last best weight
            if(lastBest > 0 && lastBest > totalAverage * ambulances) {
                lastBest = totalAverage * ambulances;
                bestEfficiency = ambulances;
            } else if (lastBest == -1) {
                lastBest = totalAverage * ambulances;
                bestEfficiency = ambulances;
            }

            System.out.println("Total time: " + s.currentTime );
            System.out.println();
        }
        // algorithm: prints out the best result
        System.out.println("The best efficiency is for: " + bestEfficiency + " ambulances.");
    }
}
