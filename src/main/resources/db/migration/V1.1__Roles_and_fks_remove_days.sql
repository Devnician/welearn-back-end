CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  `role_bg` varchar(45),
  `description` varchar(45) NOT NULL,
  `description_bg` varchar(45),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

ALTER TABLE `users`
    DROP COLUMN `role`;

ALTER TABLE `users`
    MODIFY `group_id` varchar(45);

ALTER TABLE `resource`
    MODIFY `group_id` varchar(45);

ALTER TABLE `event`
    MODIFY `group_id` varchar(45);

ALTER TABLE `evaluation_mark`
    MODIFY `group_id` varchar(45);

ALTER TABLE `users`
    ADD COLUMN `role_id` int(6);

ALTER TABLE `users`
    ADD CONSTRAINT `user_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `roles` (`id`)
    ON UPDATE RESTRICT;

ALTER TABLE `schedule`
    DROP CONSTRAINT `schedule_day`;

DROP TABLE `days`;

CREATE TABLE IF NOT EXISTS `group_discipline` (
    `group_id` varchar(45),
    `discipline_id` varchar(45)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

ALTER TABLE `group_discipline`
    ADD CONSTRAINT `grp_constraint`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;

ALTER TABLE `group_discipline`
    ADD CONSTRAINT `discipline_constraint`
    FOREIGN KEY (`discipline_id`)
    REFERENCES `discipline` (`id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;

ALTER TABLE `resource`
    ADD COLUMN `discipline_id` varchar(45);

ALTER TABLE `resource`
    ADD COLUMN `schedule_id` varchar(45);

ALTER TABLE `resource`
    ADD CONSTRAINT `resource_discipline`
    FOREIGN KEY(`discipline_id`)
    REFERENCES `discipline`(`id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;

ALTER TABLE `resource`
    ADD CONSTRAINT `resource_schedule`
    FOREIGN KEY(`schedule_id`)
    REFERENCES `schedule`(`id`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT;

ALTER TABLE `users`
    ADD COLUMN `deleted` int(1);

ALTER TABLE `users`
    ADD COLUMN `logged_in` int(1);
