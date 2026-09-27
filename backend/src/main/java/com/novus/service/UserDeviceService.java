package com.novus.service;

import com.novus.dto.UserDeviceRequestDTO;
import com.novus.dto.UserDeviceResponseDTO;
import com.novus.dto.VariantDTO;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.entity.Variant;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import com.novus.repository.VariantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final VariantRepository variantRepository;

    public UserDeviceService(UserDeviceRepository userDeviceRepository, UserRepository userRepository, VariantRepository variantRepository) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.variantRepository = variantRepository;
    }

    public UserDeviceResponseDTO registerDevice(UserDeviceRequestDTO request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Variant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found"));

        UserDevice device = new UserDevice();
        device.setUser(user);
        device.setVariant(variant);
        device.setPurchaseDate(request.getPurchaseDate());
        device.setPurchasePrice(request.getPurchasePrice());
        device.setStorageOverride(request.getStorageOverride());
        device.setColorOverride(request.getColorOverride());

        UserDevice saved = userDeviceRepository.save(device);
        return mapToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<UserDeviceResponseDTO> getUserDevices(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userDeviceRepository.findByUserId(user.getId(), pageable)
                .map(this::mapToResponseDTO);
    }

    @Transactional(readOnly = true)
    public UserDeviceResponseDTO getDevice(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDevice device = userDeviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        if (!device.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied");
        }
        return mapToResponseDTO(device);
    }

    @Transactional
    public void deleteDevice(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserDevice device = userDeviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        if (!device.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied");
        }
        
        // Clean up cascading references if needed, or let hibernate/flyway constraints handle it
        userDeviceRepository.delete(device);
    }

    private UserDeviceResponseDTO mapToResponseDTO(UserDevice device) {
        UserDeviceResponseDTO dto = new UserDeviceResponseDTO();
        dto.setId(device.getId());
        dto.setUserId(device.getUser().getId());
        
        Variant v = device.getVariant();
        VariantDTO variantDTO = new VariantDTO(v.getId(), v.getStorage(), v.getRam(), v.getColor(), v.getBasePrice(), v.getModel().getId());
        dto.setVariant(variantDTO);
        
        dto.setPurchaseDate(device.getPurchaseDate());
        dto.setPurchasePrice(device.getPurchasePrice());
        dto.setStorageOverride(device.getStorageOverride());
        dto.setColorOverride(device.getColorOverride());
        return dto;
    }
}
