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

ALTER TABLE dish_drafts ADD COLUMN scheduled_publish_at TIMESTAMP;
ALTER TABLE dish_drafts ADD COLUMN is_scheduled BOOLEAN DEFAULT FALSE;

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
-- GRANT PERMISSIONS
-- ========================================
GRANT USAGE ON SCHEMA public TO "user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "user";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "user";


-- ========================================
-- Sample Owners
-- ========================================
-- ========================================
-- Owners
-- ========================================
-- Insert owner
INSERT INTO owners (
    id,
    email,
    password_hash
) VALUES (
             'f47ac10b-58cc-4372-a567-0e02b2c3d479',  -- owner ID used in restaurants
             'owner@legourmet.com',                     -- owner's email
             '$2a$10$dummy_hash_for_testing'            -- password hash (dummy for testing)
         );


-- ========================================
-- Restaurants
-- ========================================
-- Insert a single restaurant
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
    monday,
    tuesday,
    wednesday,
    thursday,
    friday,
    saturday,
    sunday,
    created_at
) VALUES (
             '550e8400-e29b-41d4-a716-446655440000',  -- restaurant ID used in dish_drafts
             'f47ac10b-58cc-4372-a567-0e02b2c3d479',  -- owner ID (must exist in owners table)
             'Le Gourmet',                             -- name
             'Rue de Paris',                           -- street
             'Brussels',                               -- city
             '1000',                                   -- postal code
             'info@legourmet.com',                     -- contact email
             'FRENCH',                                 -- cuisine type
             30,                                       -- default preparation time in minutes
             '09:00-18:00',                            -- monday
             '09:00-18:00',                            -- tuesday
             '09:00-18:00',                            -- wednesday
             '09:00-18:00',                            -- thursday
             '09:00-18:00',                            -- friday
             '10:00-16:00',                            -- saturday
             '',                                       -- sunday (closed)
             NOW()                                     -- created_at
         );


-- ========================================
-- Dishes
-- ========================================
-- ========================================
-- Dishes (corrected UUIDs)
-- ========================================
-- Insert two dishes for a restaurant
INSERT INTO dishes (
    id,
    restaurant_id,
    name,
    description,
    dish_type,
    price_amount,
    price_currency,
    available_for_order,
    created_at,
    updated_at,
    food_tags,
    picture_url
) VALUES
      (
          gen_random_uuid(),                  -- dish 1 ID
          '550e8400-e29b-41d4-a716-446655440000',  -- restaurant_id
          'Caprese Salad',                    -- name
          'Fresh mozzarella, tomatoes, and basil drizzled with olive oil',  -- description
          'START',                            -- dish_type
          7.50,                               -- price_amount
          'EUR',                              -- price_currency
          TRUE,                               -- available_for_order
          NOW(),                              -- created_at
          NOW(),                              -- updated_at
          'VEGETARIAN',                -- food_tags
          'https://example.com/caprese.jpg'  -- picture_url
      ),
      (
          gen_random_uuid(),                  -- dish 2 ID
          '550e8400-e29b-41d4-a716-446655440000',  -- restaurant_id
          'Spaghetti Carbonara',              -- name
          'Classic Italian pasta with pancetta, egg, and parmesan',  -- description
          'MAIN',                             -- dish_type
          13.50,                              -- price_amount
          'EUR',                              -- price_currency
          TRUE,                               -- available_for_order
          NOW(),                              -- created_at
          NOW(),                              -- updated_at
          'VEGAN',                   -- food_tags
          'https://example.com/carbonara.jpg' -- picture_url
      );

-- ========================================
-- Dish Drafts
-- ========================================
-- Insert two dish drafts for a restaurant
INSERT INTO dish_drafts (
    id,
    restaurant_id,
    original_dish_id,
    name,
    description,
    dish_type,
    price_amount,
    price_currency,
    is_new_dish,
    is_scheduled,
    created_at,
    updated_at,
    scheduled_publish_at,
    food_tags,
    picture_url
) VALUES
      (
          gen_random_uuid(),                  -- draft 1 ID
          '550e8400-e29b-41d4-a716-446655440000',  -- restaurant_id
          NULL,                               -- original_dish_id (new dish)
          'Tomato Bruschetta',                -- name
          'Grilled bread topped with fresh tomato, garlic and basil',  -- description
          'START',                            -- dish_type
          6.50,                               -- price_amount
          'EUR',                              -- price_currency
          TRUE,                               -- is_new_dish
          FALSE,                              -- is_scheduled
          NOW(),                              -- created_at
          NOW(),                              -- updated_at
          NULL,                               -- scheduled_publish_at
          'VEGETARIAN',            -- food_tags
          'https://example.com/bruschetta.jpg' -- picture_url
      ),
      (
          gen_random_uuid(),                  -- draft 2 ID
          '550e8400-e29b-41d4-a716-446655440000',  -- restaurant_id
          NULL,                               -- original_dish_id (new dish)
          'Margherita Pizza',                 -- name
          'Classic pizza with tomato, mozzarella and fresh basil',  -- description
          'MAIN',                             -- dish_type
          12.50,                              -- price_amount
          'EUR',                              -- price_currency
          TRUE,                               -- is_new_dish
          FALSE,                              -- is_scheduled
          NOW(),                              -- created_at
          NOW(),                              -- updated_at
          NULL,                               -- scheduled_publish_at
          'VEGETARIAN',                -- food_tags
          'https://example.com/margherita.jpg' -- picture_url
      );

-- ========================================
-- Sample Orders
-- ========================================
INSERT INTO orders (
    id,
    restaurant_id,
    restaurant_name,
    customer_name,
    customer_email,
    delivery_street,
    delivery_number,
    delivery_postal_code,
    delivery_city,
    delivery_country,
    status,
    total_amount,
    ordered_at,
    estimated_ready_at,
    decision_deadline
) VALUES (
             '11111111-1111-1111-1111-111111111111', -- order UUID
             '550e8400-e29b-41d4-a716-446655440000', -- restaurant ID
             'La Trattoria',                           -- restaurant name
             'John Doe',                               -- customer name
             'john.doe@example.com',                   -- customer email
             'Rue de la Paix',                         -- delivery street
             '42',                                     -- delivery number
             '1000',                                   -- delivery postal code
             'Brussels',                               -- delivery city
             'Belgium',                                -- delivery country
             'PENDING',                                -- order status
             49.00,                                    -- total amount
             CURRENT_TIMESTAMP,                        -- ordered_at
             CURRENT_TIMESTAMP + INTERVAL '30 minutes',-- estimated_ready_at
             CURRENT_TIMESTAMP + INTERVAL '1 hour'     -- decision_deadline
         );

-- ========================================
-- Sample Order Lines
-- ========================================
-- Sample order lines
INSERT INTO order_lines (
    id,
    order_id,
    dish_id,
    dish_name,
    price_at_order_time,
    quantity
) VALUES
      (
          '22222222-2222-2222-2222-222222222222',       -- order line UUID
          '11111111-1111-1111-1111-111111111111',       -- order UUID
          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',       -- first dish UUID
          'Coq au Vin',                                 -- first dish name
          24.50,                                        -- price at order time
          1                                             -- quantity
      ),
      (
          '33333333-3333-3333-3333-333333333333',       -- order line UUID
          '11111111-1111-1111-1111-111111111111',       -- order UUID
          'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',       -- second dish UUID
          'Crème Brûlée',                               -- second dish name
          8.50,                                         -- price at order time
          2                                             -- quantity
      );
