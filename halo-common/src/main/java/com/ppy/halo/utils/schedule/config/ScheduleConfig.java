package com.ppy.halo.utils.schedule.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author: jackie
 * @date: 2023/12/12 16:37
 **/
@Slf4j
@Configuration
public class ScheduleConfig {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean.getScheduler();
    }

}
