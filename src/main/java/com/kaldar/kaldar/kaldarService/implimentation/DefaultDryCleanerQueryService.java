package com.kaldar.kaldar.kaldarService.implimentation;

import com.kaldar.kaldar.domain.entities.DryCleanerEntity;
import com.kaldar.kaldar.domain.entities.ServiceOffering;
import com.kaldar.kaldar.domain.repository.DryCleanerEntityRepository;
import com.kaldar.kaldar.dtos.request.FindAvailableDrycleanersRequest;
import com.kaldar.kaldar.dtos.response.AvailableDryCleanerResponse;
import com.kaldar.kaldar.kaldarService.interfaces.DryCleanerQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DefaultDryCleanerQueryService implements DryCleanerQueryService {

    private final DryCleanerEntityRepository dryCleanerEntityRepository;

    public DefaultDryCleanerQueryService(DryCleanerEntityRepository dryCleanerEntityRepository) {
        this.dryCleanerEntityRepository = dryCleanerEntityRepository;
    }

    @Override
    public Page<AvailableDryCleanerResponse> findAvailable(FindAvailableDrycleanersRequest request) {
        int page = request.getPage() == null ? 0 : request.getPage();
        int size = request.getSize() == null ? 20 : request.getSize();
        Pageable pageable = PageRequest.of(page, size);
        Page<DryCleanerEntity> base = dryCleanerEntityRepository.findByIsActiveTrueAndVerifiedUserTrue(pageable);

        double lat = request.getLat();
        double lng = request.getLng();
        double radiusKm = request.getRadiusKm();
        List<String> services = request.getServices();
        Boolean openNow = request.getOpenNow();

        // Filter by distance using Haversine in-memory for now (PostGIS can replace this later)
        double radiusMeters = radiusKm * 1000.0;
        List<DryCleanerEntity> withinRadius = base.getContent().stream()
                .filter(dc -> dc.getLatitude() != null && dc.getLongitude() != null)
                .filter(dc -> haversineMeters(lat, lng, dc.getLatitude(), dc.getLongitude()) <= radiusMeters)
                .collect(Collectors.toList());

        // Optional services filter (ALL requested services must be present)
        if (services != null && !services.isEmpty()) {
            withinRadius = withinRadius.stream().filter(dc -> {
                List<String> names = dc.getServiceOfferings() == null ? List.of() :
                        dc.getServiceOfferings().stream().map(ServiceOffering::getServiceName)
                                .filter(Objects::nonNull)
                                .map(String::toLowerCase).toList();
                for (String s : services) {
                    if (!names.contains(s.toLowerCase())) return false;
                }
                return true;
            }).collect(Collectors.toList());
        }

        // TODO: openNow computation from workingHours if needed; currently ignored
        if (Boolean.TRUE.equals(openNow)) {
            // Placeholder: implement working hours check later
        }

        // Map to response and sort by distance
        List<AvailableDryCleanerResponse> mapped = new ArrayList<>();
        for (DryCleanerEntity dc : withinRadius) {
            double distM = haversineMeters(lat, lng, dc.getLatitude(), dc.getLongitude());
            AvailableDryCleanerResponse res = new AvailableDryCleanerResponse();
            res.setId(dc.getId());
            res.setBusinessName(dc.getBusinessName());
            res.setBusinessAddress(dc.getBusinessAddress());
            res.setLatitude(dc.getLatitude());
            res.setLongitude(dc.getLongitude());
            res.setDistanceKm(distM / 1000.0);
            if (dc.getServiceOfferings() != null) {
                res.setServices(dc.getServiceOfferings().stream()
                        .map(ServiceOffering::getServiceName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            }
            mapped.add(res);
        }
        mapped.sort((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()));

        return new PageImpl<>(mapped, pageable, mapped.size());
    }

    private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
