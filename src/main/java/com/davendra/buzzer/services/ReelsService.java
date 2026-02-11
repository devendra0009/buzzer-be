package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.ReelRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.ReelModel;

import java.io.IOException;
import java.util.List;

public interface ReelsService {
    public ReelModel createReel(ReelRequest reelRequest, Long userId) throws IOException;

    public GlobalApiResponse<?> getAllReels(int page, int size);

    public List<ReelModel> getReelByUserId(Long userId);
}
