package com.kaldar.kaldar.kaldarService.interfaces;

import com.kaldar.kaldar.dtos.request.FindAvailableDrycleanersRequest;
import com.kaldar.kaldar.dtos.response.AvailableDryCleanerResponse;
import org.springframework.data.domain.Page;

public interface DryCleanerQueryService {
    Page<AvailableDryCleanerResponse> findAvailable(FindAvailableDrycleanersRequest request);
}
