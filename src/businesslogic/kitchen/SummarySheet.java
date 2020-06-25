package businesslogic.kitchen;

import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.recipe.KitchenTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SummarySheet {
    private List<KitchenJob> jobs;
    private ServiceInfo service;

    public SummarySheet(ServiceInfo service) {
        this.service = service;
        Menu menu = service.getMenu();
        List<MenuItem> menuItems = menu.getMenuItems();
        List<KitchenTask> neededTasks = new ArrayList<>();
        for (MenuItem item : menuItems) {
            neededTasks.add(item.getItemRecipe());
            neededTasks.addAll(item.getItemRecipe().getAllUsedPreparations());
        }
        jobs = neededTasks.stream()
                .map(KitchenJob::new)
                .collect(Collectors.toList());
    }

    public boolean containsJob(KitchenJob job) {
        return jobs.contains(job);
    }

    public void removeJob(KitchenJob job) {
        // TODO: Integrate w/ ShiftManager if job has shift
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
        this.jobs.removeAll(jobs);
    }

    public List<KitchenJob> getCompletedJobs() {
        return jobs.stream()
                .filter(KitchenJob::isComplete)
                .collect(Collectors.toList());
    }

    public void addJob(KitchenJob job) {
        jobs.add(job);
    }

    // TODO: Implement when ShiftManager available
}
