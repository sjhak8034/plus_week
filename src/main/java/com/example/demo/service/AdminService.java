package com.example.demo.service;

import com.example.demo.dto.ReportResponseDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: 4. find or save 예제 개선
    @Transactional
    public ReportResponseDto reportUsers(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        for(User user : users) {
            user.updateStatusToBlocked();
        }
        return new ReportResponseDto(users.stream().map(User::getId).toList());
    }
}
