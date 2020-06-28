package businesslogic.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        SummarySheet sheet = new SummarySheet(service);
        service.setSummarySheet(sheet);
        setCurrentSummarySheet(sheet);
        this.notifySummarySheetAdded(currentSummarySheet);
        return currentSummarySheet;
    }

    public void selectSummarySheet(ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException();
        if (!service.getEvent().isChef(user) || !service.hasSummarySheet()) throw new KitchenJobsException();
        setCurrentSummarySheet(service.getSummarySheet());
    }

    public void deleteSummarySheet(ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException();
        if (!service.getEvent().isChef(user) || !service.hasSummarySheet()) throw new KitchenJobsException();
        SummarySheet summarySheet = service.getSummarySheet();
        summarySheet.dispose();
        service.setSummarySheet(null);
        this.notifySummarySheetRemoved(summarySheet);
    }

    public void addTask(KitchenTask task) throws UseCaseLogicException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        KitchenJob job = currentSummarySheet.addJob(task);
        this.notifyJobAdded(job, currentSummarySheet.jobsSize() - 1);
    }

    public void deleteOffMenuRecipe(KitchenTask task) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        if (currentSummarySheet.getService().getMenu().getNeededTasks().contains(task)) throw new KitchenJobsException();
        List<KitchenJob> jobsByTask = currentSummarySheet.getJobsByTask(task);
        CatERing.getInstance().getShiftManager().removeAllKitchenJob(jobsByTask.stream().filter(KitchenJob::hasShift).collect(Collectors.toList()));
        currentSummarySheet.removeAllJobs(jobsByTask);
        this.notifyJobsRemoved(jobsByTask);
    }

    public void deleteJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (job.hasShift()) CatERing.getInstance().getShiftManager().removeKitchenJob(job);
        currentSummarySheet.removeJob(job);
        this.notifyJobRemoved(job);
    }

    public void rearrangeJob(KitchenJob job, Integer position) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (position < 0 && position >= currentSummarySheet.jobsSize()) throw new KitchenJobsException();
        currentSummarySheet.moveJob(job, position);
        this.notifyJobsRearranged();
    }

    public void assignJob(KitchenJob job, KitchenShift shift, User cook) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (cook != null && (!cook.isCook() || !shift.isAvailable(cook))) throw new KitchenJobsException();
        job.setShift(shift);
        CatERing.getInstance().getShiftManager().addKitchenJob(job);
        if (cook != null) job.setAssignedCook(cook);
        this.notifyJobAssigned(job);
    }

    public void unassignJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job) || !job.hasShift()) throw new UseCaseLogicException();
        CatERing.getInstance().getShiftManager().removeKitchenJob(job);
        job.setShift(null);
        job.setAssignedCook(null);
        this.notifyJobUnassigned(job);
    }

    public void assignJobInfo(KitchenJob job, Long time, Integer quantity) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (time != null) job.setTimeEstimate(time);
        if (quantity != null) job.setQuantity(quantity);
        this.notifyJobInfoAssigned(job);
    }

    public List<KitchenShift> getKitchenShiftBoard() {
        return CatERing.getInstance().getShiftManager().getKitchenShiftBoard();
    }

    public void setJobCompleted(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        job.setComplete();
        this.notifyJobCompleted(job);
    }

    public void setShiftComplete(KitchenShift shift, boolean complete) {
        CatERing.getInstance().getShiftManager().setKitchenShiftComplete(shift, complete);
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

    public ObservableList<KitchenTask> getOffMenuKitchenTask() {
        ObservableList<KitchenTask> allTask = CatERing.getInstance().getKitchenTaskManager().getKitchenTasks();
        List<KitchenTask> jobKitchenTask = currentSummarySheet.getJobs().stream().map(KitchenJob::getItemTask).collect(Collectors.toList());
        return FXCollections.observableArrayList(
                allTask.stream()
                        .filter(t -> !currentSummarySheet.getService().getMenu().getNeededTasks().contains(t))
                        .filter(jobKitchenTask::contains)
                        .collect(Collectors.toList())
        );
    }
}
