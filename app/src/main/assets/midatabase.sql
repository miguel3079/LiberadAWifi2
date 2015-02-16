BEGIN TRANSACTION;
CREATE TABLE USUARIOS_MONSTRUOS(
    id_user INTEGER,
    id_monster INTEGER,
    FOREIGN KEY(id_user) REFERENCES USUARIOS(id) ON DELETE CASCADE,
    FOREIGN KEY(id_monster) REFERENCES MONSTRUOS(id) ON DELETE CASCADE
);
CREATE TABLE "USUARIOS" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT NOT NULL,
	`objectId`	TEXT NOT NULL
);
INSERT INTO `USUARIOS` VALUES ('1','issam1','email@email.com');
CREATE TABLE TEST (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,nombre varchar(40) ,
 casilla int, puntuacion float,monedas int,diamantes int,experiencia float,fecha DATE);
INSERT INTO `TEST` VALUES ('1','Test Matematicas 2+2','2','4.0','0','9','3.0','null');
INSERT INTO `TEST` VALUES ('2','Test Historia America','3','4.0','4','8','9.0','null');
CREATE TABLE RESPUESTAS (id_respuestas INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,respuesta varchar(120),
solucion boolean,id_preguntas int,FOREIGN KEY(id_preguntas) REFERENCES PREGUNTAS(id_preguntas));
INSERT INTO `RESPUESTAS` VALUES ('1','4','1','1');
INSERT INTO `RESPUESTAS` VALUES ('2','3','0','1');
INSERT INTO `RESPUESTAS` VALUES ('3','2','0','1');
INSERT INTO `RESPUESTAS` VALUES ('4','0','0','1');
INSERT INTO `RESPUESTAS` VALUES ('5','1500','0','2');
INSERT INTO `RESPUESTAS` VALUES ('6','1492','1','2');
INSERT INTO `RESPUESTAS` VALUES ('7','1491','0','2');
INSERT INTO `RESPUESTAS` VALUES ('8','ayer','0','2');
CREATE TABLE "PREGUNTAS" (
	`id_preguntas`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`titulo`	varchar(120) NOT NULL,
	`categoria`	varchar(25) NOT NULL,
	`imagen`	TEXT NOT NULL,
	`fid_preguntas`	int NOT NULL,
	`tipo`	TEXT,
	`Consejo`	TEXT
);
INSERT INTO `PREGUNTAS` VALUES ('1','¿Cuantos son 2+2?','Matematicas','mimonstruo1.png','1','texto','mas facil imposible joder
');
INSERT INTO `PREGUNTAS` VALUES ('2','¿En que año se descubrió America?','Historia','mimonstruo1','2','imagen','Ayer no fue, eso es seguro');
CREATE TABLE "MONSTRUOS" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT NOT NULL,
	`escomprado`	INTEGER NOT NULL,
	`monedas`	INTEGER,
	`diamantes`	INTEGER
);
INSERT INTO `MONSTRUOS` VALUES ('1','monstruo1
','1','100','6');
INSERT INTO `MONSTRUOS` VALUES ('2','monstruo2
','0','250','0');
COMMIT;
