package com.example.demo.service;

import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public Map<String,Object> getStats(){
        Map<String,Object> stats = new LinkedHashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCompany", companyRepository.count());
        stats.put("averageUsersPerCompany", userRepository.averageUsersPerCompany());
        stats.put("biggestCompany",companyRepository.findBiggestCompany());
        stats.put("companiesWithoutUsers",companyRepository.findByNoUsers());
        return stats;
    }








}
