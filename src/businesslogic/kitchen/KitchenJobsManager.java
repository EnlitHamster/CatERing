package businesslogic.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.shift.ShiftManager;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KitchenJobsManager {
    SummarySheet currentSummarySheet;
    private final List<KitchenJobsEventReceiver> eventReceivers;

    public KitchenJobsManager() {
        eventReceivers = new ArrayList<>();
    }

    public SummarySheet getCurrentSummarySheet(){
        return currentSummarySheet;
    }

    public SummarySheet createSummarySheet(ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException();
        if (!service.getEvent().isChef(user) || service.hasSummarySheet()) throw new KitchenJobsException();
        currentSummarySheet = new SummarySheet(service);
        return currentSummarySheet;
    }

    public void selectSummarySheet(ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException();
        if (!service.getEvent().isChef(user) || service.hasSummarySheet()) throw new KitchenJobsException();
        currentSummarySheet = service.getSummarySheet();
    }

    public void deleteSummarySheet(ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException();
        if (!service.getEvent().isChef(user) || !service.hasSummarySheet()) throw new KitchenJobsException();
        SummarySheet summarySheet = service.getSummarySheet();
        summarySheet.dispose();
        service.setSummarySheet(null);
    }

    public void addTask(KitchenTask task) throws UseCaseLogicException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        currentSummarySheet.addJob(task);
    }

    public void deleteOffMenuRecipe(KitchenTask task) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        if (currentSummarySheet.getService().getMenu().getNeededTasks().contains(task)) throw new KitchenJobsException();
        List<KitchenJob> jobsByTask = currentSummarySheet.getJobsByTask(task);
        ShiftManager.getInstance().removeAllKitchenJob(jobsByTask.stream().filter(KitchenJob::hasShift).collect(Collectors.toList()));
        currentSummarySheet.removeAllJobs(jobsByTask);
    }

    public void deleteJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (job.hasShift()) ShiftManager.getInstance().removeKitchenJob(job);
        currentSummarySheet.removeJob(job);
    }

    public void rearrangeJob(KitchenJob job, Integer position) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (position < 0 && position >= currentSummarySheet.jobsSize()) throw new KitchenJobsException();
        currentSummarySheet.moveJob(job, position);
    }

    public void assignJob(KitchenJob job, KitchenShift shift, User cook) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (cook != null && (!cook.isCook() || !shift.isAvailable(cook)) || job.hasShift()) throw new KitchenJobsException();
        ShiftManager.getInstance().addKitchenJob(job, shift);
        if (cook != null) job.setAssignedCook(cook);
    }

    public void unassignJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job) || !job.hasShift()) throw new UseCaseLogicException();
        ShiftManager.getInstance().removeKitchenJob(job);
        job.setAssignedCook(null);
    }

    public void assignJobInfo(KitchenJob job, Long time, Integer quantity) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (time != null) job.setTimeEstimate(time);
        if (quantity != null) job.setQuantity(quantity);
    }

    public List<KitchenShift> getKitchenShiftBoard() {
        return ShiftManager.getInstance().getKitchenShiftBoard();
    }

    public void setJobCompleted(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        job.setComplete();
    }

    public void setShiftComplete(KitchenShift shift, boolean complete) {
        ShiftManager.getInstance().setKitchenShiftComplete(shift, complete);
    }

    private void notifySummarySheetAdded(SummarySheet sheet) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetCreated(sheet);
        }
    }

    private void notifySummarySheetRemoved(SummarySheet sheet) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetDeleted(sheet);
        }
    }

    private void notifyJobAssigned(KitchenJob job) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobAssigned(job);
        }
    }

    private void notifyJobUnassigned(KitchenJob job) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobUnassigned(job);
        }
    }

    private void notifyJobInfoAssigned(KitchenJob job) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobInfoAssigned(job);
        }
    }

    private void notifyJobCompleted(KitchenJob job) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobCompleted(job);
        }
    }

    private void notifyJobsRearranged() {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobsRearranged(currentSummarySheet);
        }
    }

    private void notifyJobsRemoved(List<KitchenJob> jobs) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobsRemoved(jobs);
        }
    }

    private void notifyJobRemoved(KitchenJob job) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobRemoved(job);
        }
    }

    private void notifyJobAdded(KitchenJob job, Integer position) {
        for (KitchenJobsEventReceiver er : this.eventReceivers) {
            er.updateJobAdded(currentSummarySheet.getService().getId(), job, position);
        }
    }

    public void setCurrentSummarySheet(SummarySheet m) {
        this.currentSummarySheet = m;
    }

    public void addEventReceiver(KitchenJobsEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(KitchenJobsEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}
