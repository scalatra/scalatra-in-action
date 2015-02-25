CREATE TABLE areas
(id INT AUTO_INCREMENT,
 name VARCHAR(255),
 location VARCHAR(255),
 longitude FLOAT,
 latitude FLOAT,
 description VARCHAR(255),
 CONSTRAINT pk_areas PRIMARY KEY(id),
 CONSTRAINT co_areas_long CHECK ((longitude >= -180) AND (longitude <= 180)),
 CONSTRAINT co_areas_lat CHECK ((latitude >= -90) AND (latitude <= 90)));

CREATE TABLE routes
(id INT AUTO_INCREMENT,
 areaId INT,
 mountainName VARCHAR(255),
 routeName VARCHAR(255),
 longitude FLOAT,
 latitude FLOAT,
 description VARCHAR(255),
 CONSTRAINT pk_routes PRIMARY KEY(id),
 CONSTRAINT co_routes_long CHECK ((longitude >= -180) AND (longitude <= 180)),
 CONSTRAINT co_routes_lat CHECK ((latitude >= -90) AND (latitude <= 90)));

