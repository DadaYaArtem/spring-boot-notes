CREATE TABLE users
(
    id UUID DEFAULT gen_random_uuid(),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    is_active boolean NOT NULL,
    activation_code VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE notes
(
    id UUID DEFAULT gen_random_uuid(),
    name_notes VARCHAR(100) NOT NULL UNIQUE,
    content VARCHAR(10000) NOT NULL,
    visibility VARCHAR(100) NOT NULL,
    user_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id)
);
