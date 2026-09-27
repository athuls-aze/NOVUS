package com.novus.service;

import com.novus.dto.SaleRequestDTO;
import com.novus.entity.Offer;
import com.novus.entity.SaleRequest;
import com.novus.entity.SaleStatus;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.repository.OfferRepository;
import com.novus.repository.SaleRequestRepository;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class SaleRequestService {

    private final SaleRequestRepository saleRequestRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public SaleRequestService(SaleRequestRepository saleRequestRepository, UserDeviceRepository userDeviceRepository, UserRepository userRepository, OfferRepository offerRepository) {
        this.saleRequestRepository = saleRequestRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public SaleRequestDTO createSaleRequest(Long deviceId, Long offerId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        UserDevice device = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found"));

        Optional<SaleRequest> existing = saleRequestRepository.findByUserDeviceId(deviceId);
        if (existing.isPresent() && existing.get().getStatus() != SaleStatus.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Active sale request already exists");
        }

        SaleRequest request = new SaleRequest();
        request.setUserDevice(device);
        request.setUser(user);
        request.setSelectedOffer(offer);
        request.setStatus(SaleStatus.SUBMITTED);

        SaleRequest saved = saleRequestRepository.save(request);
        return mapToDTO(saved);
    }

    public SaleRequestDTO advanceState(Long deviceId, SaleStatus newStatus, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        SaleRequest request = saleRequestRepository.findByUserDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale request not found"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        request.setStatus(newStatus);
        SaleRequest saved = saleRequestRepository.save(request);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public SaleRequestDTO getSaleRequest(Long deviceId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        SaleRequest request = saleRequestRepository.findByUserDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale request not found"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return mapToDTO(request);
    }

    private SaleRequestDTO mapToDTO(SaleRequest req) {
        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setId(req.getId());
        dto.setDeviceId(req.getUserDevice().getId());
        if (req.getSelectedOffer() != null) {
            dto.setSelectedOfferId(req.getSelectedOffer().getId());
        }
        dto.setStatus(req.getStatus());
        return dto;
    }
}
