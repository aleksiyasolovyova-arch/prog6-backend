# Keep Dishes Going - Food Ordering Platform

A backend-focused implementation of a food ordering platform demonstrating **hexagonal architecture**, **domain-driven design**, and **event sourcing** patterns. The frontend is intentionally minimal to emphasize robust backend engineering.

## Implementation Status

### Backend: Comprehensive
This project showcases a **production-grade backend** built with, hopefully, best practices. The domain logic is thoroughly implemented with clear architectural boundaries, domain-driven principles, and event-sourced aggregates.

### Frontend: Basic UI Layer
The frontend provides **functional but limited** user interface. It covers essential workflows but lacks polish, advanced UX patterns, and comprehensive UI coverage. The focus has been deliberately placed on backend excellence.

---

## Core Architecture

### Backend Implementation

The backend strictly adheres to **hexagonal architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    INPUT ADAPTERS                           │
│  (Web Controllers, Message Listeners, CLI, etc.)            │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              PORTS & USE CASES (CORE)                       │
│  Commands, Queries, Input Ports, Output Ports               │
└────────────────────┬────────────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
    ┌─────────────┐    ┌─────────────────┐
    │ Entities    │    │ Value Objects   │
    │ Aggregates  │    │ Domain Events   │
    └─────────────┘    └─────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────┐
│             OUTPUT ADAPTERS                                 │
│  (Persistence, RabbitMQ, REST Gateways, etc.)               │
└─────────────────────────────────────────────────────────────┘
```

### Domain-Driven Design

Multiple bounded contexts with clear **ubiquitous language** and **context mapping**:

- **Restaurant Context**: Domain model for restaurants, dishes, and menu management
- **Order Context**: Order lifecycle, acceptance decisions, state transitions
- **Delivery Context**: Integration with external delivery service via RabbitMQ
- **User Context**: Owner and customer authentication via OAuth2/Keycloak

---

## ✅ Backend: Implemented Use Cases

### Restaurant Management (Owner)
- **Sign up / Sign in** - OAuth2 authentication with Keycloak
- **Create restaurant** - Full address, contact, cuisine type, preparation time, opening hours
- **Manage dishes as drafts** - Non-breaking edits to live menu
- **Publish/unpublish dishes** - Make dishes available or hidden
- **Batch publish operations** - Apply multiple changes at once
- **Schedule bulk updates** - Publish changes at a specific time
- **Mark dishes out of stock** - Immediate state change (no scheduling)
- **Set opening hours** - Weekly schedule definition
- **Manually open/close** - Override automatic opening hours
- **Enforce dish limit** - Maximum 10 published dishes at any time

### Order Management (Owner)
- **Accept/reject orders** - With optional rejection reason
- **Auto-decline after timeout** - 5-minute window for decisions
- **Mark order ready for pickup** - Signal kitchen completion to delivery service
- **Event publishing** - Integrated RabbitMQ messaging to delivery partner

### Customer-Facing (Minimal)
- **Browse restaurants** - List view with basic filtering
- **View restaurant details** - Dishes with filtering by type and food tags
- **Shopping basket** - Single-restaurant ordering
- **Checkout** - Collect delivery address and contact info
- **Order tracking** - Status updates via event subscriptions
- **Payment integration** - Stripe integration point

### Platform Features
- **Price range evolution** - Event-sourced price category tracking with retroactive adjustments
- **Restaurant busyness factor** - Calculated from pending orders
- **Guesstimated delivery time** - Distance + preparation + busyness formula
- **Delivery service integration** - RabbitMQ event exchange with external partner

---

## ❌ Backend: Not Implemented

- **Delivery zones** - Out of scope per requirements
- **Inventory/stock counts** - Out of scope per requirements  
- **Discounts & loyalty programs** - Out of scope per requirements
- **Multi-currency support** - Out of scope per requirements
- **Restaurant editing** - Out of scope (create only, no updates)

---

## ⚠️ Frontend: Implemented Use Cases

### Owner Interface (Limited)
- **Authentication flow** - OAuth2 login screen
- **Restaurant creation form** - Address entry, cuisine selection, hours setup
- **Dish management** - Create, edit, publish/unpublish (basic UI)
- **Dashboard view** - List of pending orders
- **Accept/reject orders** - Simple modal interface

### Customer Interface (Limited)
- **Restaurant list** - Card-based layout (no map view)
- **Basic filtering** - Cuisine type filter only
- **Restaurant detail** - Dish list with minimal styling
- **Basket functionality** - Add/remove items
- **Checkout form** - Delivery address entry
- **Order tracking** - Status display (polling-based)

---

## ⚠️ Frontend: Limitations & Missing Features

### Not Implemented / Partial

**Restaurant Browsing:**
- ❌ Map view for restaurant location
- ❌ Distance-based filtering
- ❌ Price range filtering  
- ⚠️ Guesstimated delivery time display (backend calculated, UI missing)
- ❌ Price evolution graph (backend event-sourced, frontend not built)
- ❌ Advanced sorting options

**Dish Management:**
- ⚠️ Bulk scheduling interface (basic form exists, no calendar picker)
- ❌ Visual draft vs. live indicator count
- ❌ Batch operations UI feedback
- ⚠️ Food tags filtering (available, basic implementation)

**Order Management:**
- ❌ Real-time 5-minute decision timer UI
- ⚠️ Rejection reason display (minimal styling)
- ⚠️ Courier location tracking (backend events received, UI not visualized)
- ❌ Rich order status timeline

**UX & Design:**
- ❌ Material UI or design system (basic styling only)
- ⚠️ Form validation messaging (minimal)
- ❌ Error boundaries and error recovery UI
- ❌ Loading states and skeletons
- ⚠️ Responsive design (functional but not polished)
- ❌ Accessibility features beyond basics
- ❌ Internationalization

---

## 🛠️ Tech Stack

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Architecture**: Hexagonal Architecture with DDD
- **Database**: PostgreSQL
- **Event Storage**: Event sourcing with snapshots
- **Messaging**: RabbitMQ (AMQP)
- **Security**: OAuth2 / OpenID Connect via Keycloak
- **API**: RESTful with Spring Web
- **Testing**: JUnit 5, Testcontainers

### Frontend
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **State Management**: React Query (TanStack Query)
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Forms**: React Hook Form (partial)
- **Styling**: SCSS (minimal theming)
- **Security**: OAuth2 via Keycloak

---


## 🔄 Key Architectural Patterns

### Event Sourcing
- **Aggregate snapshots** for performance optimization
- **Event store** persisting all state changes

### Commands & Events
- **Domain events** (e.g., `OrderAccepted`, `DishPublished`) 
- **Commands** for state changes (e.g., `AcceptOrderCommand`)
- **Process managers** for cross-aggregate coordination

### Context Mapping
- **Anticorruption layer** between Restaurant and Order contexts-Facade Implementation
- **Published language** for Delivery service integration via RabbitMQ
- **Event-driven integration** between bounded contexts

### Hexagonal Principles
- **Technology-agnostic core domain** - No Spring, JPA, or framework dependencies in domain
- **Clear ports** - All external communication through interfaces
- **Testable adapters** - Mock implementations for testing
- **No framework leakage** - Adapters handle all infrastructure concerns

---

## 📋 User Stories Coverage

### Fully Implemented (Backend + Basic Frontend)
✅ 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 20, 22, 23, 24, 25, 28, 29, 30, 31

### Partially Implemented (Backend Complete, Frontend Lacking)
⚠️ 7 (Scheduling logic exists, calendar UI missing)
⚠️ 14 (List works, map view missing)
⚠️ 15 (Details show, UX minimal)
⚠️ 16 (Backend supports, UI filters missing)
⚠️ 17 (Backend supports, food tag filter basic)
⚠️ 18 (Backend supports, sorting UI missing)
⚠️ 19 (Calculated in backend, not displayed)
⚠️ 21 (Logic exists, error messaging minimal)
⚠️ 26 (Events stored, graph UI not built)
⚠️ 27 (Enforced in backend, UI doesn't display count)

### Not Implemented
❌ None - all functionality has a backend implementation

---

## 🚀 Running the Project

### Prerequisites
- Java 21+
- Node.js 18+
- Docker & Docker Compose
- Git

### Backend Setup
```bash
cd backend

