package businesslogic.kitchen;

import businesslogic.CatERing;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.KitchenTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SummarySheet {
    private ObservableList<KitchenJob> jobs;
    private ServiceInfo service;

    private SummarySheet() {}

    public ObservableList<KitchenJob> getJobs() {
        return jobs;
    }

    public SummarySheet(ServiceInfo service) {
        this.service = service;
        if (service.getMenu() != null)
            jobs = FXCollections.observableArrayList(
                    service.getMenu().getNeededTasks().stream()
                        .map(KitchenJob::new)
                        .collect(Collectors.toList())
            );
    }

    public boolean containsJob(KitchenJob job) {
        return jobs.contains(job);
    }

    public void removeJob(KitchenJob job) {
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

    public KitchenJob addJob(KitchenTask task) {
        KitchenJob job = new KitchenJob(task);
        jobs.add(job);
        return job;
    }

    public void dispose() {
        CatERing.getInstance().getShiftManager().removeAllKitchenJob(jobs.stream().filter(KitchenJob::hasShift).collect(Collectors.toList()));
    }

    public ServiceInfo getService() {
        return service;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static void saveNewSummarySheet(SummarySheet sheet) {
        KitchenJob.saveAllNewKitchenJobs(sheet.service.getId(), sheet.jobs);
    }

    public static void deleteSummarySheet(SummarySheet sheet) {
        sheet.jobs.forEach(KitchenJob::deleteKitchenJob);
    }

    public static SummarySheet loadSummarySheetByService(ServiceInfo service) {
        List<Integer> jobs = new ArrayList<>();
        String query = "SELECT id,position FROM kitchenjobs WHERE service = " + service.getId() + " ORDER BY position ASC";
        PersistenceManager.executeQuery(query, rs -> jobs.add(rs.getInt("id")));
        if (jobs.size() > 0) {
            SummarySheet sheet = new SummarySheet();
            sheet.service = service;
            sheet.jobs = FXCollections.observableArrayList(jobs.stream()
                    .map(KitchenJob::loadKitchenJobById)
                    .collect(Collectors.toList()));
            return sheet;
        }
        return null;
    }

    public static void saveJobsOrder(SummarySheet sheet) {
        String upd = "UPDATE kitchenjobs SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, sheet.jobs.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, sheet.jobs.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}
