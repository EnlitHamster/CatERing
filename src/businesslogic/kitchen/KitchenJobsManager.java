package businesslogic.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.shift.ShiftManager;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.List;

public class KitchenJobsManager {
    SummarySheet currentSummarySheet;
    private final List<KitchenJobsEventReceiver> eventReceivers;

    public KitchenJobsManager() {
        eventReceivers = new ArrayList<>();
    }

    public SummarySheet createSummarySheet(EventInfo event, ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef() || !event.containsService(service)) throw new UseCaseLogicException();
        if (!event.isChef(user) || service.hasSummarySheet()) throw new KitchenJobsException();
        currentSummarySheet = new SummarySheet(service);
        return currentSummarySheet;
    }

    public void selectSummarySheet(EventInfo event, ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef() || !event.containsService(service)) throw new UseCaseLogicException();
        if (!event.isChef(user) || service.hasSummarySheet()) throw new KitchenJobsException();
        currentSummarySheet = service.getSummarySheet();
    }

    public void deleteSummarySheet(EventInfo event, ServiceInfo service) throws UseCaseLogicException, KitchenJobsException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef() || !event.containsService(service)) throw new UseCaseLogicException();
        if (!event.isChef(user) || !service.hasSummarySheet()) throw new KitchenJobsException();
        SummarySheet summarySheet = service.getSummarySheet();
        summarySheet.dispose();
    }

    public void addTask(KitchenTask task) throws UseCaseLogicException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        currentSummarySheet.addJob(task);
    }

    public void deleteOffMenuRecipe(KitchenTask task) throws UseCaseLogicException, KitchenJobsException {
        if (currentSummarySheet == null) throw new UseCaseLogicException();
        if (currentSummarySheet.getService().getMenu().getNeededTasks().contains(task)) throw new KitchenJobsException();
        List<KitchenJob> jobsByTask = currentSummarySheet.getJobsByTask(task);
        currentSummarySheet.removeAllJobs(jobsByTask);
    }

    public void deleteJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
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
        job.setShift(shift);
        if (cook != null) job.setAssignedCook(cook);
    }

    public void unassignJob(KitchenJob job) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job) || !job.hasShift()) throw new UseCaseLogicException();
        ShiftManager.getInstance().removeKitchenJob(job);
        KitchenShift shift = job.getShift();
        job.setShift(null);
        job.setAssignedCook(null);
    }

    public void assignJobInfo(KitchenJob job, Long time, Integer quantity) throws UseCaseLogicException {
        if (currentSummarySheet == null || !currentSummarySheet.containsJob(job)) throw new UseCaseLogicException();
        if (time != null) job.setTimeEstimate(time);
        if (quantity != null) job.setQuantity(quantity);
    }
}