# Start infrastructure (PostgreSQL, RabbitMQ, Keycloak)
docker-compose up -d

# Build and run
./gradlew bootRun
# or ./mvnw spring-boot:run

# API available at: http://localhost:8081
# Keycloak admin: http://localhost:8180/auth/
```

### Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
# Frontend available at: http://localhost:5173
```

---

## 🧪 Testing

### Backend
```bash
cd backend

# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport
```
---

## ✨ Strengths

- **Robust backend** with clear architectural boundaries
- **Domain logic** thoroughly modeled and testable
- **Event-driven** interactions between contexts
- **Security** implemented via OAuth2/Keycloak
- **Asynchronous integration** with external delivery service
- **Scalable patterns** (event sourcing, snapshots, CQRS-ready)

---

## 📌 Weaknesses & Future Improvements

- **Frontend UI** is functional but needs design polish and component framework integration
- **Missing visualizations** (price graph, map, location tracking)
- **Limited UX feedback** (loading states, error handling, confirmations)
- **No responsive design** testing for mobile
- **Incomplete form validation** and error messaging
- **Accessibility** not prioritized in current build

---

## 👥 Credits

Built as a part of Programming 6 course demonstrating:
- Hexagonal Architecture by Alistair Cockburn
- Domain-Driven Design by Eric Evans
- Event Sourcing and CQRS patterns
- OAuth2/OpenID Connect security flows