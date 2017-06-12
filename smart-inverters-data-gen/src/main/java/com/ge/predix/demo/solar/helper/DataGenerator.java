package com.ge.predix.demo.solar.helper;

import com.ge.predix.demo.solar.model.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by 212539039 on 4/24/2017.
 */
public class DataGenerator {

    private static final Set<DayOfWeek> WEEKEND_LIST = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    public static List<TimeSeries> generateMonthlyConsumption(List<ConsumerProfile> consumerProfileList, int monthsBefore) {
        List<TimeSeries> result = new ArrayList<>();

        // calculate the period for which Time Series have to be generated (one month between (monthsBefore + 1) and monthsBefore)
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate startDate = LocalDateTime.now().minusMonths(monthsBefore + 1).toLocalDate();
        LocalDate endDate = LocalDateTime.now().minusMonths(monthsBefore).toLocalDate();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, midnight);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, midnight);

        for (; startDateTime.isBefore(endDateTime); startDateTime = startDateTime.plusDays(1)) {
            ConsumerProfile consumerProfile = null;
            // select the correct profile for the current day of week: WORKING or WEEKEND
            for (ConsumerProfile profile : consumerProfileList) {
                consumerProfile = profile;
                if (consumerProfile.getWeekDayType().equals(WeekDayType.WORKING) && !WEEKEND_LIST.contains(startDateTime.getDayOfWeek())
                        || consumerProfile.getWeekDayType().equals(WeekDayType.WEEKEND) && WEEKEND_LIST.contains(startDateTime.getDayOfWeek())) {
                    break;
                }
            }
            // generate a random consumption value per day
            Double dailyConsumption = ThreadLocalRandom.current().nextDouble(consumerProfile.getMinValue(), consumerProfile.getMaxValue());
            for (Interval interval : consumerProfile.getIntervals()) {
                List<Integer> indices = new ArrayList<>();
                // generate the value per interval
                for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                    indices.add(consumptionPercentage.getIndex());
                }
                // shuffle values for more randomness
                Collections.shuffle(indices);
                int currentIndex = 0;
                for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                    Double value = dailyConsumption * consumptionPercentage.getValue() / 100.0;
                    TimeSeries timeSeries = new TimeSeries();
                    timeSeries.setValue(value);
                    LocalDateTime current = startDateTime.plusHours(indices.get(currentIndex++));
                    timeSeries.setInstant(current.toInstant(ZoneOffset.UTC));
                    result.add(timeSeries);
                }
            }
        }
        return result;
    }

    /*
    public static List<TimeSeries> generateDailyConsumption(List<ConsumerProfile> consumerProfileList, int daysBefore) {
        List<TimeSeries> result = new ArrayList<>();

        // calculate the period for which Time Series have to be generated (one day)
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate startDate = LocalDateTime.now().minusDays(daysBefore + 1).toLocalDate();
        LocalDate endDate = LocalDateTime.now().minusDays(daysBefore).toLocalDate();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, midnight);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, midnight);

        ConsumerProfile consumerProfile = null;
        // select the correct profile for the current day of week: WORKING or WEEKEND
        for (ConsumerProfile profile : consumerProfileList) {
            consumerProfile = profile;
            if (consumerProfile.getWeekDayType().equals(WeekDayType.WORKING) && !WEEKEND_LIST.contains(startDateTime.getDayOfWeek())
                    || consumerProfile.getWeekDayType().equals(WeekDayType.WEEKEND) && WEEKEND_LIST.contains(startDateTime.getDayOfWeek())) {
                break;
            }
        }
        // generate a random consumption value per day
        Double dailyConsumption = ThreadLocalRandom.current().nextDouble(consumerProfile.getMinValue(), consumerProfile.getMaxValue());
        for (Interval interval : consumerProfile.getIntervals()) {
            List<Integer> indices = new ArrayList<>();
            // generate the value per interval
            for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                indices.add(consumptionPercentage.getIndex());
            }
            // shuffle values for more randomness
            Collections.shuffle(indices);
            int currentIndex = 0;
            for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                Double value = dailyConsumption * consumptionPercentage.getValue() / 100.0;
                TimeSeries timeSeries = new TimeSeries();
                timeSeries.setValue(value);
                LocalDateTime current = startDateTime.plusHours(indices.get(currentIndex++));
                timeSeries.setInstant(current.toInstant(ZoneOffset.UTC));
                result.add(timeSeries);
            }
        }
        return result;
    }

    public static List<TimeSeries> generateDailyGenerated(List<GeneratorProfile> generatorProfileList, int daysBefore) {
        List<TimeSeries> result = new ArrayList<>();

        // calculate the period for which Time Series have to be generated (one day)
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate startDate = LocalDateTime.now().minusDays(daysBefore + 1).toLocalDate();
        LocalDate endDate = LocalDateTime.now().minusDays(daysBefore).toLocalDate();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, midnight);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, midnight);

        // generate a random consumption value per day
        Double dailyConsumption = ThreadLocalRandom.current().nextDouble(consumerProfile.getMinValue(), consumerProfile.getMaxValue());
        for (Interval interval : consumerProfile.getIntervals()) {
            List<Integer> indices = new ArrayList<>();
            // generate the value per interval
            for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                indices.add(consumptionPercentage.getIndex());
            }
            // shuffle values for more randomness
            Collections.shuffle(indices);
            int currentIndex = 0;
            for (ConsumptionPercentage consumptionPercentage : interval.getValues()) {
                Double value = dailyConsumption * consumptionPercentage.getValue() / 100.0;
                TimeSeries timeSeries = new TimeSeries();
                timeSeries.setValue(value);
                LocalDateTime current = startDateTime.plusHours(indices.get(currentIndex++));
                timeSeries.setInstant(current.toInstant(ZoneOffset.UTC));
                result.add(timeSeries);
            }
        }
        return result;
    }

    */
}