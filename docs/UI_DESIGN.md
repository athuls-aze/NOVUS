# Frontend UI Architecture

## Identity
**Premium Technology Platform**
- **Vibe**: Futuristic, professional, clean.
- **Theme**: Dark-first with subtle gradients and controlled glassmorphism.
- **Typography**: High-quality, clear hierarchy.
- **Components**: Strong cards, useful data charts (Recharts), polished empty/loading states.
- **Avoid**: Generic admin dashboard look, overly busy Amazon-style layout, unnecessary excessive animations.

## Tech Stack
- React, TypeScript, Vite
- Tailwind CSS for styling
- Lucide React for iconography

## Navigation & Page Plan
1. **Landing Page**: Platform introduction and features.
2. **Auth**: Login / Register screens.
3. **Dashboard**: High-level overview of user's portfolio and market trends.
4. **My Devices**: List view of registered devices.
5. **Add Device**: Flow for selecting Brand → Series → Model → Variant and entering ownership data.
6. **Device Details**: Central hub for a single device.
7. **Warranty & Condition**: Sections for assessment and warranty tracking.
8. **Valuation**: Breakdown of estimated value with explainable factors.
9. **Market Offers**: Curated demo local offers.
10. **Smart Assistant**: Conversational chat interface for decision support.
11. **Sell Device**: Multi-step simulation of the sale/pickup workflow.
12. **Profile**: User settings.
