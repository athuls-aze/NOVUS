package com.novus.service;

import com.novus.dto.SmartAssistantRequestDTO;
import com.novus.dto.SmartAssistantResponseDTO;
import com.novus.entity.Offer;
import com.novus.entity.UserDevice;
import com.novus.entity.User;
import com.novus.entity.Valuation;
import com.novus.entity.Variant;
import com.novus.repository.OfferRepository;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import com.novus.repository.ValuationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SmartAssistantService {

    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final ValuationRepository valuationRepository;

    public SmartAssistantService(UserDeviceRepository userDeviceRepository,
                                 UserRepository userRepository,
                                 OfferRepository offerRepository,
                                 ValuationRepository valuationRepository) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.valuationRepository = valuationRepository;
    }

    public SmartAssistantResponseDTO generateResponse(Long deviceId, String userEmail, SmartAssistantRequestDTO request) {
        // Security: verify the device belongs to the requesting user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        UserDevice device = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Device does not belong to you");
        }

        Variant variant = device.getVariant();
        String q = request.getQuery().toLowerCase();
        StringBuilder reply = new StringBuilder();

        String modelName = variant.getModel().getName();
        String storage = variant.getStorage();

        if (q.matches(".*\\b(should i sell|can i sell|good time to sell)\\b.*")) {
            Optional<Valuation> valuationOpt = valuationRepository.findFirstByUserDeviceIdOrderByCalculatedAtDesc(deviceId);
            if (valuationOpt.isPresent()) {
                Valuation val = valuationOpt.get();
                reply.append("[Sell Recommendation]\n\n");
                if (val.getTrendMultiplier().doubleValue() < 1.0) {
                    reply.append("Yes, it is highly recommended to sell your ").append(modelName).append(" now.\n")
                         .append("The market trend indicates that prices are depreciating (trend multiplier: ").append(val.getTrendMultiplier()).append(").\n")
                         .append("Current Estimated Value: ₹").append(String.format("%,d", val.getEstimatedValue().intValue())).append("\n")
                         .append("If you hold on to it longer, the value is expected to drop further.");
                } else if (val.getTrendMultiplier().doubleValue() > 1.0) {
                    reply.append("No, there is currently no rush to sell your ").append(modelName).append(".\n")
                         .append("The market trend is showing high demand/appreciation (trend multiplier: ").append(val.getTrendMultiplier()).append(").\n")
                         .append("Current Estimated Value: ₹").append(String.format("%,d", val.getEstimatedValue().intValue())).append("\n")
                         .append("You might get a better price if you wait a bit longer, but you can sell if you need the funds now.");
                } else {
                    reply.append("It's a neutral time to sell your ").append(modelName).append(".\n")
                         .append("The market price is stable right now.\n")
                         .append("Current Estimated Value: ₹").append(String.format("%,d", val.getEstimatedValue().intValue())).append("\n");
                }
            } else {
                reply.append("I need to calculate its value first. Please complete a condition assessment to generate a valuation for your ").append(modelName).append(".");
            }
        }
        else if (q.matches(".*\\b(value|worth|price|sell|valuation)\\b.*")) {
            Optional<Valuation> valuationOpt = valuationRepository.findFirstByUserDeviceIdOrderByCalculatedAtDesc(deviceId);
            if (valuationOpt.isPresent()) {
                Valuation val = valuationOpt.get();
                reply.append("[Valuation Quote]\n\n")
                     .append("- Model: ").append(modelName).append(" (").append(storage).append(")\n")
                     .append("- Base Price: ₹").append(String.format("%,d", variant.getBasePrice().intValue())).append("\n")
                     .append("- Current Est: ₹").append(String.format("%,d", val.getEstimatedValue().intValue())).append("\n")
                     .append("- Market Range: ₹").append(String.format("%,d", val.getLowerRange().intValue()))
                     .append(" — ₹").append(String.format("%,d", val.getUpperRange().intValue())).append("\n\n")
                     .append("_Confidence Score: ").append(val.getConfidenceScore()).append("%_");
            } else {
                reply.append("Please complete a condition assessment first to generate a valuation for your ").append(modelName).append(".");
            }
        }
        else if (q.matches(".*\\b(specs|hardware|processor|chip|screen|display|camera)\\b.*")) {
            reply.append("[Hardware Specs]\n\n")
                 .append("- Chip: High-Performance Apple Bionic\n")
                 .append("- RAM: ").append(variant.getRam()).append("\n")
                 .append("- Storage: ").append(storage).append("\n")
                 .append("- Display: Super Retina XDR OLED\n")
                 .append("- Camera: Advanced Ultra-Wide & Main System");
        }
        else if (q.matches(".*\\b(offer|offers|dealer|buyer|shop|deal|sale)\\b.*")) {
            List<Offer> offers = offerRepository.findByVariantIdOrderByPriceDesc(variant.getId());
            if (!offers.isEmpty()) {
                reply.append("[Top Local Offers]\n\n");
                for (int i = 0; i < Math.min(3, offers.size()); i++) {
                    Offer off = offers.get(i);
                    reply.append(i + 1).append(". ₹").append(String.format("%,d", off.getPrice().intValue()))
                         .append(" — ").append(off.getShopName()).append(" (").append(off.getDistance()).append(" km)\n");
                }
                reply.append("\n_Click 'Accept' in the Local Offers section to lock in a deal!_");
            } else {
                reply.append("No active merchant bids found for your ").append(modelName).append(" at the moment.");
            }
        }
        else if (q.matches(".*\\b(battery|health|charge)\\b.*")) {
            reply.append("[Battery Health Insights]\n\n")
                 .append("Your device is currently operating at optimal capacity.\n")
                 .append("If health falls below 80%, a replacement is recommended before selling to maximize value.");
        }
        else {
            reply.append("[NOVUS Smart Assistant]\n\n")
                 .append("I can help you with your ").append(modelName).append(":\n\n")
                 .append("- What is my phone's current value?\n")
                 .append("- What are the top local offers?\n")
                 .append("- Show me hardware specs\n")
                 .append("- Check battery health");
        }

        return new SmartAssistantResponseDTO(reply.toString());
    }
}
