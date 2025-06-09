package com.example.webbyskymarket.service;

import com.example.webbyskymarket.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    public final FeedbackRepository feedbackRepository;
}
