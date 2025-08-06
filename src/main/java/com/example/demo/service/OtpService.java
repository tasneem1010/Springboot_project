package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.OtpData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {
    // store otps in a thread-safe map where the key is user email
    private final ConcurrentHashMap<String, OtpData> map = new ConcurrentHashMap<>();
    private final EmailSenderService emailSenderService;

    private int generateOtp(String email) {
        OtpData otpData = new OtpData();
        map.put(email, otpData);
        return otpData.getOtp();
    }

    private boolean sendOtp(int otp, String email) {
        return emailSenderService.sendEmail(email, "Password Reset Code", "Dear user,\n" +
                "This is your OTP to reset your password\n" +
                "\t" + otp + "\n" +
                "if you have not requested a password reset ignore this email");
    }

    public ResponseEntity<ApiResponse<Object>> validateOtp(String email, String otpString){
        int otp;
        try {
            otp = Integer.parseInt(otpString);
            if (otp < 100000 || otp > 999999)
                return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "OTP must be 6 digits", null);
        } catch (NumberFormatException e) {
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "OTP must be a number", null);
        }
        OtpData otpData = map.get(email);
        if (otpData == null)
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "No OTP exists for this email", null);
        if (otpData.isExpired())
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "OTP expired", null);
        if (otpData.getOtp() != otp)
            return ApiResponse.buildResponse(HttpStatus.BAD_REQUEST, false, "OTP is invalid", null);
        otpData.setVerified(true);
        System.out.println(map.get(email).toString());
        return  ApiResponse.buildResponse(HttpStatus.OK, true, "OTP is valid", null);
    }

    public ResponseEntity<ApiResponse<Object>> sendUserEmail(String email) {
        int otp = generateOtp(email);
        boolean sent = sendOtp(otp, email);
        return sent ? ApiResponse.buildResponse(HttpStatus.OK, true, "Email was sent", null)
                : ApiResponse.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "Email was not sent, check console for error", null);
    }

    public void clearOtp(String email) {
        map.remove(email);
    }

    public boolean otpExists(String email) {
        return map.containsKey(email);
    }

    public boolean isOtpVerified(String email) {
        return map.get(email).isVerified();
    }
}

