ALTER TABLE `users`
    ADD COLUMN `middle_name` varchar(45);

ALTER TABLE `users`
    ADD COLUMN `address` varchar(45);

ALTER TABLE `users`
    ADD COLUMN `birthdate` varchar(45);

ALTER TABLE `users`
    ADD COLUMN `phone_number` varchar(45);

ALTER TABLE `roles`
    ADD COLUMN `permissions` varchar(255);

ALTER TABLE `discipline`
    ADD COLUMN `teacher_id` varchar(45);

ALTER TABLE `discipline`
    ADD COLUMN `assistant_id` varchar(45);

ALTER TABLE `discipline`
    ADD CONSTRAINT `discipline_teacher`
    FOREIGN KEY (`teacher_id`)
    REFERENCES `users`(`user_id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;

ALTER TABLE `discipline`
    ADD CONSTRAINT `discipline_assistant`
    FOREIGN KEY (`assistant_id`)
    REFERENCES `users`(`user_id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;
