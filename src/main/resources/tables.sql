CREATE TABLE IF NOT EXISTS person (
        first CHAR(30) NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        last CHAR(30) NOT NULL,
        middle CHAR(30) DEFAULT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS user (
        id INT NOT NULL AUTO_INCREMENT,
        login CHAR(30) NOT NULL,
        password CHAR(50) NOT NULL,
        person_id INT NOT NULL,
        role_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        salt CHAR(50) NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS contact (
        contact_type_id INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        person_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        valid BIT(1) NOT NULL,
        value CHAR(30) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS person_address (
        address_id INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        person_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (address_id) REFERENCES address (id),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS insurance_company (
        address_id INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        name CHAR(30) NOT NULL,
        phone_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (address_id) REFERENCES address (id),
        CONSTRAINT FOREIGN KEY (phone_id) REFERENCES contact (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS birth_certificate (
        expire_date TIMESTAMP NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        number CHAR(30) NOT NULL,
        person_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS passport (
        authority CHAR(30) NOT NULL,
        birth_date TIMESTAMP NOT NULL,
        birth_locality_id INT NOT NULL,
        country_id INT NOT NULL,
        expire_date TIMESTAMP NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        issue_date TIMESTAMP NOT NULL,
        nationality CHAR(30) DEFAULT NULL,
        number CHAR(30) NOT NULL,
        person_id INT NOT NULL,
        photo_id INT DEFAULT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        sex_id INT NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (birth_locality_id) REFERENCES locality (id),
        CONSTRAINT FOREIGN KEY (country_id) REFERENCES country (id),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (photo_id) REFERENCES photo (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS drive_license (
        birth_date TIMESTAMP NOT NULL,
        birth_locality_id INT NOT NULL,
        categories CHAR(30) NOT NULL,
        country_id INT NOT NULL,
        expire_date TIMESTAMP NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        issue_date TIMESTAMP NOT NULL,
        number CHAR(30) NOT NULL,
        person_id INT NOT NULL,
        photo_id INT DEFAULT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (birth_locality_id) REFERENCES locality (id),
        CONSTRAINT FOREIGN KEY (country_id) REFERENCES country (id),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (photo_id) REFERENCES photo (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS health_insurance_policy (
        birth_date TIMESTAMP NOT NULL,
        birth_locality_id INT NOT NULL,
        country_id INT NOT NULL,
        expire_date TIMESTAMP NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        insurance_company_id INT NOT NULL,
        issue_date TIMESTAMP NOT NULL,
        number CHAR(30) NOT NULL,
        person_id INT NOT NULL,
        register_date TIMESTAMP NOT NULL,
        register_person CHAR(30) DEFAULT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        sex_id INT NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (birth_locality_id) REFERENCES locality (id),
        CONSTRAINT FOREIGN KEY (country_id) REFERENCES country (id),
        CONSTRAINT FOREIGN KEY (insurance_company_id) REFERENCES insurance_company (id),
        CONSTRAINT FOREIGN KEY (person_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS country (
        id INT NOT NULL AUTO_INCREMENT,
        name CHAR(30) NOT NULL,
        PRIMARY KEY (id) );

CREATE TABLE IF NOT EXISTS region (
        country_id INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        name CHAR(30) NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT FOREIGN KEY (country_id) REFERENCES country (id) );

CREATE TABLE IF NOT EXISTS locality (
        id INT NOT NULL AUTO_INCREMENT,
        locality_type_id INT NOT NULL,
        name CHAR(30) NOT NULL,
        region_id INT NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT FOREIGN KEY (region_id) REFERENCES region (id) );

CREATE TABLE IF NOT EXISTS locality (
        id INT NOT NULL AUTO_INCREMENT,
        locality_type_id INT NOT NULL,
        name CHAR(30) NOT NULL,
        region_id INT NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT FOREIGN KEY (region_id) REFERENCES region (id) );

CREATE TABLE IF NOT EXISTS location (
        height DOUBLE DEFAULT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        lat DOUBLE NOT NULL,
        lon DOUBLE NOT NULL,
        PRIMARY KEY (id) );

CREATE TABLE IF NOT EXISTS visit (
        address_id INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        patient_id INT NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        time TIMESTAMP NOT NULL,
        visit_type_id INT NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (address_id) REFERENCES address (id),
        CONSTRAINT FOREIGN KEY (patient_id) REFERENCES person (id),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS document (
        description CHAR(30) DEFAULT NULL,
        filename CHAR(30) NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        mime_type CHAR(30) NOT NULL,
        pages INT DEFAULT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        size INT DEFAULT NULL,
        stream BLOB NOT NULL,
        valid BIT(1) NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );

CREATE TABLE IF NOT EXISTS image (
        description CHAR(30) DEFAULT NULL,
        filename CHAR(30) NOT NULL,
        height INT NOT NULL,
        id INT NOT NULL AUTO_INCREMENT,
        mime_type CHAR(30) NOT NULL,
        row_created_by_id INT NOT NULL,
        row_created_time TIMESTAMP NOT NULL,
        size INT DEFAULT NULL,
        stream BLOB NOT NULL,
        valid BIT(1) NOT NULL,
        width INT NOT NULL,
        PRIMARY KEY (id, row_created_time),
        CONSTRAINT FOREIGN KEY (row_created_by_id) REFERENCES person (id) );
