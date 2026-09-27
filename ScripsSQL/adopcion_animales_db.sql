-- drop database if exists adopcion_animales_db;
 create database adopcion_animales_db;

use adopcion_animales_db;

CREATE TABLE animales (
    id_animal INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(80) NOT NULL,
    edad INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Disponible'
);

INSERT INTO animales 
(nombre, especie, raza, edad, estado)
VALUES
('Max', 'Perro', 'Labrador', 3, 'Disponible'),
('Luna', 'Gato', 'Siamés', 2, 'Disponible'),
('Rocky', 'Perro', 'Mestizo', 5, 'En proceso'),
('Nala', 'Gato', 'Persa', 1, 'Disponible'),
('Coco', 'Conejo', 'Enano', 2, 'Adoptado');

DELIMITER $$

CREATE PROCEDURE sp_cambiar_estado(
    IN _id_animal INT,
    IN _estado VARCHAR(20)
)
BEGIN

    UPDATE animales
    SET estado = _estado
    WHERE id_animal = _id_animal;

END $$

DELIMITER ;

SELECT * FROM animales;