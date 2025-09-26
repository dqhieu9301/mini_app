CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   username VARCHAR(40) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   full_name VARCHAR(40) NOT NULL,
   total_point INTEGER DEFAULT 0,
   last_token BIGINT,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accumulate_point_history (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   user_id UUID NOT NULL,
   change_type VARCHAR(10) CHECK (change_type IN ('INCREASE', 'DECREASE')),
   amount INT NOT NULL CHECK (amount > 0),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_user_point FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE UNIQUE INDEX ux_user_checkin_once_per_day
    ON accumulate_point_history (user_id, (created_at::date))
    WHERE change_type = 'INCREASE';

CREATE TABLE checkin_point_config (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   day_index INTEGER NOT NULL UNIQUE ,
   point INT NOT NULL CHECK (point > 0),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);