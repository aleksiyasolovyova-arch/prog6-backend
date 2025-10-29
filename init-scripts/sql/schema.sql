-- ========================================
-- Keep Dishes Going - Database Schema
-- PostgreSQL 17
-- ========================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ========================================
-- OWNERS TABLE
-- ========================================
CREATE TABLE owners (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- RESTAURANTS TABLE
-- ========================================
CREATE TABLE restaurants (
                             id UUID PRIMARY KEY,
                             owner_id UUID NOT NULL REFERENCES owners(id),
                             name VARCHAR(255) NOT NULL,

    -- Address (embedded)
                             address_street VARCHAR(255),
                             address_number VARCHAR(10),
                             address_postal_code VARCHAR(10),
                             address_city VARCHAR(100),
                             address_country VARCHAR(100),

                             contact_email VARCHAR(255) NOT NULL,
                             picture_urls TEXT,  -- Store as JSON array or comma-separated
                             cuisine_type VARCHAR(50) NOT NULL,
                             default_preparation_time_minutes INT NOT NULL,

    -- Opening Hours (separate columns per day)
                             opening_hours_monday VARCHAR(50),      -- e.g., "09:00-18:00"
                             opening_hours_tuesday VARCHAR(50),
                             opening_hours_wednesday VARCHAR(50),
                             opening_hours_thursday VARCHAR(50),
                             opening_hours_friday VARCHAR(50),
                             opening_hours_saturday VARCHAR(50),
                             opening_hours_sunday VARCHAR(50),

    -- Manual override state
                             manually_open BOOLEAN DEFAULT FALSE,
                             manually_closed BOOLEAN DEFAULT FALSE,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_restaurants_owner_id ON restaurants(owner_id);
CREATE INDEX idx_restaurants_created_at ON restaurants(created_at);

-- ========================================
-- DISHES TABLE (Published)
-- ========================================
CREATE TABLE dishes (
                        id UUID PRIMARY KEY,
                        restaurant_id UUID NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,

    -- Price (Money object embedded)
                        price_amount DECIMAL(10, 2) NOT NULL,
                        price_currency VARCHAR(3) NOT NULL DEFAULT 'EUR',

    -- Dish type (STARTER, MAIN, DESSERT, etc.)
                        dish_type VARCHAR(50) NOT NULL,

    -- Stock/availability
                        is_available_for_order BOOLEAN DEFAULT TRUE,

                        preparation_time_minutes INT,

                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_dishes_restaurant_id ON dishes(restaurant_id);
CREATE INDEX idx_dishes_available ON dishes(is_available_for_order);

-- ========================================
-- DISH DRAFTS TABLE
-- ========================================
CREATE TABLE dish_drafts (
                             id UUID PRIMARY KEY,
                             restaurant_id UUID NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
                             original_dish_id UUID REFERENCES dishes(id) ON DELETE CASCADE,  -- NULL if new dish

    -- Drafted details
                             name VARCHAR(255) NOT NULL,
                             description TEXT,

    -- Price (Money object embedded)
                             price_amount DECIMAL(10, 2) NOT NULL,
                             price_currency VARCHAR(3) NOT NULL DEFAULT 'EUR',

    -- Dish type (STARTER, MAIN, DESSERT, etc.)
                             dish_type VARCHAR(50) NOT NULL,

                             preparation_time_minutes INT,

    -- Draft state
                             state VARCHAR(50) NOT NULL DEFAULT 'DRAFT',  -- DRAFT, SCHEDULED
                             scheduled_publish_at TIMESTAMP,

                             is_new_dish BOOLEAN NOT NULL,  -- true = new dish, false = editing existing

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_dish_drafts_restaurant_id ON dish_drafts(restaurant_id);
CREATE INDEX idx_dish_drafts_original_dish_id ON dish_drafts(original_dish_id);
CREATE INDEX idx_dish_drafts_scheduled ON dish_drafts(scheduled_publish_at);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
                                      id UUID PRIMARY KEY,
                                      customer_name VARCHAR(255) NOT NULL,
                                      customer_email VARCHAR(255) NOT NULL,
                                      delivery_street VARCHAR(255) NOT NULL,
                                      delivery_number VARCHAR(50) NOT NULL,
                                      delivery_postal_code VARCHAR(20) NOT NULL,
                                      delivery_city VARCHAR(100) NOT NULL,
                                      delivery_country VARCHAR(100) NOT NULL,
                                      restaurant_id UUID NOT NULL,
                                      restaurant_name VARCHAR(255) NOT NULL,
                                      status VARCHAR(50) NOT NULL,
                                      total_amount DECIMAL(10, 2) NOT NULL,
                                      ordered_at TIMESTAMP NOT NULL,
                                      estimated_ready_at TIMESTAMP
);

-- Order lines table
CREATE TABLE IF NOT EXISTS order_lines (
                                           id UUID PRIMARY KEY,
                                           order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                                           dish_id UUID NOT NULL,
                                           dish_name VARCHAR(255) NOT NULL,
                                           price_at_order_time DECIMAL(10, 2) NOT NULL,
                                           quantity INTEGER NOT NULL
);

-- Indexes
CREATE INDEX idx_orders_restaurant ON orders(restaurant_id);
CREATE INDEX idx_orders_customer_email ON orders(customer_email);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_lines_order ON order_lines(order_id);

ALTER TABLE orders ADD COLUMN IF NOT EXISTS decision_deadline TIMESTAMP;

-- ========================================
-- DOMAIN EVENTS TABLE (for event sourcing/audit)
-- ========================================
CREATE TABLE domain_events (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               aggregate_id UUID NOT NULL,
                               aggregate_type VARCHAR(255) NOT NULL,
                               event_type VARCHAR(255) NOT NULL,
                               event_payload JSONB NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_domain_events_aggregate_id ON domain_events(aggregate_id);
CREATE INDEX idx_domain_events_event_type ON domain_events(event_type);
CREATE INDEX idx_domain_events_created_at ON domain_events(created_at);

-- ========================================
-- OUTBOX TABLE (for reliable event publishing to RabbitMQ)
-- ========================================
CREATE TABLE outbox (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        aggregate_id UUID NOT NULL,
                        event_type VARCHAR(255) NOT NULL,
                        payload JSONB NOT NULL,
                        published BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        published_at TIMESTAMP
);

CREATE INDEX idx_outbox_published ON outbox(published);
CREATE INDEX idx_outbox_created_at ON outbox(created_at);

-- ========================================
-- SAMPLE DATA (OPTIONAL - for development/testing)
-- ========================================

-- Sample owner
INSERT INTO owners (id, email, password_hash)
VALUES (
           'f47ac10b-58cc-4372-a567-0e02b2c3d479',
           'owner@legourmet.com',
           '$2a$10$dummy_hash_for_testing'
       ) ON CONFLICT DO NOTHING;

-- Sample restaurant
INSERT INTO restaurants (
    id,
    owner_id,
    name,
    address_street,
    address_city,
    address_postal_code,
    contact_email,
    cuisine_type,
    default_preparation_time_minutes,
    opening_hours_monday,
    opening_hours_tuesday,
    opening_hours_wednesday,
    opening_hours_thursday,
    opening_hours_friday,
    opening_hours_saturday,
    opening_hours_sunday
) VALUES (
             '550e8400-e29b-41d4-a716-446655440000',
             'f47ac10b-58cc-4372-a567-0e02b2c3d479',
             'Le Gourmet',
             'Rue de Paris',
             'Brussels',
             '1000',
             'owner@legourmet.com',
             'FRENCH',
             20,
             '09:00-18:00',
             '09:00-18:00',
             '09:00-18:00',
             '09:00-18:00',
             '09:00-18:00',
             '10:00-16:00',
             ''  -- Closed on Sunday
         ) ON CONFLICT DO NOTHING;

-- Sample published dish
INSERT INTO dishes (
    id,
    restaurant_id,
    name,
    description,
    price_amount,
    price_currency,
    dish_type,
    is_available_for_order,
    preparation_time_minutes
) VALUES (
             'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
             '550e8400-e29b-41d4-a716-446655440000',
             'Coq au Vin',
             'Traditional French chicken stew with red wine and mushrooms',
             24.50,
             'EUR',
             'MAIN',
             TRUE,
             30
         ) ON CONFLICT DO NOTHING;

INSERT INTO dishes (
    id,
    restaurant_id,
    name,
    description,
    price_amount,
    price_currency,
    dish_type,
    is_available_for_order,
    preparation_time_minutes
) VALUES (
             'b1ffcd10-a0d0-5fg9-cc7e-7cc0ce491b22',
             '550e8400-e29b-41d4-a716-446655440000',
             'Crème Brûlée',
             'Classic French vanilla custard with caramelized sugar top',
             8.50,
             'EUR',
             'DESSERT',
             TRUE,
             15
         ) ON CONFLICT DO NOTHING;

-- ========================================
-- GRANT PERMISSIONS
-- ========================================
GRANT USAGE ON SCHEMA public TO "user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "user";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "user";
