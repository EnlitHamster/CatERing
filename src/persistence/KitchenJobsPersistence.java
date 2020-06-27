package persistence;

import businesslogic.kitchen.KitchenJob;
import businesslogic.kitchen.KitchenJobsEventReceiver;
import businesslogic.kitchen.SummarySheet;

import java.util.List;

public class KitchenJobsPersistence implements KitchenJobsEventReceiver {
    @Override
    public void updateSummarySheetCreated(SummarySheet sheet) {
        SummarySheet.saveNewSummarySheet(sheet);
    }

    @Override
    public void updateSummarySheetDeleted(SummarySheet sheet) {
        SummarySheet.deleteSummarySheet(sheet);
    }

    @Override
    public void updateJobAssigned(KitchenJob job) {
        KitchenJob.saveAssignment(job);
    }

    @Override
    public void updateJobUnassigned(KitchenJob job) {
        KitchenJob.saveUnassignment(job);
    }

    @Override
    public void updateJobInfoAssigned(KitchenJob job) {
        KitchenJob.saveInfoAssignment(job);
    }

    @Override
    public void updateJobCompleted(KitchenJob job) {
        KitchenJob.saveJobCompleted(job);
    }

    @Override
    public void updateJobsRearranged(SummarySheet sheet) {
        SummarySheet.saveJobsOrder(sheet);
    }

    @Override
    public void updateJobsRemoved(List<KitchenJob> jobs) {
        for (KitchenJob job : jobs) KitchenJob.deleteKitchenJob(job);
    }

    @Override
    public void updateJobRemoved(KitchenJob job) {
        KitchenJob.deleteKitchenJob(job);
    }

    @Override
    public void updateJobAdded(Integer serviceId, KitchenJob job, Integer position) {
        KitchenJob.saveNewKitchenJob(serviceId, job, position);
    }
}
