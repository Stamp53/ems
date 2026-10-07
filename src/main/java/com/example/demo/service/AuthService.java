package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
