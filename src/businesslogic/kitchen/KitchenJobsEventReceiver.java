package businesslogic.kitchen;

import java.util.List;

public interface KitchenJobsEventReceiver {
    void updateSummarySheetCreated(SummarySheet sheet);
    void updateSummarySheetDeleted(SummarySheet sheet);
    void updateJobAssigned(KitchenJob job);
    void updateJobUnassigned(KitchenJob job);
    void updateJobInfoAssigned(KitchenJob job);
    void updateJobCompleted(KitchenJob job);
    void updateJobsRearranged(SummarySheet sheet);
    void updateJobsRemoved(List<KitchenJob> jobs);
    void updateJobRemoved(KitchenJob job);
    void updateJobAdded(Integer serviceId, KitchenJob job, Integer position);
}
