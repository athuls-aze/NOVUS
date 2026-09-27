package com.novus.service;

import com.novus.dto.WarrantyRequestDTO;
import com.novus.dto.WarrantyResponseDTO;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.entity.Warranty;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import com.novus.repository.WarrantyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;

    public WarrantyService(WarrantyRepository warrantyRepository, UserDeviceRepository userDeviceRepository, UserRepository userRepository) {
        this.warrantyRepository = warrantyRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
    }

    public WarrantyResponseDTO createOrUpdateWarranty(Long deviceId, String userEmail, WarrantyRequestDTO request) {
        UserDevice userDevice = validateAndGetDevice(deviceId, userEmail);

        Warranty warranty = warrantyRepository.findByUserDeviceId(deviceId).orElse(new Warranty());
        warranty.setUserDevice(userDevice);
        warranty.setWarrantyMonths(request.getWarrantyMonths());
        
        if (request.getStartsAt() != null) {
            warranty.setStartsAt(request.getStartsAt());
        } else if (warranty.getId() == null) {
            // Default to purchase date on creation if not provided
            warranty.setStartsAt(userDevice.getPurchaseDate() != null ? userDevice.getPurchaseDate() : LocalDate.now());
        }

        Warranty saved = warrantyRepository.save(warranty);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public WarrantyResponseDTO getWarranty(Long deviceId, String userEmail) {
        validateAndGetDevice(deviceId, userEmail);
        Warranty warranty = warrantyRepository.findByUserDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warranty not found for this device"));
        
        return mapToResponse(warranty);
    }

    private UserDevice validateAndGetDevice(Long deviceId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
                
        UserDevice userDevice = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!userDevice.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Device does not belong to the user");
        }
        
        return userDevice;
    }

    private WarrantyResponseDTO mapToResponse(Warranty warranty) {
        LocalDate expiresAt = warranty.getStartsAt().plusMonths(warranty.getWarrantyMonths());
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiresAt);
        
        WarrantyResponseDTO response = new WarrantyResponseDTO();
        response.setWarrantyMonths(warranty.getWarrantyMonths());
        response.setStartsAt(warranty.getStartsAt());
        response.setExpiresAt(expiresAt);
        
        if (daysRemaining > 0) {
            response.setStatus("ACTIVE");
            response.setDaysRemaining(daysRemaining);
        } else {
            response.setStatus("EXPIRED");
            response.setDaysRemaining(0L);
        }
        
        return response;
    }
}
