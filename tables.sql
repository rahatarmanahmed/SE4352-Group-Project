DROP TABLE lines;
DROP TABLE shifts;
CREATE TABLE lines (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT NOT NULL,
    description TEXT NOT NULL,
    clicks INTEGER DEFAULT 0
);

CREATE TABLE shifts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lineId INTEGER NOT NULL,
    shifted_description TEXT NOT NULL
);