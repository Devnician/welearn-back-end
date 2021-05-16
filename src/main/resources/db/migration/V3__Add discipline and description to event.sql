ALTER TABLE `event`
    ADD COLUMN `discipline_id` varchar(45) not null;

ALTER TABLE `event`
    ADD COLUMN `description` varchar(45);

ALTER TABLE `event`
    ADD CONSTRAINT `event_discipline`
    FOREIGN KEY (`discipline_id`)
    REFERENCES `discipline`(`id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;
