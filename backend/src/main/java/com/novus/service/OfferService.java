package com.novus.service;

import com.novus.dto.OfferDTO;
import com.novus.entity.UserDevice;
import com.novus.repository.OfferRepository;
import com.novus.repository.UserDeviceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserDeviceRepository userDeviceRepository;

    public OfferService(OfferRepository offerRepository, UserDeviceRepository userDeviceRepository) {
        this.offerRepository = offerRepository;
        this.userDeviceRepository = userDeviceRepository;
    }

    public List<OfferDTO> getOffersForDevice(Long deviceId) {
        UserDevice device = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        return offerRepository.findByVariantIdOrderByPriceDesc(device.getVariant().getId()).stream()
                .map(o -> new OfferDTO(
                        o.getId(),
                        o.getShopName(),
                        o.getPrice(),
                        o.getLocation(),
                        o.getDistance(),
                        o.getVerificationMethod(),
                        o.getPaymentMethod()
                ))
                .collect(Collectors.toList());
    }
}
