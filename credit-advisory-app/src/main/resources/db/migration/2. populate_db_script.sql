INSERT INTO advisor (id, email, username, first_name, last_name, role) VALUES (1, 'john.doe@gmail.com', 'jdoe', 'John', 'Doe', 'ASSOCIATE');
INSERT INTO advisor (id, email, username, first_name, last_name, role) VALUES (2, 'samuel.jackson@gmail.com', 'sjackson', 'Samuel', 'Jackson', 'PARTNER');
INSERT INTO advisor (id, email, username, first_name, last_name, role) VALUES (3, 'ron.corney@gmail.com', 'rcorney', 'Ron', 'Corney', 'SENIOR');

INSERT INTO applicant (id, email, username, first_name, last_name, social_security_number, city, street, number, zip, apt)
VALUES (4, 'roy.jones@gmail.com', 'rjones', 'Roy', 'Jones', '1NXT536F', 'Colorado', 'Algonquin Way', '1', '2100', 12),
       (5, 'robert.martin@gmail.com', 'rmartin', 'Robert', 'Martin', '1ZNG20I9', 'Colorado', 'Bay Meadows', '12', '3500', 7);

INSERT INTO application (id, amount_of_money_usd, status, created_at, applicant_id)
VALUES (7, 70000, 'NEW', NOW(), 5),
       (8, 15000, 'NEW', NOW(), 4);