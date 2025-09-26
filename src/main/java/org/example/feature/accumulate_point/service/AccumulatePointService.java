package org.example.feature.accumulate_point.service;

import org.example.common.base.PageRequestModel;
import org.example.common.config.security.CurrentUser;
import org.example.common.constants.ChangeTypePointEnum;
import org.example.common.constants.ErrorCodeEnum;
import org.example.common.constants.RedisKeyEnum;
import org.example.common.dto.UserProfile;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.InternalServerErrorException;
import org.example.common.exception.NotFoundException;
import org.example.feature.accumulate_point.dto.request.SubtractPointDto;
import org.example.feature.accumulate_point.dto.response.CheckInDayResponseDto;
import org.example.feature.accumulate_point.dto.response.CheckInResponseDto;
import org.example.feature.accumulate_point.dto.response.SubTractPointResponseDto;
import org.example.feature.accumulate_point.entity.AccumulatePointHistory;
import org.example.feature.accumulate_point.repository.AccumulatePointHistoryRepository;
import org.example.feature.accumulate_point.repository.CheckinPointConfigRepository;
import org.example.feature.user.entity.User;
import org.example.feature.user.repository.UserRepository;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccumulatePointService {
    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccumulatePointHistoryRepository accumulatePointHistoryRepository;

    @Autowired
    private CheckinPointConfigRepository checkinPointConfigRepository;

    @Transactional
    public Object checkIn() {
        UserProfile userProfile = currentUser.getCurrentUser().getUser();

        RFencedLock lock = redissonClient.getFencedLock(RedisKeyEnum.RFENCEDLOCK_POINT.format(userProfile.getId()));
        LocalDateTime now = LocalDateTime.now();
        try {
            Long token = lock.lockAndGetToken();

            LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
            Duration ttl = Duration.between(now, midnight);

            checkTheTimeFrame(now);

            Boolean isCheckInToday = (Boolean) redissonClient.getBucket(RedisKeyEnum.CHECKIN_TODAY.format(userProfile.getId())).get();
            if(isCheckInToday != null) throw new BadRequestException(ErrorCodeEnum.TODAY_HAS_BEEN_CHECKIN);
            boolean isCheckInTodayDatabase = accumulatePointHistoryRepository.existsTodayRecord(userProfile.getId());
            if(isCheckInTodayDatabase) {
                redissonClient.getBucket(RedisKeyEnum.CHECKIN_TODAY.format(userProfile.getId())).set(true, ttl);
                throw new BadRequestException(ErrorCodeEnum.TODAY_HAS_BEEN_CHECKIN);
            }

            Integer totalDayCheckedIn = (Integer) redissonClient.getBucket(RedisKeyEnum.TOTAL_DAY_CHECKEDIN.format(userProfile.getId())).get();
            if(totalDayCheckedIn == null) {
                totalDayCheckedIn = accumulatePointHistoryRepository.countCheckinDaysInMonth(userProfile.getId());
            }
            if(totalDayCheckedIn >= 7) throw new BadRequestException(ErrorCodeEnum.MOTH_HAS_HAD_ENOUGH_CHECKINS);

            User user = userRepository.findById(userProfile.getId()).orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND));

            if(token != null && (user.getLastToken() == null || token > user.getLastToken())) {
                Integer point = checkinPointConfigRepository.getPointByDayIndex(totalDayCheckedIn + 1);
                if(point == null) throw new BadRequestException(ErrorCodeEnum.NOT_EXIST_DAY_ACCUMULATED_POINT);

                user.setLastToken(token);

                Long totalPoint = user.getTotalPoint() + point;
                user.setTotalPoint(totalPoint);
                userRepository.save(user);

                accumulatePointHistoryRepository.save(AccumulatePointHistory.builder()
                         .amount(point)
                         .changeType(ChangeTypePointEnum.INCREASE)
                         .user(user)
                        .build());

                redissonClient.getBucket(RedisKeyEnum.CHECKIN_TODAY.format(user.getId())).set(true, ttl);

                LocalDate firstDayNextMonth = now.toLocalDate()
                        .withDayOfMonth(1)
                        .plusMonths(1);
                LocalDateTime nextMonthStart = firstDayNextMonth.atStartOfDay();
                Duration ttlToEndOfMonth = Duration.between(now, nextMonthStart);

                redissonClient.getBucket(RedisKeyEnum.TOTAL_DAY_CHECKEDIN.format(user.getId())).set(totalDayCheckedIn + 1, ttlToEndOfMonth);

                return CheckInResponseDto.builder()
                        .userId(user.getId())
                        .totalPoint(totalPoint)
                        .build();
            }
            throw new InternalServerErrorException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);

        } catch (DataIntegrityViolationException e) {
            LocalDate firstDayNextMonth = now.toLocalDate()
                    .withDayOfMonth(1)
                    .plusMonths(1);
            LocalDateTime nextMonthStart = firstDayNextMonth.atStartOfDay();
            Duration ttlToEndOfMonth = Duration.between(now, nextMonthStart);

            int monthCountDb = accumulatePointHistoryRepository.countCheckinDaysInMonth(userProfile.getId());
            redissonClient.getBucket(RedisKeyEnum.TOTAL_DAY_CHECKEDIN.format(userProfile.getId())).set(monthCountDb, ttlToEndOfMonth);
            if(monthCountDb >= 7) {
                throw new BadRequestException(ErrorCodeEnum.MOTH_HAS_HAD_ENOUGH_CHECKINS);
            }
            throw new BadRequestException(ErrorCodeEnum.TODAY_HAS_BEEN_CHECKIN);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }

    public void checkTheTimeFrame(LocalDateTime localDateTime) {
//        LocalTime now = localDateTime.toLocalTime();
//        boolean inMorning = now.isAfter(LocalTime.of(9, 0)) && now.isBefore(LocalTime.of(11, 0));
//        boolean inEvening = now.isAfter(LocalTime.of(19, 0)) && now.isBefore(LocalTime.of(21, 0));
//
//        if (!inMorning && !inEvening) {
//            throw new BadRequestException(ErrorCodeEnum.OUTSIDE_CHECKIN_HOURS);
//        }
    }

    @Transactional
    public Object subTractPoint(SubtractPointDto subtractPointDto) {
        UserProfile userProfile = currentUser.getCurrentUser().getUser();

        RFencedLock lock = redissonClient.getFencedLock(RedisKeyEnum.RFENCEDLOCK_POINT.format(userProfile.getId()));
        try {
            Long token = lock.lockAndGetToken();
            User user = userRepository.findById(userProfile.getId()).orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USER_NOT_FOUND));

            if(token != null && (user.getLastToken() == null || token > user.getLastToken())) {
                Long totalPoint = user.getTotalPoint();
                if(totalPoint < subtractPointDto.getPoint()) throw new BadRequestException(ErrorCodeEnum.SUBTRACT_POINT_OVER_TOTAL_POINT);

                user.setTotalPoint(totalPoint - subtractPointDto.getPoint());
                user.setLastToken(token);
                user = userRepository.save(user);

                accumulatePointHistoryRepository.save(AccumulatePointHistory.builder()
                        .amount(subtractPointDto.getPoint())
                        .changeType(ChangeTypePointEnum.DECREASE)
                        .user(user)
                        .build());

                return SubTractPointResponseDto.builder()
                        .userId(user.getId())
                        .totalPoints(user.getTotalPoint())
                        .build();
            }
            throw new InternalServerErrorException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }

    public Object historyAccumulationPoint(PageRequestModel pageRequestModel, Date startTime, Date endTime) {
        UserProfile userProfile = currentUser.getCurrentUser().getUser();

        return accumulatePointHistoryRepository.findIncreaseHistoryByUserAndDate(
                userProfile.getId(), startTime, endTime, pageRequestModel.getLimit(), pageRequestModel.getLimit() * (pageRequestModel.getPage() - 1)
        );
    }

    public Object listDayCheckin() {
        UserProfile userProfile = currentUser.getCurrentUser().getUser();

        Integer totalDayCheckedIn = (Integer) redissonClient.getBucket(RedisKeyEnum.TOTAL_DAY_CHECKEDIN.format(userProfile.getId())).get();
        if(totalDayCheckedIn == null) {
            totalDayCheckedIn = accumulatePointHistoryRepository.countCheckinDaysInMonth(userProfile.getId());

            LocalDateTime now = LocalDateTime.now();
            LocalDate firstDayNextMonth = now.toLocalDate()
                    .withDayOfMonth(1)
                    .plusMonths(1);
            LocalDateTime nextMonthStart = firstDayNextMonth.atStartOfDay();
            Duration ttlToEndOfMonth = Duration.between(now, nextMonthStart);
            redissonClient.getBucket(RedisKeyEnum.TOTAL_DAY_CHECKEDIN.format(userProfile.getId()))
                    .set(totalDayCheckedIn, ttlToEndOfMonth);
        }

        List<CheckInDayResponseDto> checkInDayResponseDtoList = new ArrayList<>();
        for (int day = 1; day <= 7; day++) {
            checkInDayResponseDtoList.add(CheckInDayResponseDto.builder()
                    .day(day)
                    .isCheckIn(day <= totalDayCheckedIn)
                    .build());
        }

        return  checkInDayResponseDtoList;
    }
}
