package businesslogic.kitchen;

import businesslogic.event.ServiceInfo;
import businesslogic.recipe.KitchenTask;
import businesslogic.shift.ShiftManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SummarySheet {
    private final List<KitchenJob> jobs;
    private final ServiceInfo service;

    public SummarySheet(ServiceInfo service) {
        this.service = service;
        jobs = service.getMenu().getNeededTasks().stream()
                .map(KitchenJob::new)
                .collect(Collectors.toList());
    }

    public boolean containsJob(KitchenJob job) {
        return jobs.contains(job);
    }

    public void removeJob(KitchenJob job) {
        ShiftManager.getInstance().removeKitchenJob(job);
        jobs.remove(job);
    }

    public void moveJob(KitchenJob job, Integer position) {
        jobs.remove(job);
        jobs.add(position, job);
    }

    public Integer jobsSize() {
        return jobs.size();
    }

    public List<KitchenJob> getJobsByTask(KitchenTask task) {
        List<KitchenJob> jobsByTask = new ArrayList<>();
        for (KitchenJob job : jobs) if (job.preparesTask(task)) jobsByTask.add(job);
        return jobsByTask;
    }

    public void removeAllJobs(Collection<KitchenJob> jobs) {
        ShiftManager.getInstance().removeAllKitchenJob(jobs);
        this.jobs.removeAll(jobs);
    }

    public List<KitchenJob> getCompletedJobs() {
        return jobs.stream()
                .filter(KitchenJob::isComplete)
                .collect(Collectors.toList());
    }

    public KitchenJob addJob(KitchenTask task) {
        KitchenJob job = new KitchenJob(task);
        jobs.add(job);
        return job;
    }

    public void dispose() {
        ShiftManager.getInstance().removeAllKitchenJob(jobs);
    }

    public ServiceInfo getService() {
        return service;
    }
}
