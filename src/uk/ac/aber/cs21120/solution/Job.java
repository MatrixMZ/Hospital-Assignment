package uk.ac.aber.cs21120.solution;

import uk.ac.aber.cs21120.hospital.IJob;

/**
 * This class contains implementation of IJob and Comparable interfaces,
 * comparable is used here help PriorityQueue class that is places in
 * the simulator to put the Job on the right place in the queue.
 *
 * @author Matuesz Ziobrowski(maz26@aber.ac.uk)
 *
 */
public class Job implements IJob, Comparable<IJob> {
    private int id;
    private int priority;
    private int duration;
    private int timeSubmitted = -1;

    /**
     * Initializes Job with the given parameters.
     *
     * @param id int
     * @param priority int
     * @param duration int
     */
    public Job(int id, int priority, int duration) {
        this.id = id;
        this.priority = priority;
        this.duration = duration;
    }

    /**
     * Getter for ID
     *
     * @return the job ID
     */
    public int getID() {
        return id;
    }

    /**
     * Getter for priority
     *
     * @return the priority of the job
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Run an update tick on this job. Simply decrements the number of ticks remaining
     * in the job.
     */
    public void tick() {
        duration--;
    }

    /**
     * Check if a job has completed
     *
     * @return true if job has completed
     */
    public boolean isDone() {
        return duration <= 0;
    }

    /**
     * Return the number of ticks since the job was added to the simulator. If called before
     * the job is submitted the result is undefined (a RuntimeException might also be thrown).
     *
     * @param now the current simulator tick number
     * @return the difference between now and the time the job was submitted
     */
    public int getTimeSinceSubmit(int now) {
        // the initial timeSubmitted is equal -1, it is changed when the simulator adds the new job to the priority queue
        // it helps to figure out how much time has passed since the Job was submitted
        if(timeSubmitted == -1) {
            throw new RuntimeException();
        } else {
            return now - timeSubmitted;
        }
    }

    /**
     * Sets the submit time - this will be called by the simulator
     *
     * @param time the time at which the job was added to the simulator
     */
    public void setSubmitTime(int time) {
        timeSubmitted = time;
    }

    /**
     * Returns the minus value when the given Job.priority is grater than the priority of this class,
     * if is smaller then returns plus value and returns 0 when thr priorities are equal.
     *
     * @param iJob
     * @return int
     */
    public int compareTo(IJob iJob) {
        return getPriority() - iJob.getPriority();
    }
}
