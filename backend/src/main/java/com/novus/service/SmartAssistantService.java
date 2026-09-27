package com.novus.service;

import com.novus.dto.SmartAssistantRequestDTO;
import com.novus.dto.SmartAssistantResponseDTO;
import com.novus.entity.ConditionAssessment;
import com.novus.entity.Offer;
import com.novus.entity.SaleRequest;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.entity.Valuation;
import com.novus.entity.Variant;
import com.novus.entity.Warranty;
import com.novus.repository.ConditionAssessmentRepository;
import com.novus.repository.OfferRepository;
import com.novus.repository.SaleRequestRepository;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import com.novus.repository.ValuationRepository;
import com.novus.repository.WarrantyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SmartAssistantService {

    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final WarrantyRepository warrantyRepository;
    private final ConditionAssessmentRepository conditionAssessmentRepository;
    private final ValuationRepository valuationRepository;
    private final OfferRepository offerRepository;
    private final SaleRequestRepository saleRequestRepository;

    public SmartAssistantService(UserDeviceRepository userDeviceRepository,
                                 UserRepository userRepository,
                                 WarrantyRepository warrantyRepository,
                                 ConditionAssessmentRepository conditionAssessmentRepository,
                                 ValuationRepository valuationRepository,
                                 OfferRepository offerRepository,
                                 SaleRequestRepository saleRequestRepository) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.warrantyRepository = warrantyRepository;
        this.conditionAssessmentRepository = conditionAssessmentRepository;
        this.valuationRepository = valuationRepository;
        this.offerRepository = offerRepository;
        this.saleRequestRepository = saleRequestRepository;
    }

    public SmartAssistantResponseDTO processQuery(Long deviceId, String userEmail, SmartAssistantRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        UserDevice device = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Device does not belong to the user");
        }

        String rawQuery = request.getQuery() != null ? request.getQuery().trim() : "";
        String q = rawQuery.toLowerCase();

        Variant variant = device.getVariant();
        String modelName = variant.getModel().getName();
        String seriesName = variant.getModel().getSeries().getName();
        String storage = device.getStorageOverride() != null ? device.getStorageOverride() : variant.getStorage();
        String color = device.getColorOverride() != null ? device.getColorOverride() : variant.getColor();
        String ram = variant.getRam();
        BigDecimal basePrice = variant.getBasePrice();
        LocalDate releaseDate = variant.getModel().getReleaseDate();

        Optional<Valuation> valuationOpt = valuationRepository.findFirstByUserDeviceIdOrderByCalculatedAtDesc(deviceId);
        Optional<Warranty> warrantyOpt = warrantyRepository.findByUserDeviceId(deviceId);
        Optional<ConditionAssessment> conditionOpt = conditionAssessmentRepository.findFirstByUserDeviceIdOrderByCreatedAtDesc(deviceId);
        Optional<SaleRequest> saleOpt = saleRequestRepository.findByUserDeviceId(deviceId);
        List<Offer> offers = offerRepository.findByVariantIdOrderByPriceDesc(variant.getId());

        StringBuilder reply = new StringBuilder();

        // 1. GREETINGS & IDENTITY
        if (q.matches(".*\\b(hi|hello|hey|greetings|hola|good morning|good evening|who are you|what can you do|help)\\b.*")) {
            reply.append("👋 **Hello! I'm your NOVUS Smart AI Assistant.**\n\n")
                 .append("I am specialized in analyzing your **").append(modelName).append(" (").append(storage).append(", ").append(color).append(")**. ")
                 .append("I can help you with:\n")
                 .append("• 💰 **Valuation & Pricing Insights:** Current market worth, depreciation factors, and future value\n")
                 .append("• 📈 **Selling Strategy:** Optimal time to sell, market trends, and trade-in readiness\n")
                 .append("• 🛡️ **Warranty & Protection:** Remaining days, claims coverage, and value retention\n")
                 .append("• 🔋 **Battery & Diagnostics:** Health analysis, replacement thresholds, and performance\n")
                 .append("• 📱 **Technical Specs:** Chipset, display, camera system, and hardware details\n")
                 .append("• 🏪 **Offers & Liquidation:** Local dealer bids and step-by-step sale verification\n\n")
                 .append("What would you like to explore about your device?");
        }

        // 2. DEVICE SPECIFICATIONS & HARDWARE DETAILS
        else if (q.matches(".*\\b(spec|specs|specification|specifications|hardware|feature|features|details about phone|camera|display|screen|chip|processor|processor|ram|storage|resolution|refresh rate|5g|magsafe|ip68|water)\\b.*")
                && !q.contains("price") && !q.contains("worth") && !q.contains("value") && !q.contains("sell")) {

            String chip = getChipForModel(modelName);
            String displayTech = getDisplayForModel(modelName);
            String cameraSystem = getCameraForModel(modelName);

            reply.append("📱 **Technical Specifications for ").append(modelName).append("**\n\n")
                 .append("• **Model Series:** ").append(seriesName).append("\n")
                 .append("• **Internal Storage:** ").append(storage).append(" NVMe fast storage\n")
                 .append("• **System Memory (RAM):** ").append(ram).append("\n")
                 .append("• **Finish / Color:** ").append(color).append("\n")
                 .append("• **Processor:** Apple ").append(chip).append(" with Neural Engine\n")
                 .append("• **Display:** ").append(displayTech).append("\n")
                 .append("• **Camera Setup:** ").append(cameraSystem).append("\n")
                 .append("• **Original Launch Price:** ₹").append(String.format("%,.2f", basePrice.doubleValue())).append("\n")
                 .append("• **Release Date:** ").append(releaseDate != null ? releaseDate.toString() : "Apple Official").append("\n")
                 .append("• **Connectivity:** 5G (Sub-6 GHz & mmWave), Wi-Fi 6/6E, Bluetooth, Ultra Wideband (UWB)\n")
                 .append("• **Build & Protection:** Ceramic Shield front, Aerospace-grade frame, IP68 water & dust resistance (6m for up to 30 mins)\n\n")
                 .append("💡 *Tip: High storage configurations like ").append(storage).append(" maintain significantly higher trade-in velocity in local secondary markets.*");
        }

        // 3. VALUATION, PRICE, WORTH, DEPRECIATION
        else if (q.matches(".*\\b(value|price|worth|valuation|how much|cost|depreciation|depreciate|rate|quote|appraisal)\\b.*")) {
            if (valuationOpt.isPresent()) {
                Valuation val = valuationOpt.get();
                reply.append("💰 **Valuation Breakdown for your ").append(modelName).append("**\n\n")
                     .append("• **Estimated Resale Value:** **₹").append(String.format("%,.2f", val.getEstimatedValue().doubleValue())).append("**\n")
                     .append("• **Fair Market Range:** ₹").append(String.format("%,.2f", val.getLowerRange().doubleValue()))
                     .append(" — ₹").append(String.format("%,.2f", val.getUpperRange().doubleValue())).append("\n")
                     .append("• **Confidence Score:** ").append(val.getConfidenceScore()).append("%\n")
                     .append("• **Device Age Factor:** ").append(val.getAgeMonths()).append(" months old\n")
                     .append("• **Condition Score Applied:** ").append(val.getConditionScore()).append("/100\n\n")
                     .append("📊 **Algorithmic Explanation:**\n")
                     .append(val.getExplanation()).append("\n\n");

                if (val.getTrendMultiplier() != null) {
                    double mult = val.getTrendMultiplier().doubleValue();
                    if (mult > 1.0) {
                        reply.append("📈 **Market Trend Alert:** Demand for this model is currently **RISING** (+").append(String.format("%.1f", (mult - 1.0) * 100)).append("%). Premium resale buyers are active.");
                    } else if (mult < 1.0) {
                        reply.append("📉 **Market Trend Alert:** Market trend is **DEPRECIATING** (-").append(String.format("%.1f", (1.0 - mult) * 100)).append("%). Value is expected to decline by another 4-8% over the next quarter.");
                    } else {
                        reply.append("⚖️ **Market Trend Alert:** Secondary market pricing is **STABLE** right now.");
                    }
                }
            } else {
                reply.append("⚠️ **No Valuation Generated Yet**\n\n")
                     .append("To get an accurate, explainable market valuation for your **").append(modelName).append(" (").append(storage).append(")**:\n")
                     .append("1. Complete a quick **Condition Assessment** (Battery, Display, Body, Camera)\n")
                     .append("2. Our deterministic valuation engine will calculate the exact fair-market valuation and local buy-back quotes!");
            }
        }

        // 4. SELLING STRATEGY & "SHOULD I SELL NOW?"
        else if (q.matches(".*\\b(sell|selling|should i sell|best time to sell|when to sell|trade in|upgrade|cash out)\\b.*")) {
            reply.append("🎯 **Strategic Selling Advisory for your ").append(modelName).append("**\n\n");

            if (valuationOpt.isPresent()) {
                Valuation val = valuationOpt.get();
                double trend = val.getTrendMultiplier() != null ? val.getTrendMultiplier().doubleValue() : 1.0;
                int age = val.getAgeMonths() != null ? val.getAgeMonths() : 12;

                if (trend < 0.98 || age > 24) {
                    reply.append("⚡ **Recommendation: SELL SOON**\n")
                         .append("• Your device has reached **").append(age).append(" months** of lifecycle age, where depreciation accelerates dramatically.\n")
                         .append("• Current market trend is downward due to newer Apple releases. Selling now secures approximately **₹")
                         .append(String.format("%,.2f", val.getEstimatedValue().doubleValue())).append("** before the next major price drop.\n");
                } else {
                    reply.append("✅ **Recommendation: OPTIONAL HOLD / GOOD POSITION**\n")
                         .append("• Secondary market demand for ").append(modelName).append(" remains resilient.\n")
                         .append("• Your estimated retention value stands strong at **₹").append(String.format("%,.2f", val.getEstimatedValue().doubleValue())).append("**.\n");
                }
            } else {
                reply.append("• **Key Market Rule:** Smartphones depreciate by 15-20% immediately upon next-generation product announcements.\n")
                     .append("• For maximum recovery, sell while battery health is above **80%** and the exterior is free of structural dents.\n");
            }

            if (!offers.isEmpty()) {
                Offer topOffer = offers.get(0);
                reply.append("\n🏪 **Top Local Instant Offer Available:**\n")
                     .append("• **₹").append(String.format("%,.2f", topOffer.getPrice().doubleValue())).append("** offered by **")
                     .append(topOffer.getShopName()).append("** (").append(topOffer.getDistance()).append(" km away, via ")
                     .append(topOffer.getPaymentMethod()).append(").\n")
                     .append("Check the **Local Offers** section below to accept and initiate pickup!");
            } else {
                reply.append("\n💡 You can list this device on NOVUS to receive competitive local merchant bids with zero commission fees.");
            }
        }

        // 5. HOW TO PREPARE PHONE FOR SALE / DATA SAFETY
        else if (q.matches(".*\\b(prepare|prep|ready to sell|wipe|reset|factory reset|backup|icloud|find my|data|erase|safety|tradein checklist)\\b.*")) {
            reply.append("🔒 **Pre-Sale Preparation Checklist for ").append(modelName).append("**\n\n")
                 .append("Follow these 5 essential steps before handing your phone to a buyer:\n\n")
                 .append("1. **Complete a Full Backup:**\n")
                 .append("   • Go to *Settings > [Your Name] > iCloud > iCloud Backup > Back Up Now* or connect to Finder/PC.\n\n")
                 .append("2. **Unpair Apple Watch (if applicable):**\n")
                 .append("   • Open the Watch app on your iPhone, go to *My Watch > All Watches*, tap the (i) icon and select *Unpair Apple Watch*.\n\n")
                 .append("3. **Sign Out of Apple ID & Turn Off 'Find My':**\n")
                 .append("   • Go to *Settings > [Your Name] > Sign Out*. This disables Activation Lock so the new owner can set up the device.\n\n")
                 .append("4. **Erase All Content and Settings:**\n")
                 .append("   • Go to *Settings > General > Transfer or Reset iPhone > Erase All Content and Settings*.\n\n")
                 .append("5. **Physical Cleanup:**\n")
                 .append("   • Remove SIM card / delete eSIM profiles, clean the Lightning/USB-C port and camera lenses. Clean devices inspect at +10% higher condition grade!");
        }

        // 6. WARRANTY & APPLECARE ADVISORY
        else if (q.matches(".*\\b(warranty|applecare|guarantee|insurance|coverage|claim|expired|under warranty)\\b.*")) {
            reply.append("🛡️ **Warranty & Protection Status**\n\n");
            if (warrantyOpt.isPresent()) {
                Warranty war = warrantyOpt.get();
                LocalDate expiresAt = war.getStartsAt().plusMonths(war.getWarrantyMonths());
                long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiresAt);
                boolean isActive = daysRemaining > 0;

                reply.append("• **Warranty Plan:** ").append(war.getWarrantyMonths()).append(" Months Coverage\n")
                     .append("• **Valid Period:** ").append(war.getStartsAt()).append(" to **").append(expiresAt).append("**\n")
                     .append("• **Live Status:** ").append(isActive ? "🟢 **ACTIVE** (" + daysRemaining + " days remaining)" : "🔴 **EXPIRED**").append("\n\n")
                     .append("📋 **What Standard Apple Warranty Covers:**\n")
                     .append("• Manufacturing hardware defects (logic board, camera sensor failure, Face ID failure)\n")
                     .append("• Battery degradation if capacity drops below 80% during the warranty window\n\n")
                     .append("❌ **What is NOT Covered Without AppleCare+:**\n")
                     .append("• Accidental display cracks or liquid damage\n\n");

                if (isActive) {
                    reply.append("✨ *Resale Advantage: Having an active warranty increases buyer confidence and can yield 10-15% higher resale prices!*");
                } else {
                    reply.append("💡 *Your warranty has expired. Keeping the phone in a shockproof case and maintaining >80% battery health will preserve the remaining valuation.*");
                }
            } else {
                reply.append("No active warranty record is linked to this device.\n\n")
                     .append("You can record your warranty duration in the **Warranty Status** panel below. Having verifiable warranty documentation directly boosts your phone's valuation rating!");
            }
        }

        // 7. BATTERY HEALTH & OPTIMIZATION
        else if (q.matches(".*\\b(battery|health|drain|drainage|battery life|replace battery|charging|cycle|charge|mah|overheat)\\b.*")) {
            reply.append("🔋 **Battery Health Diagnostics & Advice**\n\n");
            if (conditionOpt.isPresent()) {
                int bh = conditionOpt.get().getBatteryHealth();
                reply.append("• **Your Recorded Battery Health:** **").append(bh).append("%**\n\n");

                if (bh >= 90) {
                    reply.append("🟢 **Optimal Condition:** Your battery is in peak performance condition. No service is needed. Resale value penalty is 0%.\n");
                } else if (bh >= 80) {
                    reply.append("🟡 **Normal Wear:** Your battery still delivers standard peak performance. Expect normal daily wear.\n");
                } else {
                    reply.append("🔴 **Degraded / Service Recommended:** Battery is below Apple's 80% critical threshold. Performance management throttling may occur.\n")
                         .append("• An official Apple battery replacement costs ~₹6,500 - ₹8,500. Replacing it can increase resale valuation by up to ₹10,000!\n");
                }
            } else {
                reply.append("Please submit a condition assessment to record your exact battery health percentage.\n\n");
            }

            reply.append("\n⚡ **Proven Tips to Prolong iPhone Battery Life:**\n")
                 .append("1. **Enable Optimized Battery Charging:** *Settings > Battery > Battery Health & Charging*\n")
                 .append("2. **Avoid Heat Spikes:** Never leave your phone in direct sunlight or on heavy gaming while fast charging\n")
                 .append("3. **Keep Battery Between 20% and 80%:** Lithium-ion cells suffer minimum stress in this range\n")
                 .append("4. **Use Apple MFi / USB-PD Certified Chargers:** Prevents voltage ripple damage to internal power management ICs.");
        }

        // 8. CONDITION ASSESSMENT & GRADING IMPACT
        else if (q.matches(".*\\b(condition|grade|physical|scratch|dent|crack|damage|screen condition|body condition|camera condition)\\b.*")) {
            reply.append("🔍 **Physical Condition & Valuation Impact**\n\n");
            if (conditionOpt.isPresent()) {
                ConditionAssessment ca = conditionOpt.get();
                reply.append("• **Overall Condition Score:** **").append(ca.getOverallScore()).append("/100**\n")
                     .append("• **Display:** ").append(ca.getDisplayCondition()).append("\n")
                     .append("• **Body & Enclosure:** ").append(ca.getBodyCondition()).append("\n")
                     .append("• **Camera Lenses:** ").append(ca.getCameraCondition()).append("\n")
                     .append("• **Charging Port:** ").append(ca.getChargingCondition()).append("\n")
                     .append("• **Battery Maximum Capacity:** ").append(ca.getBatteryHealth()).append("%\n\n")
                     .append("⚖️ **Weighting Breakdown in NOVUS Algorithm:**\n")
                     .append("• Battery Health: **30%**\n")
                     .append("• Display Quality: **25%**\n")
                     .append("• Body / Chassis: **20%**\n")
                     .append("• Camera Module: **15%**\n")
                     .append("• Charging & Ports: **10%**\n\n")
                     .append("💡 *Tip: Devices graded 'EXCELLENT' or 'GOOD' qualify for instant dealer payouts through the Local Offers marketplace.*");
            } else {
                reply.append("You haven't performed a condition assessment yet. Click **Start Condition Assessment** on the device page to calculate your grade!");
            }
        }

        // 9. OFFERS & SALE REQUEST STATUS
        else if (q.matches(".*\\b(offer|offers|dealer|buyer|shop|deal|sale request|status|track|payout|payment)\\b.*")) {
            reply.append("🏪 **Local Marketplace & Sale Status**\n\n");
            if (saleOpt.isPresent()) {
                SaleRequest sr = saleOpt.get();
                reply.append("• **Active Sale Status:** `").append(sr.getStatus()).append("`\n")
                     .append("• **Created On:** ").append(sr.getCreatedAt()).append("\n\n")
                     .append("🔄 **Workflow Progress:**\n")
                     .append("1. `DRAFT` ➔ 2. `SUBMITTED` ➔ 3. `VERIFICATION_PENDING` ➔ 4. `VERIFIED` ➔ 5. `PAYMENT_PENDING` ➔ 6. `COMPLETED`\n\n")
                     .append("You can advance or verify your sale status directly from the **Sell Device** panel.");
            } else if (!offers.isEmpty()) {
                reply.append("You have **").append(offers.size()).append(" verified local merchant offers** for this device:\n\n");
                for (int i = 0; i < Math.min(3, offers.size()); i++) {
                    Offer off = offers.get(i);
                    reply.append(i + 1).append(". **₹").append(String.format("%,.2f", off.getPrice().doubleValue()))
                         .append("** — *").append(off.getShopName()).append("* (").append(off.getDistance()).append(" km, ")
                         .append(off.getPaymentMethod()).append(")\n");
                }
                reply.append("\nClick **Accept** on any offer in the Sell section to lock in your price!");
            } else {
                reply.append("No active sale request or offers currently placed. Ensure your phone has an active valuation to view curated merchant buy-back bids.");
            }
        }

        // 10. REPAIR VS SELL ADVICE
        else if (q.matches(".*\\b(repair|fix|screen repair|replace screen|cost to fix|repair or sell|broken)\\b.*")) {
            reply.append("🛠️ **Repair vs. Sell Economic Analysis**\n\n")
                 .append("• **Display Replacement:** OEM screen replacement costs ~₹18,000 - ₹28,000 for OLED iPhones. If your screen is only scratched or has minor hairline cracks, selling as 'FAIR' is often more profitable than paying for a replacement.\n")
                 .append("• **Back Glass:** Replacing back glass on modern iPhones is labor-intensive. Aftermarket back glass repairs void water resistance and reduce dealer appraisal confidence.\n")
                 .append("• **Battery Replacement:** If battery health is <80%, paying ₹6,500 - ₹8,500 for a certified replacement typically recoups 100-140% of the cost during trade-in resale.\n\n")
                 .append("💡 *Recommendation: Sell as-is through NOVUS local dealer bids to avoid out-of-pocket repair risks.*");
        }

        // 11. SOFTWARE UPDATES & IOS LIFECYCLE
        else if (q.matches(".*\\b(ios|update|updates|support|future proof|ai|apple intelligence|how long will it last)\\b.*")) {
            reply.append("🍏 **iOS Support & Future-Proofing for ").append(modelName).append("**\n\n")
                 .append("• **Apple Software Policy:** Apple typically provides **5 to 7 years** of major iOS updates plus subsequent critical security patches.\n")
                 .append("• **Device Outlook:** Your ").append(modelName).append(" will remain secure and fully capable of running contemporary apps for years to come.\n")
                 .append("• **Apple Intelligence Note:** On-device Apple Intelligence requires an A17 Pro (iPhone 15 Pro / Pro Max) or A18 / A18 Pro (iPhone 16 series) with 8GB RAM minimum.\n\n")
                 .append("If AI features are critical to your workflow, upgrading to an iPhone 15 Pro or iPhone 16 series is recommended!");
        }

        // 12. GENERAL INTELLIGENT FALLBACK
        else {
            reply.append("🤖 **NOVUS Smart AI Assistant**\n\n")
                 .append("I analyzed your inquiry: *\"").append(rawQuery).append("\"*\n\n")
                 .append("Here is how I can assist with your **").append(modelName).append(" (").append(storage).append(", ").append(color).append(")**:\n\n")
                 .append("• Ask **\"What is my phone's current value?\"** for an in-depth financial quote\n")
                 .append("• Ask **\"Should I sell now or wait?\"** for market trend predictions\n")
                 .append("• Ask **\"How is my battery health?\"** for diagnostic tips\n")
                 .append("• Ask **\"What are the technical specs of my phone?\"** for complete hardware details\n")
                 .append("• Ask **\"How do I prepare my phone for sale?\"** for data wiping safety instructions\n")
                 .append("• Ask **\"Explain my warranty\"** for coverage details\n\n")
                 .append("Try asking any of the above!");
        }

        return new SmartAssistantResponseDTO(reply.toString());
    }

    private String getChipForModel(String model) {
        String m = model.toLowerCase();
        if (m.contains("16 pro")) return "A18 Pro (3nm, 6-core CPU, 6-core GPU, 16-core NPU)";
        if (m.contains("16")) return "A18 (3nm, 6-core CPU, 5-core GPU, 16-core NPU)";
        if (m.contains("15 pro")) return "A17 Pro (3nm, 6-core CPU, 6-core GPU with Ray Tracing)";
        if (m.contains("15")) return "A16 Bionic (4nm, 6-core CPU, 5-core GPU)";
        if (m.contains("14 pro")) return "A16 Bionic (4nm, 6-core CPU, 5-core GPU)";
        if (m.contains("14")) return "A15 Bionic (5nm, 6-core CPU, 5-core GPU)";
        if (m.contains("13")) return "A15 Bionic (5nm, 6-core CPU, 4/5-core GPU)";
        if (m.contains("12")) return "A14 Bionic (5nm, 6-core CPU, 4-core GPU)";
        if (m.contains("11")) return "A13 Bionic (7nm, 6-core CPU, 4-core GPU)";
        if (m.contains("se (3rd")) return "A15 Bionic (5nm)";
        if (m.contains("se")) return "A13 Bionic (7nm)";
        return "Apple Silicon High-Performance Bionic";
    }

    private String getDisplayForModel(String model) {
        String m = model.toLowerCase();
        if (m.contains("16 pro max")) return "6.9-inch Super Retina XDR OLED, 120Hz ProMotion, Always-On, Dynamic Island, 2000 nits";
        if (m.contains("16 pro")) return "6.3-inch Super Retina XDR OLED, 120Hz ProMotion, Always-On, Dynamic Island, 2000 nits";
        if (m.contains("16 plus") || m.contains("15 plus") || m.contains("14 plus")) return "6.7-inch Super Retina XDR OLED, 60Hz, Dynamic Island / True Tone";
        if (m.contains("16") || m.contains("15")) return "6.1-inch Super Retina XDR OLED, Dynamic Island, up to 2000 nits peak";
        if (m.contains("15 pro max") || m.contains("14 pro max")) return "6.7-inch Super Retina XDR OLED, 120Hz ProMotion, Always-On, Dynamic Island";
        if (m.contains("15 pro") || m.contains("14 pro")) return "6.1-inch Super Retina XDR OLED, 120Hz ProMotion, Always-On, Dynamic Island";
        if (m.contains("13 pro max") || m.contains("12 pro max") || m.contains("11 pro max")) return "6.7/6.5-inch Super Retina XDR OLED, HDR10, Dolby Vision";
        if (m.contains("13 pro") || m.contains("12 pro") || m.contains("11 pro")) return "6.1/5.8-inch Super Retina XDR OLED, HDR10, True Tone";
        if (m.contains("mini")) return "5.4-inch Super Retina XDR OLED, 476 ppi, Compact Form-factor";
        if (m.contains("se")) return "4.7-inch Retina HD IPS LCD with True Tone";
        return "6.1-inch Super Retina XDR OLED with Ceramic Shield";
    }

    private String getCameraForModel(String model) {
        String m = model.toLowerCase();
        if (m.contains("16 pro")) return "Triple setup: 48MP Fusion + 48MP Ultra Wide + 12MP 5x Telephoto, 4K 120fps Dolby Vision, Spatial Video";
        if (m.contains("16")) return "Dual setup: 48MP Fusion (2x Telephoto) + 12MP Ultra Wide with Macro, Camera Control button";
        if (m.contains("15 pro max")) return "Triple setup: 48MP Main + 12MP Ultra Wide + 12MP 5x Tetraprism Telephoto, Photonic Engine";
        if (m.contains("15 pro")) return "Triple setup: 48MP Main + 12MP Ultra Wide + 12MP 3x Telephoto, ProRes, Log Video";
        if (m.contains("15") || m.contains("14 pro")) return "48MP Main with sensor-shift OIS + 12MP Ultra Wide, 2x Optical Telephoto crop";
        if (m.contains("14 pro max")) return "Triple setup: 48MP Main + 12MP Ultra Wide + 12MP 3x Telephoto, Action Mode";
        if (m.contains("pro max") || m.contains("pro")) return "Triple setup: 12MP Wide + 12MP Ultra Wide + 12MP Telephoto with Night Mode portraits";
        if (m.contains("se")) return "Single 12MP Wide camera with Portrait Mode, Smart HDR, 4K 60fps";
        return "Dual 12MP setup: Wide + Ultra Wide with Night Mode and Deep Fusion";
    }
}
