
-- areas

INSERT INTO areas (id, name, location, latitude, longitude, description) VALUES
(1, 'Sicilia / Sicily', 'Italy, Europe', 37.61423, 13.93616,
'With more than 25000 square km Sicily is the largest island of the Mediterranean.');
-- ?

INSERT INTO areas (id, name, location, latitude, longitude, description) VALUES
(2, 'Southwest Absaroka Range', 'Wyoming, United States, North America', 43.75461, 110.04421,
'The Wyoming Absarokas are a vast, broad range, and their character and composition (although most of the range is volcanic in origin) vary in different sections of the range.');
-- 15295 ft / 4662 m

INSERT INTO areas (id, name, location, latitude, longitude, description) VALUES
(3, 'Alaska Coast Range Region', 'Alaska, United States, North America', 58.76820, 133.94531,
'The Alaska Coast Range extends from the British Columbia Coast Range of Canada in the South to the Saint Elias Range and Icefield Range in the North.');
-- 11896 ft / 3626 m

INSERT INTO areas (id, name, location, latitude, longitude, description) VALUES
(4, 'Yosemite National Park', 'California, United States, North America', 37.51, 119.3404,
'Yosemite National Park is a United States National Park spanning eastern portions of Tuolumne, Mariposa and Madera counties in the central eastern portion of the U.S. state of California.');
-- ?

INSERT INTO areas (id, name, location, latitude, longitude, description) VALUES
(5, 'Magic Wood', 'Switzerland, Europe', 46.3347, 9.2612,
'Hidden in the forest of the Avers valley there are some of the best boulder problems we have ever seen.');

-- routes

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(1, 1, 'Rocca Busambra', 'Crest Traverse', 37.85590, 13.40040,
'The crest traverse is a deserving, though somewhat strenuous and exposed route to the summit of the mountain.');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(2, 2, 'Citadel Mountain', 'Bobcat-Houlihan', 44.33550, 109.567,
'For mountaineers who like solitude, adventure, and remote peaks that are rarely (if ever) climbed, Wyomings Absaroka Range is heaven.');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(3, 3, 'Alaska Coast Range Trail', 'Hike to Devils Punchbowl', 58.76820, 133.94531,
'This is a great hike for fit hikers with 5-6 hours of time in Skagway.');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(4, 3, 'Alaska Coast Range Trail', 'Chilkoot Trail', 58.76820, 133.94531,
'The trail is typically hiked from south to north, i.e. from Alaska into British Columbia. Skagway, Alaska is the logical jumping off point for hikers on the Chilkoot.');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(5, 4, NULL, 'Midnight Lightning', 37.7418, 119.602,
'Midnight Lightning is a bouldering problem on the Columbia Boulder in Camp 4 of Yosemite National Park.');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(6, 5, NULL, 'Downunder', 46.3347, 9.2612, 'Boulder, 7C+ at Sofasurfer');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(7, 5, NULL, 'The Wizard', 46.3347, 9.2612, 'Boulder, 7C at Beachbloc');

INSERT INTO routes (id, areaId, mountainName, routeName, latitude, longitude, description) VALUES
(8, 5, NULL, 'Master of a cow', 46.3347, 9.2612, 'Boulder, 7C+ at Man of a cow');


-- http://en.wikipedia.org/wiki/Category:Climbing_routes
-- http://en.wikipedia.org/wiki/Midnight_Lightning_(bouldering)
-- http://en.wikipedia.org/wiki/The_Nose_(El_Capitan)#
-- http://www.thecrag.com/climbing/india/hampi/area/415927959


--http://27crags.com/crags/magic-wood/
--http://27crags.com/crags/magic-wood/routes/unendliche-geschichte-1
--http://27crags.com/crags/magic-wood/routes/downunder
--http://27crags.com/crags/magic-wood/topos/sofasurfer
--http://27crags.com/crags/magic-wood/routes/master-of-a-cow
--http://27crags.com/crags/magic-wood/routes/the-wizard-24045
--http://27crags.com/crags/magic-wood/routes/la-boume-de-luxe
--http://27crags.com/crags/magic-wood/routes/bingo-bongo-128539