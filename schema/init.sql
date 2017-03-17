CREATE TABLE account
(
    account_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created DATETIME(3) NOT NULL,
    email VARCHAR(256) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    modified DATETIME(3) NOT NULL,
    password VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX account_by_email ON account (email);
CREATE TABLE account_badge
(
    account_badge_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    comment VARCHAR(512),
    created DATETIME(3) NOT NULL,
    account_id BIGINT(20) NOT NULL,
    badge_id BIGINT(20) NOT NULL,
    CONSTRAINT fk_account_badge_account_id FOREIGN KEY (account_id) REFERENCES account (account_id),
    CONSTRAINT fk_account_badge_badge_id FOREIGN KEY (badge_id) REFERENCES badge (badge_id)
);
CREATE UNIQUE INDEX account_badge_by_account_id_badge_id ON account_badge (account_id, badge_id);
CREATE INDEX fk_account_badge_badge_id ON account_badge (badge_id);
CREATE TABLE account_privilege
(
    account_id BIGINT(20) NOT NULL,
    privilege_id INT(11) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (account_id, privilege_id),
    CONSTRAINT fk_account_privilege_account_id FOREIGN KEY (account_id) REFERENCES account (account_id)
);
CREATE TABLE badge
(
    badge_id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created DATETIME(3) NOT NULL,
    description VARCHAR(512) NOT NULL,
    full_image LONGBLOB,
    modified DATETIME(3) NOT NULL,
    thumbnail_image LONGBLOB,
    title VARCHAR(64) NOT NULL,
    owner_account_id BIGINT(20) NOT NULL,
    CONSTRAINT fk_badge_owner_account_id FOREIGN KEY (owner_account_id) REFERENCES account (account_id)
);
CREATE UNIQUE INDEX badge_by_title ON badge (title);
CREATE INDEX fk_badge_owner_account_id ON badge (owner_account_id);