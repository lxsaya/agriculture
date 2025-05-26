CREATE TABLE IF NOT EXISTS agriculture.plant (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    growth_period INT,
    created_at  timestamp NOT NULL DEFAULT now(),
    modified_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS agriculture.field (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    area DECIMAL(10,2),
    soil_type VARCHAR(100),
    created_at  timestamp NOT NULL DEFAULT now(),
    modified_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS agriculture.worker (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(255) NOT NULL,
    position VARCHAR(100),
    experience INT,
    created_at  timestamp NOT NULL DEFAULT now(),
    modified_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS agriculture.sowing_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sowing_date DATE,
    is_field_sown BOOLEAN DEFAULT FALSE,
    quantity_kg DECIMAL(10,2),
    created_at  timestamp NOT NULL DEFAULT now(),
    modified_at timestamp NOT NULL DEFAULT now(),
    worker_id UUID,
    plant_id UUID,
    field_id UUID,
    FOREIGN KEY (worker_id) REFERENCES agriculture.worker(id),
    FOREIGN KEY (plant_id) REFERENCES agriculture.plant(id),
    FOREIGN KEY (field_id) REFERENCES agriculture.field(id)
);