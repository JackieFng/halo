package com.ppy.halo.utils.schedule;

import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: jackie
 * @date: 2022/10/12 16:40
 **/
@Component
public class ScheduleUtil {

    @Resource
    private Scheduler scheduler;

    /**
     * 添加定时任务Job
     *
     * @param jobName
     * @param jobGroup
     * @param cls
     * @param triggerName
     * @param triggerGroup
     * @param cronExpression
     */
    public void addJob(String jobName, String jobGroup, Class cls, String triggerName, String triggerGroup, String cronExpression, JobDataMap params) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(cls)
                    .usingJobData(params)
                    .withIdentity(jobName, jobGroup)
                    .storeDurably()
                    .build();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroup)
                    //该配置解决首次执行任务出现重复执行的问题
                    .startAt(DateBuilder.futureDate(90, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 暂停定时任务
     *
     * @param jobName
     * @param jobGroup
     */
    public void pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改定时任务触发时间
     *
     * @param triggerName
     * @param triggerGroupName
     * @param cronExpression
     */
    public void modifyJobCron(String triggerName, String triggerGroupName, String cronExpression) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) return;
            String oldCron = trigger.getCronExpression();
            if (!oldCron.equalsIgnoreCase(cronExpression)) {
                // trigger已存在，则更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(cronExpression);
                // 按新的cronExpression表达式重新构建trigger
                CronTrigger cronTrigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, cronTrigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 移除定时任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动所有定时任务
     */
    public void startAllJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭所有定时任务
     */
    public void shutdownAllJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
