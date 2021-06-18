CREATE TABLE IF NOT EXISTS `groups` (
  `group_id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(45),
  `start_date` timestamp NOT NULL,
  `end_date` timestamp NOT NULL,
  `max_resources_mb` int NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` varchar(45) NOT NULL,
  `group_id` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(45) NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `resource` (
  `resource_id` varchar(45) NOT NULL,
  `group_id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `dir_path` varchar(45) NOT NULL,
  `accessible_all` BOOLEAN NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `event` (
  `event_id` varchar(45) NOT NULL,
  `group_id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `start_date` timestamp NOT NULL,
  `end_date` timestamp NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `discipline` (
  `id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `days` (
  `id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` varchar(45) NOT NULL,
  `group_id` varchar(45) NOT NULL,
  `day_id` varchar(45) NOT NULL,
  `discipline_id` varchar(45) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `evaluation_mark` (
  `id` varchar(45) NOT NULL,
  `group_id` varchar(45) NOT NULL,
  `user_id` varchar(45) NOT NULL,
  `discipline_id` varchar(45) NOT NULL,
  `mark_value` double NOT NULL,
  `creation_date` timestamp,
  `modified_date` timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

CREATE TABLE IF NOT EXISTS `blacklist` (
  `event_id` varchar(45) NOT NULL,
  `user_id` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

ALTER TABLE `users`
    ADD CONSTRAINT `user_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
ON DELETE RESTRICT;

ALTER TABLE `resource`
    ADD CONSTRAINT `resource_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `event`
    ADD CONSTRAINT `event_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `schedule`
    ADD CONSTRAINT `schedule_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `schedule`
    ADD CONSTRAINT `schedule_day`
    FOREIGN KEY (`day_id`)
    REFERENCES `days` (`id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `schedule`
    ADD CONSTRAINT `schedule_discipline`
    FOREIGN KEY (`discipline_id`)
    REFERENCES `discipline` (`id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `evaluation_mark`
    ADD CONSTRAINT `mark_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `groups` (`group_id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `evaluation_mark`
    ADD CONSTRAINT `mark_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON UPDATE RESTRICT
ON DELETE CASCADE;

ALTER TABLE `evaluation_mark`
    ADD CONSTRAINT `mark_discipline`
    FOREIGN KEY (`discipline_id`)
    REFERENCES `discipline` (`id`)
    ON UPDATE RESTRICT
ON DELETE RESTRICT;

ALTER TABLE `blacklist`
    ADD CONSTRAINT `blacklist_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`event_id`)
    ON UPDATE RESTRICT
ON DELETE RESTRICT;

ALTER TABLE `blacklist`
    ADD CONSTRAINT `blacklist_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON UPDATE RESTRICT
ON DELETE RESTRICT;