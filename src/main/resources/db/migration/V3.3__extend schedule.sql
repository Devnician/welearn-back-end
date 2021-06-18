ALTER TABLE `schedule`
    ADD COLUMN `start_date` timestamp NOT NULL;

ALTER TABLE `schedule`
    ADD COLUMN `end_date` timestamp NOT NULL;
