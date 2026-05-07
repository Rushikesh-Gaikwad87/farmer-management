# Kisan — Farmer Management Dashboard

A beautiful, handcrafted frontend for the Farmer Management System API.

## Features

- **Live Dashboard** — Real-time farmer statistics, state coverage, and land metrics
- **Full CRUD Operations** — Add, view, edit, and delete farmers
- **Advanced Filtering** — Filter by crop type, sort by name or land acres
- **Real-time Search** — Quick search filters as you type
- **State Summary** — Analytics view showing farmer count and land per state
- **Responsive Design** — Works on desktop and tablet
- **Toast Notifications** — Success and error feedback
- **Beautiful Animations** — Smooth page transitions and load effects

## Design

- **Dark Earthy Theme** — Warm soil tones, wheat gold accents, sage green highlights
- **Typography** — Playfair Display (headings) + DM Sans (body) + DM Mono (data)
- **No Build Required** — Single HTML file, open directly in the browser

## How to Use

1. Make sure the Spring Boot backend is running:
   ```bash
   mvn spring-boot:run
   ```

2. Open `frontend/index.html` directly in your browser (Chrome recommended)

3. The dashboard will auto-connect to `http://localhost:8080/api/farmers`

## What You Can Do

| Action | How |
|--------|-----|
| View all farmers | Home page displays paginated table |
| Search farmers | Use the search bar in the top right |
| Add farmer | Click "+ Add Farmer" button or use sidebar |
| Edit farmer | Click "✎ Edit" in the table actions |
| Delete farmer | Click "✕" in the table actions, confirm |
| View summary | Click "State Summary" in the sidebar |
| Filter/Sort | Use dropdowns in the table header |

## API Endpoints Used

- `GET /api/farmers` — Get all farmers
- `GET /api/farmers/summary` — Get state-level summary
- `POST /api/farmers` — Create new farmer
- `PUT /api/farmers/{id}` — Update farmer
- `DELETE /api/farmers/{id}` — Delete farmer

## Browser Support

- Chrome / Edge (recommended)
- Firefox
- Safari

## Styling Highlights

- Custom CSS variables for theme colors
- Glassmorphism effects (backdrop blur)
- Smooth animations and transitions
- Optimized scrollbar styling
- Responsive grid layouts
